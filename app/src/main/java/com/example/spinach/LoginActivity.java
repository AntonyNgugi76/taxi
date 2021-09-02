
package com.example.spinach;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.spinach.config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //Defining views
    private String token;
    private TextView register;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button blogin;
    SharedPreferences sharedPreferences;


    //boolean variable to check user is logged in or not
    //initially it is false
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences= this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        token=sharedPreferences.getString("token","");

        //Initializing views
        editTextEmail = (EditText) findViewById(R.id.emaillogin);
        editTextPassword = (EditText) findViewById(R.id.passwordlogin);
        blogin = (Button) findViewById(R.id.login);
        register = findViewById(R.id.registertview);

        //buttonLogin = (AppCompatButton) findViewById(R.id.buttonLogin);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });

        //Adding click listener
        blogin.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(config.LOGGEDIN_SHARED_PREF, false);
        //If we will get true
       if(loggedIn){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void login(){
        //Getting values from edit texts
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        //final String auth_Token= null;

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message =jsonObject.getString("message");
                            JSONObject data =jsonObject.getJSONObject("data");
                            //String token=data.getString("token");
                            //Log.d("status",""+jsonObject.getString("status"));
                            String token = jsonObject.getJSONObject("data").getString("token");
                            Log.d(TAG, "onResponse: "+token);
                            SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                            preferences.edit().putString("token", token).commit();
                          //  System.out.println("The token is: "+token);

                            //Log.d(TAG, "onResponse: "+jsonObject.getJSONObject("data").getString("token"));

                            if(jsonObject.getString("status").equalsIgnoreCase(config.LOGIN_SUCCESS)){
                                //Creating a shared preference
                                SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

                                //Creating editor to store values to shared preferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                //Adding values to editor
                                editor.putBoolean(config.LOGGEDIN_SHARED_PREF, true);
                                editor.putString(config.EMAIL_SHARED_PREF, email);
                                //editor.putString(config.AUTH_TOKEN_SHARED_PREF,jsonObject.getJSONObject("data").getString("token"));

                                //Saving values to editor
                                editor.commit();


                                //Starting profile activity
                                confirmValidation();
                                /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);*/
                            }else{
                                //If the server response is not success
                                //Displaying an error message on toast
                                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(config.KEY_EMAIL, email);
                params.put(config.KEY_PASSWORD, password);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void confirmValidation() {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, config.DRIVER_DETAILS_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (!response.equals(null)) {
                                Log.e("Your Array Response", response);
                            } else {
                                Log.e("Your Array Response", "Data Null");
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject data =jsonObject.getJSONObject("data");
                                Boolean validated= data.getBoolean("verified");
                                Log.d(TAG, "onResponse: "+validated);

                                if (validated){
                                    Intent intent= new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, Verification.class);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "onResponse: "+ token);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want
                            NetworkResponse response = error.networkResponse;
                            if (error instanceof ServerError && response != null) {
                                try {
                                    String res = new String(response.data,
                                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                    // Now you can use any deserializer to make sense of data
                                    JSONObject obj = new JSONObject(res);
                                } catch (UnsupportedEncodingException e1) {
                                    // Couldn't properly decode data to string
                                    e1.printStackTrace();
                                } catch (JSONException e2) {
                                    // returned data is not JSONObject?
                                    e2.printStackTrace();
                                }
                            }
                        }
                    }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accept", "application/json; charset=UTF-8");
                    params.put("Authorization","Bearer "+ token);
                    return params;

                }
            };

            //Adding the string request to the queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
    }


    @Override
    public void onClick(View v) {
        //Calling the login function
        login();
    }
}
