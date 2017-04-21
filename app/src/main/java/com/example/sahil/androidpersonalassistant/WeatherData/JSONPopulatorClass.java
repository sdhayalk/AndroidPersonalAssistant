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

public interface JSONPopulatorClass {
    void populate(JSONObject jsonObject);
    void populateArray(JSONArray jsonArray);
}
