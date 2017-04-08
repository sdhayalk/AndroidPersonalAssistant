package com.example.sahil.androidpersonalassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dpkch on 3/17/2017.
 */

public class SuggestRestaurantActivity extends AppCompatActivity {
    // use this class for Weather Suggestions
    String latitude, longitude;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_restaurant);

        Bundle bundle = getIntent().getExtras();
        latitude = bundle.getString("latitude");
        longitude = bundle.getString("longitude");
    }
}
