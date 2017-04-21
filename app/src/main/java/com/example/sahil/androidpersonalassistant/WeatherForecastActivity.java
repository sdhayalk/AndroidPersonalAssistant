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
import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
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

public class WeatherForecastActivity  extends AppCompatActivity implements WeatherServiceCallback {
    String city;
    double latitude, longitude;
    TextView temperatureHighTextView, temperatureLowTextView, textTextView;
    WeatherServiceUsingYahoo weatherServiceUsingYahoo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_weather);

//        Bundle bundle = getIntent().getExtras();
//        city = bundle.getString("city");

        Bundle bundle = getIntent().getExtras();
        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("longitude");
        city = bundle.getString("city");

        temperatureHighTextView = (TextView) findViewById(R.id.temperatureHighTextView);
        temperatureLowTextView = (TextView) findViewById(R.id.temperatureLowTextView);
        textTextView = (TextView) findViewById(R.id.textTextView);

        if(city!=null)  {
            weatherServiceUsingYahoo = new WeatherServiceUsingYahoo(WeatherForecastActivity.this);
            weatherServiceUsingYahoo.refreshWeather(city);
        }
        else {
            AsyncTaskWeather asyncTaskRunner = new AsyncTaskWeather();
            asyncTaskRunner.execute();
        }


    }

    @Override
    public void successfullService(Channel channel) {
        Item item = channel.getItem();
        temperatureHighTextView.setText(item.getForecast().getHigh()+"");
        temperatureLowTextView.setText(item.getForecast().getLow()+"");
        textTextView.setText(item.getForecast().getText());
    }

    @Override
    public void unsuccessfullService(Exception exception) {
        exception.printStackTrace();
    }

    public class AsyncTaskWeather extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(WeatherForecastActivity.this, "Please wait", "Fetching weather forecast");
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                //referred from: http://stackoverflow.com/questions/8119369/how-can-i-get-the-current-city-name-in-android
                Geocoder gcd = new Geocoder(WeatherForecastActivity.this, Locale.getDefault());
                List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
                if (addresses.size() > 0)
                    city = addresses.get(0).getLocality();
                Log.d("latitude: ", latitude+"");
            } catch (Exception e)   {e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            weatherServiceUsingYahoo = new WeatherServiceUsingYahoo(WeatherForecastActivity.this);
            weatherServiceUsingYahoo.refreshWeather(city);
        }
    }
}
