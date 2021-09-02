package com.example.spinach.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.spinach.CompleteProfile;
import com.example.spinach.R;
import com.example.spinach.config;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Expenses extends Fragment {

    EditText description,amount;
    Button sub;
    String retrieveToken;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    private void postExpense() {
        String desc = description.getText().toString().trim();
        String am = amount.getText().toString().trim();
        StringRequest srequest = new StringRequest(Request.Method.POST, config.EXPENSES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("Accept","application/json; charset=UTF-8");
                headers.put("Authorization","Bearer "+ retrieveToken);
                return headers;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("type",desc);
                params.put("amount",am);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(srequest);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        description = view.findViewById(R.id.description);
        //description = view.findViewById(R.id.description);
        amount = view.findViewById(R.id.amount);
        sub = view.findViewById(R.id.outlinedButton);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences getToken = Expenses.this.getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                retrieveToken = getToken.getString("token",null);
                Log.d(TAG, "onClick: "+ retrieveToken);
                postExpense();
            }
        });
        return view;
    }
}