package com.example.sahil.androidpersonalassistant.WeatherService;

/*
 * referred from: https://www.youtube.com/watch?v=FkT1kwtYSFU&t=955s
 */

import android.net.Uri;
import android.os.AsyncTask;

import com.example.sahil.androidpersonalassistant.WeatherData.Channel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by SAHIL on 08-04-2017.
 */

public class WeatherServiceUsingYahoo {
    WeatherServiceCallback weatherServiceCallback;
    private String location;
    Exception exception;

    public WeatherServiceUsingYahoo(WeatherServiceCallback weatherServiceCallback) {
        this.weatherServiceCallback = weatherServiceCallback;
    }


    public void refreshWeather(String l) {
        this.location = l;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")", strings[0]);
                String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));

                try {
                    URL url = new URL(endpoint);
                    URLConnection urlConnection = url.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while((line = bufferedReader.readLine()) != null)   {
                        sb.append(line);
                    }

                    return sb.toString();

                } catch (Exception e) {exception = e;}
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if (s == null && exception != null) {
                    weatherServiceCallback.unsuccessfullService(exception);
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject queryResults = jsonObject.optJSONObject("query");
                    int count = queryResults.optInt("count");
                    if(count == 0)  {
                        weatherServiceCallback.unsuccessfullService(new LocationWeatherException("Error, no such city = " + location));
                        return;
                    }

                    Channel channel = new Channel();
                    channel.populate(queryResults.optJSONObject("results").optJSONObject("channel"));
                    weatherServiceCallback.successfullService(channel);
                } catch (JSONException e) {
                    weatherServiceCallback.unsuccessfullService(e);
                    e.printStackTrace();
                }
            }
        }.execute(location);
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public class LocationWeatherException extends Exception {
        public LocationWeatherException(String detailMessage) {
            super(detailMessage);
        }
    }
}
