package com.bignerdranch.android.cinemaquiz.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import com.bignerdranch.android.cinemaquiz.fragments.MainFragment;
import com.bignerdranch.android.cinemaquiz.R;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        MobileAds.initialize(this, getString(R.string.Ad_app_id));

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if(fragment == null){
            fragment = MainFragment.newInstance();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }

}
