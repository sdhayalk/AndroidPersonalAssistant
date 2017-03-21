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
import android.widget.Toast;

public class UserPreferencesActivity extends AppCompatActivity implements LocationListener{
    String username, password;
    Button currentLocationButton;
    LocationManager locationManager;
    EditText addressEditText;
    Button confirmPreferencesButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preferences);

        Bundle bundle = getIntent().getExtras();
        try {
            username = bundle.getString("username");
            password = bundle.getString("password");
        }catch(Exception e) {
            Toast.makeText(this, "Cannot get username and password", Toast.LENGTH_SHORT).show();
        }

        currentLocationButton = (Button) findViewById(R.id.currentLocationButton);
        currentLocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //get current address
                //referred from: http://javapapers.com/android/get-current-location-in-android/

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
