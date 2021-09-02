package com.example.spinach;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class RegisterActivity extends AppCompatActivity {
    Button bRegister;
    EditText nameeT, emaileT, passwordeT, cpasswordeT;
    TextView tviewlog;
    /*private String URL = "https://authentic-sparrows.herokuapp.com/api/";
    private String REGISTER = URL+"register";*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tviewlog= findViewById(R.id.logintview);
        nameeT = findViewById(R.id.name1);
        emaileT = findViewById(R.id.email);
        passwordeT= findViewById(R.id.password);
        cpasswordeT = findViewById(R.id.passwordcnf);
        bRegister= findViewById(R.id.register);
        tviewlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            }
        });
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = nameeT.getText().toString().trim();
                String useremail= emaileT.getText().toString().trim();
                String userpassword=passwordeT.getText().toString().trim();
                String cpassword = cpasswordeT.getText().toString().trim();


                if (TextUtils.isEmpty(username)){
                    Toast.makeText(RegisterActivity.this, "Name Cannot be Empty", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(useremail)){
                    Toast.makeText(RegisterActivity.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(userpassword)){
                    Toast.makeText(RegisterActivity.this, "Password Cannot be Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (userpassword.length()<6){
                    Toast.makeText(RegisterActivity.this,"password Too Short", Toast.LENGTH_SHORT).show();
                }
                if (!userpassword.equals(cpassword)){
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
                StringRequest stringRequest = new StringRequest(Request.Method.POST, config.REGISTER_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject data = jsonObject.getJSONObject("data");
                            String token = data.getString("token");
                            Log.d(TAG, "onResponse: "+data.getString("token"));
                            Log.d(TAG, "onResponse: "+jsonObject.getString("status"));
                            if (jsonObject.getString("status").equalsIgnoreCase(config.REGISTER_SUCCESS)){
                                SharedPreferences sharedPreferences = RegisterActivity.this.getSharedPreferences(config.SHARED_PREF_NAME,
                                        Context.MODE_PRIVATE);
                                sharedPreferences.edit().putString(config.AUTH_TOKEN_SHARED_PREF,token).apply();
                                startActivity(new Intent(RegisterActivity.this,CompleteProfile.class));
                                finish();
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.d(TAG, "onErrorResponse: "+error.toString());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(RegisterActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(RegisterActivity.this, "Authentication Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(RegisterActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(RegisterActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(RegisterActivity.this, "Parse Error:Account Exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put(config.KEY_NAME, username);
                        params.put(config.KEY_EMAIL, useremail);
                        params.put(config.KEY_PASSWORD, userpassword);
                        params.put(config.KEY_CPASSWORD, cpassword);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        });

    }




}