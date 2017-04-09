package com.example.sahil.androidpersonalassistant;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by SAHIL on 07-04-2017.
 */

/*
 * referred from : https://github.com/googlemaps/android-samples/blob/master/ApiDemos/app/src/main/java/com/example/mapdemo/MyLocationDemoActivity.java
 * referred from : http://wptrafficanalyzer.in/blog/adding-marker-at-user-input-latitude-and-longitude-in-google-map-android-api-v2/
 *
 * Using the above references, we modity the code to include two markers on the map, one corresponding
 * to user's current position and the other corresponding to the selected restaurant's position
 *
 */

public class ShowOnMap extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private boolean mPermissionDenied = false;
    private GoogleMap mMap;
    double latitude, longitude, resLatitude, resLongitude;
    LatLng currentLatLng, resLatLng;
    MarkerOptions currentOptions, resOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle bundle = getIntent().getExtras();
        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("longitude");
        resLatitude = bundle.getDouble("resLatitude");
        resLongitude = bundle.getDouble("resLongitude");

        currentLatLng = new LatLng(latitude, longitude);
        resLatLng = new LatLng(resLatitude, resLongitude);

        currentOptions = new MarkerOptions();
        currentOptions.position(currentLatLng);
        resOptions = new MarkerOptions();
        resOptions.position(resLatLng);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        //mMap.setOnMyLocationButtonClickListener(this);
        mMap.addMarker(currentOptions);
        mMap.addMarker(resOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13.5f));
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        enableMyLocation();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            mPermissionDenied = false;
        }
    }
}
