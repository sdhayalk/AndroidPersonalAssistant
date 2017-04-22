package com.example.sahil.androidpersonalassistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;
/*
 * Created by dpkch on 3/10/2017.
 */

public class RegisterActivity  extends AppCompatActivity implements OnRequestCompleted{
    Button registerButton,backButton;
    String username;
    SharedPreferences sharedPreferences;
    EditText usernameEditText,passwordEditText,emailEditText,rePasswordEditText;

    static final String AWS_API_TAG =  "users_register";
    static final String LOG_TAG = "REG_SERVICE";

    ProgressDialog prgDialog;
    InvokeServerAPI invokeServerAPI;
    OnRequestCompleted onRequestCompleted;

    @Override
    public void taskCompleted(String stringResponse, JSONObject jsonResponse)
    {
        runOnCompletion(stringResponse, jsonResponse);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_register);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("user_name");

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        sharedPreferences = getSharedPreferences("PreferencesToCheckIfLoggedIn", Context.MODE_PRIVATE);

        invokeServerAPI = new InvokeServerAPI(getApplicationContext(), this);


        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        rePasswordEditText = (EditText) findViewById(R.id.rePasswordEditText);


//        usernameEditText.setText(username);

        registerButton=(Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String username_text = usernameEditText.getText().toString();
                String password_text = passwordEditText.getText().toString();
                String repassword_text = rePasswordEditText.getText().toString();
                String email_text = emailEditText.getText().toString();

                if(Utility.isNull(username_text) || Utility.isNull(password_text) || Utility.isNull(email_text))
                {
                    Toast.makeText(RegisterActivity.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!password_text.equals(repassword_text))
                {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                }
                else if(!isValidEmail(email_text)){
                        Toast.makeText(RegisterActivity.this, "Invalid Email!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                else{

                    prgDialog.show();

                    RequestParams requestParams = new RequestParams();

                    requestParams.put("user_name", username_text);
                    requestParams.put("password", password_text);
                    requestParams.put("email_id", email_text);

                    String stringParams = String.format("user_name=%s&password=%s&email_id=%s",username_text,password_text,email_text);

                    invokeServerAPI.executeRequest(stringParams,requestParams,AWS_API_TAG);

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


    void runOnCompletion(String stringResponse, JSONObject jsonResponse)
    {
        prgDialog.hide();
        String username_text = usernameEditText.getText().toString();
        String password_text = passwordEditText.getText().toString();

        try {

            boolean request_success = Boolean.valueOf(Utility.jsonRecurseKeys(jsonResponse,"success"));
            String request_message = Utility.jsonRecurseKeys(jsonResponse,"message");
            if ( request_success )
            {
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("user_name", username_text);
//                editor.putString("password", password_text);
//                editor.putBoolean("keepLoggedIn",false);
//                editor.commit();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);

            }
            else
            {
                Toast.makeText(getApplicationContext(), request_message, Toast.LENGTH_LONG).show();
                Log.i(LOG_TAG + " | Exp ", request_message);
            }

        } catch (JSONException e) {

            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            Log.i(LOG_TAG + " | Exp ", e.toString());
        }

    }

    private boolean isValidEmail(String email) { // validate the email address
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}
