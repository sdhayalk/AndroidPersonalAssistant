package com.example.sahil.androidpersonalassistant;
/*
 * Referred from: http://wptrafficanalyzer.in/blog/showing-nearby-places-and-place-details-using-google-places-api-and-google-maps-android-api-v2/
 * We will use only restuarants from the various list of places given by Google Places API
 */


/**
 * Created by SAHIL on 07-04-2017.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RestaurantJSONParser {
    public List<HashMap<String,String>> parse(JSONObject jsonObject){
        JSONArray jsonArrayPlaces = null;
        try {
            jsonArrayPlaces = jsonObject.getJSONArray("results");
        } catch (JSONException e) {e.printStackTrace();}
        return getPlaces(jsonArrayPlaces);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArrayPlaces){
        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> place = null;

        for(int i=0 ; i<jsonArrayPlaces.length() ; i++){
            try {
                place = getPlace((JSONObject)jsonArrayPlaces.get(i));
                placesList.add(place);
            } catch (JSONException e) {e.printStackTrace();}
        }
        return placesList;
    }

    private HashMap<String, String> getPlace(JSONObject jPlace){

        HashMap<String, String> place = new HashMap<String, String>();
        String placeName = "";
        String vicinity = "";
        String latitude = "";
        String longitude = "";
        String reference = "";

        try {
            if(!jPlace.isNull("name"))
                placeName = jPlace.getString("name");
            if(!jPlace.isNull("vicinity"))
                vicinity = jPlace.getString("vicinity");

            latitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = jPlace.getString("reference");

            place.put("place_name", placeName);
            place.put("vicinity", vicinity);
            place.put("lat", latitude);
            place.put("lng", longitude);
            place.put("reference", reference);

        } catch (JSONException e) {e.printStackTrace();}
        return place;
    }
}