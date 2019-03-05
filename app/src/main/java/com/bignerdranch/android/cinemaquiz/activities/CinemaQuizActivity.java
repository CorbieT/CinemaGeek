package com.bignerdranch.android.cinemaquiz.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.bignerdranch.android.cinemaquiz.fragments.CinemaQuizFragment;
import com.bignerdranch.android.cinemaquiz.R;
import com.google.android.gms.ads.MobileAds;

public class CinemaQuizActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        MobileAds.initialize(this, getString(R.string.Ad_app_id));

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if(fragment == null){
            fragment = CinemaQuizFragment.newInstance();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }

}
