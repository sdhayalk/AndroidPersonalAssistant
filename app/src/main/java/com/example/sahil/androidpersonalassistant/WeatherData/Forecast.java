package com.example.sahil.androidpersonalassistant.WeatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SAHIL on 21-04-2017.
 */

public class Forecast implements JSONPopulatorClass {
    String high, low, text;

    @Override
    public void populate(JSONObject jsonObject) {

    }

    @Override
    public void populateArray(JSONArray jsonArray) {
        high = jsonArray.optJSONObject(0).optString("high");
        low = jsonArray.optJSONObject(0).optString("low");
        text = jsonArray.optJSONObject(0).optString("text");
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getText() {
        return text;
    }
}
