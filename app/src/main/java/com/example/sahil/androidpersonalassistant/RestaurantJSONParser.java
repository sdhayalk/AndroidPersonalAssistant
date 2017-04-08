package com.example.sahil.androidpersonalassistant;
/*
 * Referred from:
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
        JSONArray jsonPlaces = null;
        try {
            jsonPlaces = jsonObject.getJSONArray("results");
        } catch (JSONException e) {e.printStackTrace();}
        return getPlaces(jsonPlaces);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonPlaces){
        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> place = null;

        for(int i=0; i<jsonPlaces.length() ;i++){
            try {
                place = getPlace((JSONObject)jsonPlaces.get(i));
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