package com.example.sahil.androidpersonalassistant.WeatherService;

/*
 * referred from: https://www.youtube.com/watch?v=FkT1kwtYSFU&t=955s
 */

import android.os.AsyncTask;

/**
 * Created by SAHIL on 08-04-2017.
 */

public class WeatherServiceUsingYahoo {
    WeatherServiceCallback weatherServiceCallback;
    String location;

    public WeatherServiceUsingYahoo(WeatherServiceCallback weatherServiceCallback) {
        this.weatherServiceCallback = weatherServiceCallback;
    }


    public void refreshWeather(String location) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }.execute(location);
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
}
