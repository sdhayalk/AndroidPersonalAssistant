package com.example.sahil.androidpersonalassistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dpkch on 3/17/2017.
 */

public class SuggestOfferActivity extends AppCompatActivity implements OnRequestCompleted{
    // use this class for Weather Suggestions
    String shareMessage;
    Button shareButton;
    ListView offersListView;
    SharedPreferences sharedPreferences;
    String username, password;
    boolean keepLoggedIn;
    EditText usernameEditText, passwordEditText;
    Button continueButton;
    Button registerButton;
    CheckBox rememberMe;

    public String[] offerresults;
    public String[] saleresults;

    ProgressDialog prgDialog;
    InvokeServerAPI invokeServerAPI;
    OnRequestCompleted onRequestCompleted;

    static final String AWS_API_TAG =  "get_suggestions";
    static final String LOG_TAG = "GET_SUGGESTIONS";

    @Override
    public void taskCompleted(String stringResponse, JSONObject jsonResponse)    {
        runOnCompletion(stringResponse, jsonResponse);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_offer);

        offersListView = (ListView) findViewById(R.id.offersListView);

        String query = String.format("%s&%s", "clothing", "shoes");
        getSuggestions(SuggestOfferActivity.this, query);

    }

    private class StableArrayAdapter extends ArrayAdapter<String> {
        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }
        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

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
            boolean request_success = jsonResponse.getBoolean("success");
            String request_message = jsonResponse.getString("message");
            JSONObject data = (JSONObject) jsonResponse.get("data");
            JSONArray offers = data.getJSONArray("offers");
            JSONArray sale = data.getJSONArray("sale");
            offerresults = toStringArray(offers);
            saleresults = toStringArray(sale);
            if ( true )
            {
                Log.d("ABCD", offerresults[5]);
                final String[] values = offerresults;

                final ArrayList<String> list = new ArrayList<String>();
                for (int i = 0; i < values.length; ++i) {
                    list.add(values[i]);
                }

                offersListView = (ListView) findViewById(R.id.offersListView);
                final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list);
                offersListView.setAdapter(adapter);

                offersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, final View view,int position, long id) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT, "Offer: " + values[position]);
                        startActivity(Intent.createChooser(share, "Title"));
                    }
                });

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

    public String[] returnResult()   {
        return offerresults;
    }
}
