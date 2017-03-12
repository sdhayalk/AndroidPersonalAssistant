package com.example.sahil.androidpersonalassistant;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String username, password;
    boolean keepLoggedIn;
    EditText usernameEditText, passwordEditText;
    Button continueButton;
    Button registerButton;
    CheckBox rememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.cancel(0);

        sharedPreferences = getSharedPreferences("PreferencesToCheckIfLoggedIn", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);
        password = sharedPreferences.getString("password", null);
        keepLoggedIn=sharedPreferences.getBoolean("keepLoggedIn",false);

        if(keepLoggedIn){
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
            rememberMe=(CheckBox)findViewById(R.id.rememberMeCheckBox);

            continueButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (usernameEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")) {
                        Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                    } else {
                        if(username==null){
                            Toast.makeText(LoginActivity.this, "User Not Registered!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (!(username.equals(usernameEditText.getText().toString()))) {
                                Toast.makeText(LoginActivity.this, "User Not Registered!", Toast.LENGTH_SHORT).show();

                            } else {
                                if (!(password.equals(passwordEditText.getText().toString()))) {
                                    Toast.makeText(LoginActivity.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("username", usernameEditText.getText().toString());
                                    bundle.putString("password", passwordEditText.getText().toString());
                                    if(rememberMe.isChecked()){
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean("keepLoggedIn",true);
                                        editor.commit();
                                    }
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            }
                        }
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("username", usernameEditText.getText().toString());
//                        editor.putString("password", passwordEditText.getText().toString());
//                        editor.commit();
                    }
                }
            });

            registerButton=(Button) findViewById(R.id.registerButton);
            registerButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", usernameEditText.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }
}
