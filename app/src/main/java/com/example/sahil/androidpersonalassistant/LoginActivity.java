package com.example.sahil.androidpersonalassistant;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String username, password;
    EditText usernameEditText, passwordEditText;
    Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.cancel(0);

        sharedPreferences = getSharedPreferences("PreferencesToCheckIfLoggedIn", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);
        if(username != null)    {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("password", password);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else {
            setContentView(R.layout.activity_login);

            usernameEditText = (EditText) findViewById(R.id.usernameEditText);
            passwordEditText = (EditText) findViewById(R.id.passwordEditText);
            continueButton = (Button) findViewById(R.id.continueButton);

            continueButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (usernameEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")) {
                        Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", usernameEditText.getText().toString());
                        editor.putString("password", passwordEditText.getText().toString());
                        editor.commit();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("username", usernameEditText.getText().toString());
                        bundle.putString("password", passwordEditText.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}
