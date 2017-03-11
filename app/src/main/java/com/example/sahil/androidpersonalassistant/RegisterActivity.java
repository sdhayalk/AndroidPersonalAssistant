package com.example.sahil.androidpersonalassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by dpkch on 3/10/2017.
 * Dude 1
 */

public class RegisterActivity  extends AppCompatActivity {
    Button registerButton;
    String username;
    EditText usernameEditText,passwordEditText,emailEditText,rePasswordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_register);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");

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
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}
