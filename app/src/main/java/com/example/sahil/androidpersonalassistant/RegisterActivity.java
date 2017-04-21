package com.example.sahil.androidpersonalassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;
/*
 * Created by dpkch on 3/10/2017.
 */

public class RegisterActivity  extends AppCompatActivity {
    Button registerButton,backButton;
    String username;
    SharedPreferences sharedPreferences;
    EditText usernameEditText,passwordEditText,emailEditText,rePasswordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_register);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");

        sharedPreferences = getSharedPreferences("PreferencesToCheckIfLoggedIn", Context.MODE_PRIVATE);


        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        rePasswordEditText = (EditText) findViewById(R.id.rePasswordEditText);


        usernameEditText.setText(username);

        registerButton=(Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(usernameEditText.getText().toString().equals("")||emailEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!passwordEditText.getText().toString().equals(rePasswordEditText.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "Password do not match!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(!isValidEmail(emailEditText.getText().toString())){
                        Toast.makeText(RegisterActivity.this, "Invalid Email!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", usernameEditText.getText().toString());
                    editor.putString("password", passwordEditText.getText().toString());
                    editor.putBoolean("keepLoggedIn",false);
                    editor.commit();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        backButton=(Button) findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
            }
        });

    }

    private boolean isValidEmail(String email) { // validate the email address
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}
