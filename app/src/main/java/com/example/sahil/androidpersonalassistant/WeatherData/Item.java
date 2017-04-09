package com.example.sahil.androidpersonalassistant.WeatherData;

/*
 * referred from : https://www.youtube.com/watch?v=FkT1kwtYSFU
 * referred from : https://www.youtube.com/watch?v=dUKJN_KCK6U
 * referred from : https://www.youtube.com/watch?v=gJ9Ny_J3tcM
 *
 */
import org.json.JSONObject;

/**
 * Created by SAHIL on 08-04-2017.
 */

public class Item implements JSONPopulatorClass {
    Condition condition;

    @Override
    public void populate(JSONObject jsonObject) {
        condition = new Condition();
        condition.populate(jsonObject.optJSONObject("condition"));
    }

    public Condition getCondition() {
        return condition;
    }
}
