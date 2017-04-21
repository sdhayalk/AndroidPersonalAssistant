package com.example.sahil.androidpersonalassistant.WeatherData;

/*
 * referred from : https://www.youtube.com/watch?v=FkT1kwtYSFU
 * referred from : https://www.youtube.com/watch?v=dUKJN_KCK6U
 * referred from : https://www.youtube.com/watch?v=gJ9Ny_J3tcM
 *
 */
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by SAHIL on 08-04-2017.
 */

public class Item implements JSONPopulatorClass {
    Condition condition;
    Forecast forecast;

    @Override
    public void populate(JSONObject jsonObject) {
        condition = new Condition();
        condition.populate(jsonObject.optJSONObject("condition"));
        forecast = new Forecast();
        forecast.populateArray(jsonObject.optJSONArray("forecast"));
    }

    @Override
    public void populateArray(JSONArray jsonArray) {

    }

    public Condition getCondition() {
        return condition;
    }

    public Forecast getForecast() {
        return forecast;
    }
}
