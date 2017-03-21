package com.example.sahil.androidpersonalassistant;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.util.List;
import java.util.Locale;

public class LocationData {
    Geocoder geocoder;
    double latitude, longitude;
    String city;
    Context context;

    LocationData(Geocoder geocoder, double latitude, double longitude)  {
        this.geocoder = geocoder;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCity()    {
        //referred from: http://stackoverflow.com/questions/2296377/how-to-get-city-name-from-latitude-and-longitude-coordinates-in-google-maps
        try {
            //Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0)
                city = addresses.get(0).getLocality();
        }catch (Exception e)    {
            e.printStackTrace();
        }
        return city;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
