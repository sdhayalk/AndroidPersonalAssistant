package com.example.sahil.androidpersonalassistant;
//API KEY = AIzaSyAWAY4zfihHOlrdy9dN2JINy0fiSsFgIXo

/*
 *  referred from: http://wptrafficanalyzer.in/blog/showing-nearby-places-and-place-details-using-google-places-api-and-google-maps-android-api-v2/
 *  we modify the code to consider only restaurants. Also, we have listview instead of google maps so that the user is comfortable to get the address
 *  
 */
import android.app.Dialog;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dpkch on 3/17/2017.
 */

public class SuggestRestaurantActivity extends AppCompatActivity implements LocationListener {
    // use this class for Weather Suggestions
    double latitude, longitude;
    String API_KEY = "AIzaSyAWAY4zfihHOlrdy9dN2JINy0fiSsFgIXo";
    ListView restaurantListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_restaurant);

        Bundle bundle = getIntent().getExtras();
        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("longitude");

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if(status != ConnectionResult.SUCCESS){ // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
            Log.d("No Google play service", "");
        }
        else    {
            // Google Play Services are available
//            // Getting reference to the SupportMapFragment
//            SupportMapFragment fragment = ( SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//            // Getting Google Map
//            mGoogleMap = fragment.getMap();
//            // Enabling MyLocation in Google Map
//            mGoogleMap.setMyLocationEnabled(true);
//            // Getting LocationManager object from System Service LOCATION_SERVICE
//            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//            // Creating a criteria object to retrieve provider
//            Criteria criteria = new Criteria();
//            // Getting the name of the best provider
//            String provider = locationManager.getBestProvider(criteria, true);
//            // Getting Current Location From GPS
//            Location location = locationManager.getLastKnownLocation(provider);
//            if(location!=null){
//                onLocationChanged(location);
//            }
//            locationManager.requestLocationUpdates(provider, 20000, 0, this);

//            mGoogleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
//
//                @Override
//                public void onInfoWindowClick(Marker arg0) {
//                    Intent intent = new Intent(getBaseContext(), PlaceDetailsActivity.class);
//                    String reference = mMarkerPlaceLink.get(arg0.getId());
//                    intent.putExtra("reference", reference);
//
//                    // Starting the Place Details Activity
//                    startActivity(intent);
//                }
//            });

            String type = "restaurant";
            StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb.append("location="+latitude+","+longitude);
            sb.append("&radius=2000");
            sb.append("&types="+type);
            sb.append("&sensor=true");
            sb.append("&key=AIzaSyAWAY4zfihHOlrdy9dN2JINy0fiSsFgIXo");

            PlacesTask placesTask = new PlacesTask();
            Log.d("In OnCreate", sb.toString());
            placesTask.execute(sb.toString());
        }

        restaurantListView = (ListView) findViewById(R.id.restaurantListView);

    }

    private class PlacesTask extends AsyncTask<String, Integer, String> {
        String data = null;
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
                Log.d("Data size", data.length()+"");
            }catch(Exception e){e.printStackTrace();}
            return data;
        }

        @Override
        protected void onPostExecute(String result){
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{
        JSONObject jsonObject;
        @Override
        protected List<HashMap<String,String>> doInBackground(String... jsonData) {
            List<HashMap<String, String>> places = null;
            RestaurantJSONParser restaurantJSONParser = new RestaurantJSONParser();
            try{
                jsonObject = new JSONObject(jsonData[0]);
                places = restaurantJSONParser.parse(jsonObject);
            }catch(Exception e){e.printStackTrace();}
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String,String>> list){
            //display here as ListView
            final List<String> restaurantName = new ArrayList<String>();
            final List<String> restaurantAddress = new ArrayList<String>();

            for(int i=0 ; i<list.size() ; i++)  {
                HashMap<String, String> hashMap = list.get(i);
                restaurantName.add(hashMap.get("place_name"));
                restaurantAddress.add(hashMap.get("vicinity"));
            }

            //referred from : https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(SuggestRestaurantActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1, restaurantName)   {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                    text1.setText(restaurantName.get(position));
                    text2.setText(restaurantAddress.get(position));
                    return view;
                }
            };
            restaurantListView.setAdapter(itemsAdapter);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
