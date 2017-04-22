package com.example.sahil.androidpersonalassistant;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


//import org.json.JSONObject;
//import cz.msebera.android.httpclient.Header;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.NOTIFICATION_SERVICE;


public class GetSuggestions implements OnRequestCompleted{

    SharedPreferences sharedPreferences;
    String username, password;
    boolean keepLoggedIn;
    EditText usernameEditText, passwordEditText;
    Button continueButton;
    Button registerButton;
    CheckBox rememberMe;

    ProgressDialog prgDialog;
    InvokeServerAPI invokeServerAPI;
    OnRequestCompleted onRequestCompleted;

    static final String AWS_API_TAG =  "get_suggestions";
    static final String LOG_TAG = "GET_SUGGESTIONS";

    @Override
    public void taskCompleted(String stringResponse, JSONObject jsonResponse)
    {
        runOnCompletion(stringResponse, jsonResponse);
    }

    protected void getSuggestions(Context context, String stringParams) {
        InvokeServerAPI invokeServerAPI = new InvokeServerAPI(context, this);
        RequestParams requestParams = new RequestParams();
        invokeServerAPI.executeRequest(stringParams,requestParams,AWS_API_TAG);
    }


    void runOnCompletion(String stringResponse, JSONObject jsonResponse)
    {
        try {
            Log.d("ABCD", "YOYOYOY");
            boolean request_success = Boolean.valueOf(Utility.jsonRecurseKeys(jsonResponse,"success"));
            JSONObject data = (JSONObject) jsonResponse.get("data");
            JSONArray offers = data.getJSONArray("offers");
            JSONArray sale = data.getJSONArray("sale");
            String[] offerresults = toStringArray(offers);
            String[] saleresults = toStringArray(sale);
            if ( request_success )
            {
                Log.d("ABCD", offerresults[5]);
            }
            else
            {
//                Log.d("Error Message", request_message);
            }

        } catch (JSONException e) {
            Log.i(LOG_TAG + " | Exp ", e.toString());
        }

    }

    public static String[] toStringArray(JSONArray array) {
        if(array==null)
            return null;

        String[] arr=new String[array.length()];
        for(int i=0; i<arr.length; i++) {
            arr[i]=array.optString(i);
        }
        return arr;
    }

}
