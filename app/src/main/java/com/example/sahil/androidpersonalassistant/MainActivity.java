package com.example.sahil.androidpersonalassistant;

import android.*;
import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.Instance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import weka.classifiers.functions.LinearRegression;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class MainActivity extends AppCompatActivity {
    boolean loadedFlag;
    SharedPreferences sharedPreferences;
    String username, password;
    Button signOutButton; // TODO: sahil to implement
    Button sendNotification;
    Button creditsButton;
    ListView listview;
    TextView weatherDataTextView;
    ProgressDialog progressDialog;
    BroadcastReceiver broadcastReceiver;
    String mainMsg, sideMsg;
    LocationManager locationManager;
    Geocoder geocoder;
    double latitude, longitude;
    public static final String FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + "Mydata";   //was APA

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadedFlag = false;
        if(runtime_permissions())  {}
        listview = (ListView) findViewById(R.id.listview);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        password = bundle.getString("password");

        /* start service to add the personalization data to the DB */
//        Intent serviceIntent = new Intent(MainActivity.this, CollectData.class);
//        serviceIntent.putExtras(bundle);
//        startService(serviceIntent);
        /*****/

        //disableAll();


        progressDialog = ProgressDialog.show(MainActivity.this, "Fetching current location", "Please wait...");
        Intent locationDataIntent = new Intent(getApplicationContext(), LocationData.class);
        startService(locationDataIntent);

        geocoder = new Geocoder(this, Locale.getDefault());


        sharedPreferences = getSharedPreferences("PreferencesToCheckIfLoggedIn", Context.MODE_PRIVATE);
        mainMsg = "Main Message";
        sideMsg = "Side Message";


        /***********************LIST VIEW******************/

        String[] values = new String[] { "OFFERS","RESTAURANTS","WEATHER","USER PREFERENCES"};

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
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    bundle.putString("password", password);
                    intent.putExtras(bundle);
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
            }
        });


        //--------------------------STARTING PERSONALIZATION--------------------------//

        /*
         * referred from: http://www.sanfoundry.com/java-android-program-start-service-every-hour/
         */

        startService(new Intent(this, PersonalizationService.class));
        Calendar calendar = Calendar.getInstance();
        Intent intent = new Intent(this, PersonalizationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5*1000, pendingIntent);
                                                                                    // it should be 3600000 for every hour

        try {
            File folder = new File(FILE_PATH);
            if (!folder.exists())
                folder.mkdir();

            File file = new File(FILE_PATH + File.separator + "location.csv");
            if (!file.exists())
                file.createNewFile();
        } catch (Exception e)   {e.printStackTrace();}


        signOutButton =(Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(), PersonalizationService.class));
                sharedPreferences.edit().clear().commit();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        creditsButton = (Button) findViewById(R.id.creditsButton);
        creditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreditsActivity.class);
                startActivity(intent);
            }
        });

        //--------------------------ENDING PERSONALIZATION--------------------------//

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
                    enableAll();
                    progressDialog.dismiss();
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
    }

    //referred from: https://www.youtube.com/watch?v=lvcGh2ZgHeA
    boolean runtime_permissions()   {
        if(Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            return true;
        }
        return false;
    }

    //referred from: https://www.youtube.com/watch?v=lvcGh2ZgHeA
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED)    {

            }
            else    {
                runtime_permissions();
            }
        }
    }

    // referred from http://www.vogella.com/tutorials/AndroidListView/article.html
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