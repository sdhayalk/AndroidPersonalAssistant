package com.example.sahil.androidpersonalassistant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String username, password;
    Button signOutButton;
    Button sendNotification;
    Button buttonWeather,buttonOffer,buttonRestaurant, buttonPreferences;
    TextView weatherDataTextView;
    BroadcastReceiver broadcastReceiver;
    String mainMsg, sideMsg;
    LocationManager locationManager;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(runtime_permissions())  {}

        geocoder = new Geocoder(this, Locale.getDefault());

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        password = bundle.getString("password");
        sharedPreferences = getSharedPreferences("PreferencesToCheckIfLoggedIn", Context.MODE_PRIVATE);
        mainMsg = "Main Message";
        sideMsg = "Side Message";

        signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("username");
                editor.remove("password");
                editor.remove("keepLoggedIn");
                editor.commit();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("mainMsg", mainMsg);
                bundle.putString("sideMsg", sideMsg);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        buttonPreferences = (Button) findViewById(R.id.preferencesButton);
        buttonPreferences.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserPreferencesActivity.class);
                startActivity(intent);
            }
        });
        buttonOffer = (Button) findViewById(R.id.suggestOfferButton);
        buttonOffer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SuggestOfferActivity.class);
                startActivity(intent);
            }
        });
        buttonRestaurant = (Button) findViewById(R.id.suggestRestaurantButton);
        buttonRestaurant.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SuggestRestaurantActivity.class);
                startActivity(intent);
            }
        });
        buttonWeather = (Button) findViewById(R.id.suggestWeatherButton);
        buttonWeather.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SuggestWeatherActivity.class);
                startActivity(intent);
            }
        });

        sendNotification = (Button) findViewById(R.id.sendNotificationButton);
        sendNotification.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("mainMsg", mainMsg);
                bundle.putString("sideMsg", sideMsg);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //get city data:
//        geocoder = new Geocoder(this, Locale.getDefault());
//        LocationData locationData = new LocationData(geocoder, 33.4255, -111.9400);
//        locationData.getCity();
//        Toast.makeText(this, locationData.getCity(), Toast.LENGTH_SHORT).show();

        //get weather data:
        Intent getWeatherDataIntent = new Intent(getApplicationContext(), LocationData.class);
        startService(getWeatherDataIntent);

    }

    //referred from: https://www.youtube.com/watch?v=lvcGh2ZgHeA
    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null)   {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    double latitude = (double) intent.getExtras().get("latitude");
                    double longitude = (double) intent.getExtras().get("longitude");

                    //get city data:
                    GetWeatherData getWeatherData = new GetWeatherData(geocoder, latitude, latitude);

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
}