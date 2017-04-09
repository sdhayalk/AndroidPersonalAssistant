package com.example.sahil.androidpersonalassistant.WeatherService;

/*
 * referred from: https://www.youtube.com/watch?v=FkT1kwtYSFU&t=955s
 */

import com.example.sahil.androidpersonalassistant.WeatherData.Channel;

/**
 * Created by SAHIL on 08-04-2017.
 */

public interface WeatherServiceCallback {
    void successfullService(Channel channel);
    void unsuccessfullService(Exception exception);
}
