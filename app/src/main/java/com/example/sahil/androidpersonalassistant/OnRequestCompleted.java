package com.example.sahil.androidpersonalassistant;

import org.json.JSONObject;

/**
 * Created by ravichandran on 21/04/17.
 */

public interface OnRequestCompleted {
    public void taskCompleted(String stringResponse, JSONObject jsonResponse);
}
