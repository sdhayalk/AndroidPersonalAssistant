package com.example.sahil.androidpersonalassistant;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dpkch on 3/17/2017.
 */

public class SuggestOfferActivity extends AppCompatActivity {
    // use this class for Weather Suggestions
    String shareMessage;
    Button shareButton;
    ListView offersListView;
    SQLiteDatabase db;
    Handler mHandler=new Handler();
    int clothing, footwear, accessories, books, movies, music, electronics, software;
    final ArrayList<String> list = new ArrayList<String>();
    String username, password;
    public String TABLE = "Preferences";
    public  StringBuilder DATABASE_NAME=new StringBuilder("");
    public  final String FILE_PATH_DB = Environment.getExternalStorageDirectory() + File.separator + "Mydata";
    public  StringBuilder DATABASE_LOCATION = new StringBuilder(FILE_PATH_DB + File.separator) ;


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
        GetSuggestions getSuggestions = new GetSuggestions();
        String query = String.format("%s&%s", "clothing", "shoes");
        getSuggestions.getSuggestions(SuggestOfferActivity.this, query);

        shareMessage = "Message";
        shareButton = (Button) findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(share, "Title"));
            }
        });

        OffersTask offersTask = new OffersTask();
        offersTask.execute();
    }

    private class OffersTask extends AsyncTask<String, Integer, List<HashMap<String,String>>> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
//            progressDialog = ProgressDialog.show(SuggestOfferActivity.this, "Finding Offers", "Please wait...");
        }

        @Override
        protected List<HashMap<String,String>> doInBackground(String... strings) {
            //get offers data from server:
            HashMap<String, String> offerHashMap = new HashMap<String, String>();
            List<HashMap<String, String>> offers = new ArrayList<>();

            offerHashMap.put("offerDescription", "Offer Description 1");
            offerHashMap.put("offerWebsite", "Offer Website 1");
            offers.add(offerHashMap);

            offerHashMap = new HashMap<String, String>();
            offerHashMap.put("offerDescription", "Offer Description 2");
            offerHashMap.put("offerWebsite", "Offer Website 2");
            offers.add(offerHashMap);

            offerHashMap = new HashMap<String, String>();
            offerHashMap.put("offerDescription", "Offer Description 3");
            offerHashMap.put("offerWebsite", "Offer Website 3");
            offers.add(offerHashMap);

            return offers;
        }

        @Override
        protected void onPostExecute(List<HashMap<String,String>> list) {
            //display here as ListView
            final List<String> offerDescription = new ArrayList<String>();
            final List<String> offerWebsite = new ArrayList<String>();

            for(int i=0 ; i<list.size() ; i++)  {
                HashMap<String, String> hashMap = list.get(i);
                offerDescription.add(hashMap.get("offerDescription"));
                offerWebsite.add(hashMap.get("offerWebsite"));
            }

            //referred from : https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(SuggestOfferActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1, offerDescription)   {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                    text1.setText(offerDescription.get(position));
                    text2.setText(offerWebsite.get(position));
                    return view;
                }
            };

            offersListView.setAdapter(itemsAdapter);
        }
    }

    public void createListArray() {
        try {
            try {
                File folder = new File(FILE_PATH_DB);
                if (!folder.exists())
                    folder.mkdir();
            } catch (Exception e)   {e.printStackTrace();}

            File folder = new File(FILE_PATH_DB);
            if (!folder.exists()) {
                folder.mkdir();
            }

            db = SQLiteDatabase.openOrCreateDatabase(DATABASE_LOCATION.toString(), null);
            //perform your database operations here ...
            db.beginTransaction();
            Cursor c = db.rawQuery("select *  from tbl_"+ TABLE , null);
            if(c.moveToFirst()){
                do{
                    clothing=c.getInt(0);
                    footwear=c.getInt(1);
                    accessories=c.getInt(2);
                    books=c.getInt(3);
                    movies=c.getInt(4);
                    music=c.getInt(5);
                    electronics=c.getInt(6);
                    software=c.getInt(7);
                }while(c.moveToNext());
            }
            c.close();
            db.setTransactionSuccessful(); //commit your changes
            int i=0;
            if(clothing==1)
                list.add("clothing");

            if(footwear==1)
                list.add("footwear");

            if(accessories==1)
                list.add("accessories");

            if(books==1)
                list.add("books");

            if(movies==1)
                list.add("movies");

            if(music==1)
                list.add("music");

            if(electronics==1)
                list.add("electronics");

            if(software==1)
                list.add("software");
        }
        catch (SQLiteException e) { // can't toast from a service
            final SQLiteException ee=e;
            mHandler.post(new Runnable(){
                public void run(){
                    Toast.makeText(SuggestOfferActivity.this,"ERROR DB select"+ ee.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception e) {
        }
        finally {
            db.endTransaction();
            db.close();
        }
    }

}
