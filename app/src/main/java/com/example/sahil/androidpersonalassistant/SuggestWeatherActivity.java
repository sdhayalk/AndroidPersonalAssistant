package com.example.sahil.androidpersonalassistant;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by dpkch on 3/17/2017.
 */

public class SuggestWeatherActivity  extends AppCompatActivity {
    double latitude, longitude;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_weather);


    }
}
