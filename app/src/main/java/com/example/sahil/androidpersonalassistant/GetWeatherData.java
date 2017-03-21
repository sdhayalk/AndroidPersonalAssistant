package com.example.sahil.androidpersonalassistant;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class GetWeatherData {
    Geocoder geocoder;
    double latitude, longitude;
    List<Address> addresses;
    String city;

    GetWeatherData(Geocoder geocoder, double latitude, double longitude) {
        this.geocoder = geocoder;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCity() {
        //referred from: http://stackoverflow.com/questions/2296377/how-to-get-city-name-from-latitude-and-longitude-coordinates-in-google-maps
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {e.printStackTrace();        }
        if(addresses.size()>0) {
            city = addresses.get(0).getLocality();
        }
        else    {}
        return city;
    }



}
