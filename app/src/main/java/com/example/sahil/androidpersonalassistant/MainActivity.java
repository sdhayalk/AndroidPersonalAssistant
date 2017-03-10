package com.example.sahil.androidpersonalassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    String username, password;
    Button sendNotification;
    String mainMsg, sideMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        password = bundle.getString("password");

        mainMsg = "Main Message";
        sideMsg = "Side Message";

        sendNotification = (Button) findViewById(R.id.sendNotificationButton);
        sendNotification.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("mainMsg", mainMsg);
                bundle.putString("sideMsg", sideMsg);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}