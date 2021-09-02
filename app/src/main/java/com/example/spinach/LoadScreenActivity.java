package com.example.spinach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;

public class LoadScreenActivity extends AppCompatActivity {

    LottieAnimationView load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);

        getSupportActionBar().hide();

        load = findViewById(R.id.load);
        load.playAnimation();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LoadScreenActivity.this,LoginActivity.class));
                finish();
            }
        },5000);

    }
}