package com.example.spinach.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.example.spinach.LoginActivity;
import com.example.spinach.MainActivity;
import com.example.spinach.R;
import com.example.spinach.config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.example.spinach.R.id.emaildisp;
import static com.example.spinach.R.id.image_view;

public class Profile extends Fragment {
    private TextView tViewemail, tviewname, tviewidnum, tViewdlnum;
    private ImageView imageView;
    private String token;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        Log.d(TAG, "onCreate: "+token);

        displayData();




    }

    private void displayData() {
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
                            JSONObject jsonObject= new JSONObject(response);
                            JSONObject data= jsonObject.getJSONObject("data");
                            JSONObject userdata = data.getJSONObject("user");
                            String email =userdata.getString("email");
                            String name= userdata.getString("name");
                            tViewemail.setText(email);
                            tviewname.setText(name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            JSONObject data=jsonObject.getJSONObject("data");
                            String idNum= data.getString("id_number");
                            String dlNum= data.getString("license_number");
                            tviewidnum.setText(idNum);
                            tViewdlnum.setText(dlNum);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        tViewemail= view.findViewById(emaildisp);
        tviewname= view.findViewById(R.id.tViewName);
        tviewidnum= view.findViewById(R.id.iddisplay);
        tViewdlnum= view.findViewById(R.id.dldisplay);
        imageView= view.findViewById(image_view);


        return view;
    }

}
