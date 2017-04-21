package com.example.sahil.androidpersonalassistant;

/**
 * Created by ravichandran on 20/04/17.
 */

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;



public class InvokeServerAPI {


    static final String LOG_TAG = "INVOKE_SERVER_API";

    Context context;

    OnRequestCompleted requestListener;

    AsyncHttpClient asyncHttpClient;
    RequestParams requestParams;

    String stringResponse;
    JSONObject jsonResponse;

    static final String AWS_EC2_URL = "http://ec2-52-36-128-208.us-west-2.compute.amazonaws.com";
    static final String DEFAULT_URL = AWS_EC2_URL + "/users/login?user_name=dude_1&password=dude123";
    static final String USER_LOGIN_URL = AWS_EC2_URL + "/users/login?";
    static final String USER_REGISTERATION_URL = AWS_EC2_URL + "/users/registration?";




    public InvokeServerAPI() {
        asyncHttpClient = new AsyncHttpClient();
//        requestParams = new RequestParams();
    }

    public InvokeServerAPI(Context context, OnRequestCompleted listener) {
        asyncHttpClient = new AsyncHttpClient();
        this.context = context;
        this.requestListener = listener;
    }

    public String requestURLHandler(String requestTag)
    {
        switch(requestTag) {
            case "users_login" :
                return USER_LOGIN_URL;
            case "users_register" :
                return USER_REGISTERATION_URL;
            default :
                return DEFAULT_URL;
        }

    }

    public void executeRequest(String stringParams,RequestParams currentParams, final String requestTag) {

        requestParams = currentParams;

//        Log.i(LOG_TAG + " | " + requestTag, "executeRequest Called");

        String REQUEST_URL = requestURLHandler(requestTag);
        REQUEST_URL += stringParams;

        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(REQUEST_URL, currentParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

//                Log.i(LOG_TAG + " | " + requestTag, "entered On Success");

                try {

                    jsonResponse = new JSONObject(new String(responseBody));

                    stringResponse = jsonResponse.toString();

//                    Log.i(LOG_TAG + " | " + requestTag, "onSuccess: stringResponse = " + stringResponse);

                    requestListener.taskCompleted(stringResponse, jsonResponse);



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {

//                Log.i(LOG_TAG + " | " + requestTag, "entered OnFailure");

                try {

                    jsonResponse = new JSONObject();

                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Cannot Reach AWS EC2 Server | Status Code = "+statusCode);
                    jsonResponse.put("exception", true);

                    stringResponse = jsonResponse.toString();

                    requestListener.taskCompleted(stringResponse, jsonResponse);

//                    Log.i(LOG_TAG + " | " + requestTag, "onFailure: stringResponse = " + stringResponse);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }


}