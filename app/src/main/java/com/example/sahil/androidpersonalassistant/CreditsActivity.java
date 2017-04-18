package com.example.sahil.androidpersonalassistant;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by SAHIL on 18-04-2017.
 */

public class CreditsActivity extends AppCompatActivity{
    TextView creditsLinkTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        creditsLinkTextView = (TextView) findViewById(R.id.creditsLinkTextView);
        creditsLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * referred from: http://stackoverflow.com/questions/2521959/how-to-scale-an-image-in-imageview-to-keep-the-aspect-ratio
                 * we modify it to go the link: http://www.freepik.com
                 */
                String url = "http://www.freepik.com";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // Chrome is probably not installed
                    // Try with the default browser
                    intent.setPackage(null);
                    startActivity(intent);
                } catch (Exception e)   {
                    Toast.makeText(CreditsActivity.this, "Error while opening your browser", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
