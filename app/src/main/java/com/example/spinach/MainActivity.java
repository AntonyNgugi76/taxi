package com.example.spinach;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.spinach.Adapters.ViewpagerAdapter;
import com.example.spinach.Fragments.Expenses;
import com.example.spinach.Fragments.HomeFragment;
import com.example.spinach.Fragments.Profile;

import com.example.spinach.mpesa.PhoneNo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    //This is our viewPager
    private ViewPager viewPager;


    //Fragments

    HomeFragment homeFragment;
    /*Remittances remitFragment;*/
    Profile profileFragment;
    Expenses expenseFragment;
    MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }else getSupportActionBar().show();*/

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //Initializing the bottomNavigationView
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bbn_home:
                                viewPager.setCurrentItem(0);
                                break;/*
                            case R.id.bbn_remittances:
                                viewPager.setCurrentItem(1);
                                break;*/
                            case R.id.bbn_expenses:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.profile:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

       /*  //Disable ViewPager Swipe
       viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
        */

        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewpagerAdapter adapter = new ViewpagerAdapter(getSupportFragmentManager());
        homeFragment=new HomeFragment();
        /*remitFragment=new Remittances();*/
        expenseFragment=new Expenses();
        profileFragment=new Profile();

        adapter.addFragment(homeFragment);
        /*adapter.addFragment(remitFragment);*/
        adapter.addFragment(expenseFragment);
        adapter.addFragment(profileFragment);
        viewPager.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.logout_button:
                SharedPreferences preferences =getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                finish();
                break;*/
            case R.id.remittance:
                Intent intent = new Intent(MainActivity.this, RemittanceHistory.class);
                //Toast.makeText(this, "Remittance History", Toast.LENGTH_SHORT).show();
                 startActivity(intent);
                 break;
            case R.id.remit:
            Intent intent1 = new Intent(MainActivity.this, PhoneNo.class);
           // Toast.makeText(this, "Remittance History", Toast.LENGTH_SHORT).show();
            startActivity(intent1);
            break;



        }
                return true;

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}