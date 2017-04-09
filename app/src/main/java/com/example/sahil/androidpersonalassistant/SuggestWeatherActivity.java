package com.example.sahil.androidpersonalassistant;
/*
 * referred from : https://www.youtube.com/watch?v=FkT1kwtYSFU
 * referred from : https://www.youtube.com/watch?v=dUKJN_KCK6U
 * referred from : https://www.youtube.com/watch?v=gJ9Ny_J3tcM
 *
 */


//Client ID (Consumer Key)
//dj0yJmk9OVhKRnBNSzVOUHlSJmQ9WVdrOVNERXhhWGc1TjJzbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD0zMA--
//
//Client Secret (Consumer Secret)
//ba9dbcf0825b6b4ab154b6feb19442e8591a83ef

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by SAHIL on 4/08/2017.
 */

public class SuggestWeatherActivity  extends AppCompatActivity {
    double latitude, longitude;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_weather);


    }
}
