package com.example.sahil.androidpersonalassistant;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

public class LocationData extends Service{
    LocationManager locationManager;
    LocationListener locationListener;
    Geocoder geocoder;
    String locationManagerProvider;
    String currentLocationWeather[];
    String locationWeather[];

    @Override
    public void onCreate() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent locationUpdateIntent = new Intent("LocationUpdate");
                locationUpdateIntent.putExtra("latitude", location.getLatitude());
                locationUpdateIntent.putExtra("longitude", location.getLongitude());
                sendBroadcast(locationUpdateIntent);
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}
            @Override
            public void onProviderEnabled(String s) {}
            @Override
            public void onProviderDisabled(String s) {
                Intent getSettingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getSettingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(getSettingsIntent);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500000, 0, locationListener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null)
            locationManager.removeUpdates(locationListener);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
