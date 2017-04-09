package com.example.sahil.androidpersonalassistant.WeatherData;

/*
 * referred from: https://www.youtube.com/watch?v=FkT1kwtYSFU&t=955s
 */

import org.json.JSONObject;

/**
 * Created by SAHIL on 08-04-2017.
 */

public class Units implements JSONPopulatorClass {
    String temperature;

    @Override
    public void populate(JSONObject jsonObject) {
        temperature = jsonObject.optString("temperature");
    }

    public String getTemperature() {
        return temperature;
    }
}
