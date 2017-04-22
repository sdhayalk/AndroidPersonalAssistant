package com.example.sahil.androidpersonalassistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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

import java.io.File;
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

    Handler mHandler=new Handler();
    int clothing, footwear, accessories, books, movies, music, electronics, software;
    final ArrayList<String> list_pref = new ArrayList<String>();
    String username, password;
    SQLiteDatabase db;
    public String TABLE = "Preferences";
    public  StringBuilder DATABASE_NAME=new StringBuilder("");
    public  final String FILE_PATH_DB = Environment.getExternalStorageDirectory() + File.separator + "Mydata";
    public  StringBuilder DATABASE_LOCATION = new StringBuilder(FILE_PATH_DB + File.separator) ;

    @Override
    public void taskCompleted(String stringResponse, JSONObject jsonResponse)    {
        runOnCompletion(stringResponse, jsonResponse);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_offer);
        Bundle bundle = getIntent().getExtras();
        try {
            username = bundle.getString("username");
            password = bundle.getString("password");
        }catch(Exception e) {
            Toast.makeText(this, "Cannot get username and password", Toast.LENGTH_SHORT).show();
        }
        DATABASE_NAME.append(username);
        DATABASE_LOCATION.append(DATABASE_NAME.toString());
        offersListView = (ListView) findViewById(R.id.offersListView);
        createListArray();

        Log.d("list pref", list_pref.size()+"");
        StringBuilder sb = new StringBuilder();
        for(int k=0 ; k<list_pref.size()-1 ; k++) {
            sb.append(list_pref.get(k));
            sb.append("&");
        }
        sb.append(list_pref.get(list_pref.size()-1));

//        String query = String.format("%s&%s", "clothing", "shoes");
        String query = sb.toString();
        Log.d("query = ", query);
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

    public void createListArray() {
        try {
            try {
                File folder = new File(FILE_PATH_DB);
                if (!folder.exists())
                    folder.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }

            File folder = new File(FILE_PATH_DB);
            if (!folder.exists()) {
                folder.mkdir();
            }

            db = SQLiteDatabase.openOrCreateDatabase(DATABASE_LOCATION.toString(), null);
            //perform your database operations here ...
            db.beginTransaction();
            Cursor c = db.rawQuery("select *  from tbl_" + TABLE, null);
            if (c.moveToFirst()) {
                do {
                    clothing = c.getInt(0);
                    footwear = c.getInt(1);
                    accessories = c.getInt(2);
                    books = c.getInt(3);
                    movies = c.getInt(4);
                    music = c.getInt(5);
                    electronics = c.getInt(6);
                    software = c.getInt(7);
                } while (c.moveToNext());
            }
            c.close();
            db.setTransactionSuccessful(); //commit your changes
            int i = 0;
            if (clothing == 1)
                list_pref.add("clothing");

            if (footwear == 1)
                list_pref.add("footwear");

            if (accessories == 1)
                list_pref.add("accessories");

            if (books == 1)
                list_pref.add("books");

            if (movies == 1)
                list_pref.add("movies");

            if (music == 1)
                list_pref.add("music");

            if (electronics == 1)
                list_pref.add("electronics");

            if (software == 1)
                list_pref.add("software");
        } catch (SQLiteException e) { // can't toast from a service
            final SQLiteException ee = e;
            mHandler.post(new Runnable() {
                public void run() {
                    Toast.makeText(SuggestOfferActivity.this, "ERROR DB select" + ee.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}
