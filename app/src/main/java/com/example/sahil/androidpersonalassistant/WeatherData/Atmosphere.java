package com.example.sahil.androidpersonalassistant.WeatherData;

import org.json.JSONObject;

/**
 * Created by SAHIL on 09-04-2017.
 */

public class Atmosphere implements JSONPopulatorClass {
    String humidity, pressure, rising, visibility;

    @Override
    public void populate(JSONObject jsonObject) {
        humidity = jsonObject.optString("humidity");
        pressure = jsonObject.optString("pressure");
        rising = jsonObject.optString("rising");
        visibility = jsonObject.optString("visibility");
    }

    public String getHumidity() {
        return humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public String getRising() {
        return rising;
    }

    public String getVisibility() {
        return visibility;
    }
}
