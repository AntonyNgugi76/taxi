package com.example.spinach;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CompleteProfile extends AppCompatActivity {
    private EditText idnum,licensenum,pnum, psvexpiry,licenseexpiry;
    TextView idtext, dltext, psvtext, passporttext;
    Button bID, bDL, bPassport, bPSV, bSubmit;
    private String image;


    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        getSupportActionBar().hide();

        idnum = findViewById(R.id.id_num);
        licensenum = findViewById(R.id.license_num);
        licenseexpiry = findViewById(R.id.license_expiry_number);
        psvexpiry = findViewById(R.id.psv_expiry_number);
        pnum = findViewById(R.id.phone_number);
        idtext = findViewById(R.id.idtext);
        dltext = findViewById(R.id.dltext);
        psvtext = findViewById(R.id.psvtext);
        passporttext = findViewById(R.id.passporttext);
        bDL = findViewById(R.id.dl);
        bID = findViewById(R.id.uid);
        bPassport = findViewById(R.id.passport);
        bPSV = findViewById(R.id.psv);
        bSubmit = findViewById(R.id.submit);
        bDL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else{
                        galleryImage();
                    }
                }
                else{
                    galleryImage();
                }

            }
        });
        bID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else{
                        galleryImage();
                    }
                }
                else{
                    galleryImage();
                }

            }
        });
        bPassport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else{
                        galleryImage();
                    }
                }
                else{
                    galleryImage();
                }
            }
        });
        bPSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else{
                        galleryImage();
                    }
                }
                else{
                    galleryImage();
                }
            }
        });
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idNumber = idnum.getText().toString().trim();
                String licenseNumber = licensenum.getText().toString().trim();
                String phoneNumber = pnum.getText().toString().trim();
                String licenseExpiryDate = licenseexpiry.getText().toString().trim();
                String psvExpiryDate = psvexpiry.getText().toString().trim();

                if (TextUtils.isEmpty(idNumber)){
                    Toast.makeText(CompleteProfile.this, "Field Cannot be Empty", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(licenseNumber)){
                    Toast.makeText(CompleteProfile.this, "Field Cannot be Empty", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(CompleteProfile.this, "Field Cannot be Empty", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(licenseExpiryDate)){
                    Toast.makeText(CompleteProfile.this, "Field Cannot be Empty", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(psvExpiryDate)){
                    Toast.makeText(CompleteProfile.this, "Field Cannot be Empty", Toast.LENGTH_SHORT).show();
                }
                //Retrieve token
                SharedPreferences getToken = CompleteProfile.this.getSharedPreferences(config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String retrieveToken = getToken.getString(config.AUTH_TOKEN_SHARED_PREF,null);
                //Toast.makeText(CompleteProfile.this, "token is:"+retrieveToken, Toast.LENGTH_SHORT).show();
                //StringRequest to drivers api
                StringRequest stringRequest = new StringRequest(Request.Method.POST, config.COMPLETE_PROFILE_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals(null)) {
                            Log.e("Your Array Response", response);
                        } else {
                            Log.e("Your Array Response", "Data Null");
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equalsIgnoreCase("success")){
                                startActivity(new Intent(CompleteProfile.this,LoadScreenActivity.class));
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                       //
                        // Toast.makeText(CompleteProfile.this, "respone is" + response, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: "+response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(CompleteProfile.this, error.toString(), Toast.LENGTH_SHORT).show();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(CompleteProfile.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(CompleteProfile.this, "Authentication Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(CompleteProfile.this, "Server Error"+error.toString(), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onErrorResponse: ",error);
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(CompleteProfile.this, "Network Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(CompleteProfile.this, "Parse Error:Account Exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> headers = new HashMap<>();
                        headers.put("Accept","application/json; charset=UTF-8");
                        headers.put("Authorization","Bearer "+ retrieveToken);
                        return headers;
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String,String>();
                        params.put("id_number",idNumber);
                        params.put("license_number",licenseNumber);
                        params.put("phone_number",phoneNumber);
                        params.put("license_expiry_date",licenseExpiryDate);
                        params.put("psv_badge_expiry_date",psvExpiryDate);
                        //params.put("id_photo",image);
                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(stringRequest);




                Intent intent= new Intent(CompleteProfile.this, LoadScreenActivity.class);
                startActivity(intent);
            }
        });

    }

    private void galleryImage() {
        /*Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);*/
        Intent selectImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(selectImage,IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE: {
                if (grantResults.length >0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    galleryImage();
                }
                else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){


            try {
                Uri imageUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                uploadImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            /* String path = data.getData().getPath();
            dltext.setText(path);
            idtext.setText(path);
            psvtext.setText(path);
            passporttext.setText(path);
            Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();*/

        }
    }

    private void uploadImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        image = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);


    }
}