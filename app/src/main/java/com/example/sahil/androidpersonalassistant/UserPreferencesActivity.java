package com.example.sahil.androidpersonalassistant;

import android.content.Context;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserPreferencesActivity extends AppCompatActivity implements LocationListener{
    String username, password;
    Button currentLocationButton;
    LocationManager locationManager;
    EditText addressEditText;
    Button confirmPreferencesButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        password = bundle.getString("password");

        currentLocationButton = (Button) findViewById(R.id.signOutButton);
        currentLocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //get current address
                //referred from: http://javapapers.com/android/get-current-location-in-android/
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        });

        addressEditText = (EditText) findViewById(R.id.addressEditText);

        confirmPreferencesButton = (Button) findViewById(R.id.confirmPreferencesButton);
        confirmPreferencesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //save preferences in DB
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
