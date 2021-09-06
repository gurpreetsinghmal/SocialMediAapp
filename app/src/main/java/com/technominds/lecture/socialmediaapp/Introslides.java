package com.technominds.lecture.socialmediaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.technominds.lecture.socialmediaapp.CustomAdapters.IntroAdapter;
import com.technominds.lecture.socialmediaapp.CustomModels.IntroModel;

import java.util.ArrayList;
import java.util.List;

public class Introslides extends AppCompatActivity {

    List<IntroModel> list=new ArrayList<>();
    IntroAdapter introAdapter;
    ViewPager intro_viewpager;
    TabLayout intro_tab;
    Button btnnext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(readpref())
        {
            movetosplash();
        }
        setContentView(R.layout.activity_introslides);
        intro_viewpager=findViewById(R.id.intro_viewpager);
        intro_tab=findViewById(R.id.intro_tablayout);
        btnnext=findViewById(R.id.intro_btn_next);

        list.add(new IntroModel(R.mipmap.pic1,"Welcome to Social Media"));
        list.add(new IntroModel(R.mipmap.pic2,"Post Your Dreams"));
        list.add(new IntroModel(R.mipmap.pic3,"Share and Change World"));

        introAdapter=new IntroAdapter(list,Introslides.this);
        intro_viewpager.setAdapter(introAdapter);

        intro_tab.setupWithViewPager(intro_viewpager);

        intro_tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                  if(tab.getPosition()==list.size()-1)
                  {
                      btnnext.setVisibility(View.VISIBLE);
                  }
                  else
                      btnnext.setVisibility(View.GONE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savepref();
                movetosplash();
            }
        });
    }

    private void movetosplash() {
        startActivity(new Intent(this, SplashScreen.class));
        finish();
    }

    private void savepref() {
        SharedPreferences pref=getSharedPreferences("Socialmediaapp",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putBoolean("isvisited",true);
        editor.commit();
    }
    private Boolean readpref(){
        SharedPreferences pref=getSharedPreferences("Socialmediaapp",MODE_PRIVATE);
        return pref.getBoolean("isvisited",false);
    }
}