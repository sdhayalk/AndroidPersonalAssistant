package com.example.sahil.androidpersonalassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_offer);

        offersListView = (ListView) findViewById(R.id.offersListView);

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
}
