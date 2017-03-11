package com.example.sahil.androidpersonalassistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserPreferencesActivity extends AppCompatActivity{
    String username, password;
    Button currentLocationButton;
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
            }
        });

        addressEditText = (EditText) findViewById(R.id.addressEditText);

        confirmPreferencesButton = (Button) findViewById(R.id.confirmPreferencesButton);
    }
}
