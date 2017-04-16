package com.example.sahil.androidpersonalassistant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    boolean loadedFlag;
    SharedPreferences sharedPreferences;
    String username, password;
    Button signOutButton;
    Button sendNotification;
    ListView listview;
    TextView loadingTextView;
    TextView weatherDataTextView;
    BroadcastReceiver broadcastReceiver;
    String mainMsg, sideMsg;
    LocationManager locationManager;
    Geocoder geocoder;
    double latitude, longitude;
    String currentCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadedFlag = false;
        if(runtime_permissions())  {}
        listview = (ListView) findViewById(R.id.listview);
        loadingTextView = (TextView) findViewById(R.id.loadingTextView);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        password = bundle.getString("password");

        /* start service to add the personalization data to the DB */
        Intent serviceIntent = new Intent(MainActivity.this, CollectData.class);
        serviceIntent.putExtras(bundle);
        startService(serviceIntent);
        /*****/

        //disableAll();


        Intent locationDataIntent = new Intent(getApplicationContext(), LocationData.class);
        startService(locationDataIntent);

        geocoder = new Geocoder(this, Locale.getDefault());


        sharedPreferences = getSharedPreferences("PreferencesToCheckIfLoggedIn", Context.MODE_PRIVATE);
        mainMsg = "Main Message";
        sideMsg = "Side Message";


        /***********************LIST VIEW******************/

        String[] values = new String[] { "OFFERS","RESTAURANTS","WEATHER","USER PREFERENCES","SEND NOTIFICATION"};

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View view,int position, long id) {
                if(position==0){
                    Intent intent = new Intent(MainActivity.this, SuggestOfferActivity.class);
                    startActivity(intent);
                }
                else if(position==1){
                    Intent intent = new Intent(MainActivity.this, SuggestRestaurantActivity.class);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    startActivity(intent);
                }
                else if(position==2){
                    Intent intent = new Intent(MainActivity.this, SuggestWeatherActivity.class);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    startActivity(intent);
                }
                else if(position==3){
                    Intent intent = new Intent(MainActivity.this, UserPreferencesActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    bundle.putString("password", password);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else if(position==4){
                    Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("mainMsg", mainMsg);
                    bundle.putString("sideMsg", sideMsg);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        //while(!listview.isEnabled())    {}
        Toast.makeText(this, latitude+"", Toast.LENGTH_SHORT).show();

    }

    //referred from: https://www.youtube.com/watch?v=lvcGh2ZgHeA
    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null)   {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    latitude = (double) intent.getExtras().get("latitude");
                    longitude = (double) intent.getExtras().get("longitude");
//                    Log.d("latitude",latitude+"");
                    enableAll();
                    //get city data:
//                    GetWeatherData getWeatherData = new GetWeatherData(geocoder, latitude, latitude);
//                    currentCity = getWeatherData.getCity();

                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("LocationUpdate"));
    }

    //referred from: https://www.youtube.com/watch?v=lvcGh2ZgHeA
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
    }

    public void disableAll()  {
        listview.setEnabled(false);
    }

    public void enableAll() {
        listview.setEnabled(true);
        loadingTextView.setEnabled(false);
    }

    //referred from: https://www.youtube.com/watch?v=lvcGh2ZgHeA
    boolean runtime_permissions()   {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)   {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return true;
        }
        return false;
    }

    //referred from: https://www.youtube.com/watch?v=lvcGh2ZgHeA
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)    {

            }
            else    {
                runtime_permissions();
            }
        }
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {
        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }
        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}