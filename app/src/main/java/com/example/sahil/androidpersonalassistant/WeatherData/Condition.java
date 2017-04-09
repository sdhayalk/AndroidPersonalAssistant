package com.example.sahil.androidpersonalassistant.WeatherData;

/*
 * referred from: https://www.youtube.com/watch?v=FkT1kwtYSFU&t=955s
 */

import org.json.JSONObject;

/**
 * Created by SAHIL on 08-04-2017.
 */

public class Condition implements JSONPopulatorClass {
    int temperature, code;
    String description;

    @Override
    public void populate(JSONObject jsonObject) {
        temperature = jsonObject.optInt("temp");
        code = jsonObject.optInt("code");
        description = jsonObject.optString("text");
    }

    public int getTemperature() {
        return temperature;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
