package com.example.sahil.androidpersonalassistant;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


//import org.json.JSONObject;
//import cz.msebera.android.httpclient.Header;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity implements OnRequestCompleted{

    SharedPreferences sharedPreferences;
    String username, password;
    boolean keepLoggedIn;
    EditText usernameEditText, passwordEditText;
    Button continueButton;
    Button registerButton;
    CheckBox rememberMe;

    ProgressDialog prgDialog;
    InvokeServerAPI invokeServerAPI;
    OnRequestCompleted onRequestCompleted;

    static final String AWS_API_TAG =  "users_login";
    static final String LOG_TAG = "LOGIN_SERVICE";

    @Override
    public void taskCompleted(String stringResponse, JSONObject jsonResponse)
    {
        runOnCompletion(stringResponse, jsonResponse);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(runtime_permissions())  {}

        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.cancel(0);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        sharedPreferences = getSharedPreferences("PreferencesToCheckIfLoggedIn", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);
        password = sharedPreferences.getString("password", null);
        keepLoggedIn=sharedPreferences.getBoolean("keepLoggedIn",false);

//        invokeServerAPI = new InvokeServerAPI();
        invokeServerAPI = new InvokeServerAPI(getApplicationContext(), this);

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



                    String username_text = usernameEditText.getText().toString();
                    String password_text = passwordEditText.getText().toString();

                    RequestParams requestParams = new RequestParams();
//                    username == null && Utility.isNotNull(username_text) && Utility.isNotNull(password_text)
                    if( Utility.isNotNull(username_text) && Utility.isNotNull(password_text))
                    {
                        prgDialog.show();

                        requestParams.put("user_name", username_text);
                        requestParams.put("password", password_text);

                        String stringParams = String.format("user_name=%s&password=%s",username_text,password_text);

                        invokeServerAPI.executeRequest(stringParams,requestParams,AWS_API_TAG);


                    } else if (Utility.isNull(username_text) || Utility.isNull(password_text)){
                        Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
                    }

                    else {
                            if ( !(username.equals(username_text)))
                            {
                                Toast.makeText(LoginActivity.this, "User Not Registered!", Toast.LENGTH_SHORT).show();

                            }
                            else if ( !(password.equals(password_text)) )
                            {
                                    Toast.makeText(LoginActivity.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();

                            }
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

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", usernameEditText.getText().toString());
                bundle.putString("password", passwordEditText.getText().toString());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username",username_text);
                editor.putString("password",password_text);
                if(rememberMe.isChecked()){
                    editor.putBoolean("keepLoggedIn",true);
                }
                else
                    editor.putBoolean("keepLoggedIn",false);
                editor.commit();
                intent.putExtras(bundle);
                startActivity(intent);

            }
            else
            {
                Toast.makeText(getApplicationContext(), request_message, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {

            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            Log.i(LOG_TAG + " | Exp ", e.toString());
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
