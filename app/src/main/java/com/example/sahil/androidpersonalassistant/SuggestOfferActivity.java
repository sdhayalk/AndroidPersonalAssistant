package com.example.sahil.androidpersonalassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by dpkch on 3/17/2017.
 */

public class SuggestOfferActivity extends AppCompatActivity {
    // use this class for Weather Suggestions
    String shareMessage;
    Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_offer);

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
    }
}
