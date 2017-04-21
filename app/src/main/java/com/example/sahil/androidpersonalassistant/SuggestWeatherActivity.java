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
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.sahil.androidpersonalassistant.WeatherData.Channel;
import com.example.sahil.androidpersonalassistant.WeatherData.Item;
import com.example.sahil.androidpersonalassistant.WeatherService.WeatherServiceCallback;
import com.example.sahil.androidpersonalassistant.WeatherService.WeatherServiceUsingYahoo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.List;
import java.util.Locale;

/**
 * Created by SAHIL on 4/08/2017.
 */

public class SuggestWeatherActivity  extends AppCompatActivity implements WeatherServiceCallback {
    double latitude, longitude;
    String currentLocation;
    TextView currentTemperatureTextView, currentConditionTextView, currentLocationTextView, currentHumidityTextView, currentPressureTextView, currentRisingTextView, currentVisibilityTextView;
    WeatherServiceUsingYahoo weatherServiceUsingYahoo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_weather);

        Bundle bundle = getIntent().getExtras();
        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("longitude");

        //referred from: http://stackoverflow.com/questions/8119369/how-can-i-get-the-current-city-name-in-android
        try {
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0)
                currentLocation = addresses.get(0).getLocality();
        } catch (Exception e)   {e.printStackTrace();}

        currentTemperatureTextView = (TextView) findViewById(R.id.currentTemperatureTextView);
        currentConditionTextView = (TextView) findViewById(R.id.currentConditionTextView);
        currentLocationTextView = (TextView) findViewById(R.id.currentLocationTextView);
        currentHumidityTextView = (TextView) findViewById(R.id.currentHumidityTextView);
        currentPressureTextView = (TextView) findViewById(R.id.currentPressureTextView);
        currentRisingTextView = (TextView) findViewById(R.id.currentRisingTextView);
        currentVisibilityTextView = (TextView) findViewById(R.id.currentVisibilityTextView);

        weatherServiceUsingYahoo = new WeatherServiceUsingYahoo(this);
        weatherServiceUsingYahoo.refreshWeather(currentLocation);

    }

    @Override
    public void successfullService(Channel channel) {
        Item item = channel.getItem();
        currentTemperatureTextView.setText(item.getCondition().getTemperature() + " " + channel.getUnits().getTemperature());
        currentConditionTextView.setText(item.getCondition().getDescription());
        currentLocationTextView.setText(currentLocation);
        currentHumidityTextView.setText(channel.getAtmosphere().getHumidity());
        currentPressureTextView.setText(channel.getAtmosphere().getPressure());
        currentRisingTextView.setText(channel.getAtmosphere().getRising());
        currentVisibilityTextView.setText(channel.getAtmosphere().getVisibility());
    }

    @Override
    public void unsuccessfullService(Exception exception) {
        exception.printStackTrace();
    }
}
