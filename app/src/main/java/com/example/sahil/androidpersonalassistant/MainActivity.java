package com.example.sahil.androidpersonalassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String username, password;
    Button signOutButton;
    Button sendNotification;
    Button buttonWeather,buttonOffer,buttonRestaurant;
    String mainMsg, sideMsg;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        password = bundle.getString("password");
        sharedPreferences = getSharedPreferences("PreferencesToCheckIfLoggedIn", Context.MODE_PRIVATE);
        mainMsg = "Main Message";
        sideMsg = "Side Message";

        signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("username");
                editor.remove("password");
                editor.remove("keepLoggedIn");
                editor.commit();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("mainMsg", mainMsg);
                bundle.putString("sideMsg", sideMsg);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        buttonOffer = (Button) findViewById(R.id.suggestOfferButton);
        buttonOffer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SuggestOfferActivity.class);
                startActivity(intent);
            }
        });
        buttonRestaurant = (Button) findViewById(R.id.suggestRestaurantButton);
        buttonRestaurant.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SuggestRestaurantActivity.class);
                startActivity(intent);
            }
        });
        buttonWeather = (Button) findViewById(R.id.suggestWeatherButton);
        buttonWeather.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SuggestWeatherActivity.class);
                startActivity(intent);
            }
        });

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

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

    }
}