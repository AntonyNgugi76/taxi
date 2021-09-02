package com.example.spinach.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.spinach.Adapters.CarInfoAdapter;
import com.example.spinach.CarInfoGetterSetter;
import com.example.spinach.LoginActivity;
import com.example.spinach.MainActivity;
import com.example.spinach.R;
import com.example.spinach.config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
     RecyclerView.Adapter adapter;
     int[] images;
    private String token;
    private TextView tViewname, carplateNo, chasisNo, insuranceNo, insuranceExp;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token","");
        Log.d(TAG, "onCreate: "+token);
        showData();


    }
    private void showData() {
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
                            //String licenceNumber = data.getString("license_number");
                            JSONObject car = data.getJSONObject("car");
                            String plateNo= car.getString("plate_number");
                            String chasis_No= car.getString("chasis_number");
                            String insurance_No= car.getString("insurance_number");
                            String insurance_Exp= car.getString("insurance_expiry_date");

                            //Appending to textviews
                            carplateNo.setText(plateNo);
                            chasisNo.setText(chasis_No);
                            insuranceNo.setText(insurance_No);
                            //tViewname.setText(username);
                            insuranceExp.setText(insurance_Exp);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject data =jsonObject.getJSONObject("data");
                            JSONObject user = data.getJSONObject("user");
                            String username = user.getString("name");
                            tViewname.setText(username);
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        tViewname= view.findViewById(R.id.namehome);
        carplateNo= view.findViewById(R.id.carplateNo);
        chasisNo= view.findViewById(R.id.chasisNo);
        insuranceNo= view.findViewById(R.id.insuranceNo);
        insuranceExp= view.findViewById(R.id.insuranceExp);
        carInfo();


        return  view;

    }
    private void carInfo() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        ArrayList<CarInfoGetterSetter> car_info = new ArrayList<>();
        car_info.add(new CarInfoGetterSetter(R.drawable.car_removebg));
        car_info.add(new CarInfoGetterSetter(R.drawable.car_removebg));
        car_info.add(new CarInfoGetterSetter(R.drawable.car_removebg));
        car_info.add(new CarInfoGetterSetter(R.drawable.car_removebg));

        adapter = new CarInfoAdapter(car_info);
        recyclerView.setAdapter(adapter);
    }
}
