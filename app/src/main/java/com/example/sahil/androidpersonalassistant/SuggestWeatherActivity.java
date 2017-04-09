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
import android.widget.TextView;

import com.example.sahil.androidpersonalassistant.WeatherData.Channel;
import com.example.sahil.androidpersonalassistant.WeatherService.WeatherServiceCallback;
import com.example.sahil.androidpersonalassistant.WeatherService.WeatherServiceUsingYahoo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by SAHIL on 4/08/2017.
 */

public class SuggestWeatherActivity  extends AppCompatActivity implements WeatherServiceCallback {
    double latitude, longitude;
    TextView currentTemperatureTextView, currentConditionTextView, currentLocationTextView;
    WeatherServiceUsingYahoo weatherServiceUsingYahoo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_weather);

        currentTemperatureTextView = (TextView) findViewById(R.id.currentTemperatureTextView);
        currentConditionTextView = (TextView) findViewById(R.id.currentConditionTextView);
        currentLocationTextView = (TextView) findViewById(R.id.currentLocationButton);

        weatherServiceUsingYahoo = new WeatherServiceUsingYahoo(this);
        weatherServiceUsingYahoo.refreshWeather("Austin, TX");

    }

    @Override
    public void successfullService(Channel channel) {

    }

    @Override
    public void unsuccessfullService(Exception exception) {
        exception.printStackTrace();
    }
}
