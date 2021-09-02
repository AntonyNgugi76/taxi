package com.example.spinach;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.spinach.Adapters.Remit_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RemittanceHistory extends AppCompatActivity {
    ListView listView; Button button;
    // Server Http URL
    String HTTP_URL = "https://android-examples.000webhostapp.com/all_subjects.php";
    // String to hold complete JSON response object.
    String FinalJSonObject ;
    ProgressBar progressBar;
    @Override
    protected void onCreate(
            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remittance_jistory);
// Assign ID's to ListView.
        listView = (ListView) findViewById(R.id.listView1);
        button = (Button)findViewById(R.id.buttonload);
        //progressBar = (ProgressBar)findViewById(R.id.ProgressBar1);
// Adding click listener to button.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Showing progress bar just after button click.
                progressBar.setVisibility(View.VISIBLE);
// Creating StringRequest and set the JSON server URL in here.
                StringRequest stringRequest = new StringRequest(HTTP_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
// After done Loading store JSON response in FinalJSonObject string variable.
                        FinalJSonObject = response ;
// Calling method to parse JSON object.
                        new ParseJSonDataClass(RemittanceHistory.this).execute();
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
// Showing error message if something goes wrong.
                                Toast.makeText(RemittanceHistory.this,error.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
// Creating String Request Object.
                RequestQueue requestQueue = Volley.newRequestQueue(RemittanceHistory.this);
// Passing String request into RequestQueue.
                requestQueue.add(stringRequest);
            }
        });
    }
    // Creating method to parse JSON object.
    private class ParseJSonDataClass extends AsyncTask<Void, Void, Void> {
        public Context context;
        // Creating List of Subject class.
        List<Remit_Item> CustomSubjectNamesList;
        public ParseJSonDataClass(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
// Checking whether FinalJSonObject is not equals to null.
                if (FinalJSonObject != null) {
// Creating and setting up JSON array as null.
                    JSONArray jsonArray = null;
                    try {
                        // Adding JSON response object into JSON array.
                        jsonArray = new JSONArray(FinalJSonObject);
// Creating JSON Object.
                        JSONObject jsonObject;
// Creating Subject class object.
                        Remit_Item subject;
// Defining CustomSubjectNamesList AS Array List.
                        CustomSubjectNamesList = new ArrayList<Remit_Item>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            subject = new Remit_Item();
                            jsonObject = jsonArray.getJSONObject(i);
//Storing ID into subject list.
                            subject.amount = jsonObject.getString("id");
//Storing Subject name in subject list.
                            subject.date = jsonObject.getString("subject_Name");
// Adding subject list object into CustomSubjectNamesList.
                            CustomSubjectNamesList.add(subject);
                        }
                    } catch (JSONException e) {
// TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void
        onPostExecute(Void result) {
// After all done loading set complete CustomSubjectNamesList with application context to ListView adapter.
            Remit_Adapter adapter = new Remit_Adapter(CustomSubjectNamesList, context);
// Setting up all data into ListView.
            listView.setAdapter(adapter);
// Hiding progress bar after all JSON loading done.
            //progressBar.setVisibility(View.GONE);
        }
    }
}