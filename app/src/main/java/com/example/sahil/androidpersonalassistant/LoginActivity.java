package com.example.sahil.androidpersonalassistant;

import android.*;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
        if(runtime_permissions())  {}

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

    //referred from: https://www.youtube.com/watch?v=lvcGh2ZgHeA
    boolean runtime_permissions()   {
        if(Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            return true;
        }
        return false;
    }

    //referred from: https://www.youtube.com/watch?v=lvcGh2ZgHeA
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED)    {

            }
            else    {
                runtime_permissions();
            }
        }
    }
}
