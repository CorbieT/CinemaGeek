package com.bignerdranch.android.cinemaquiz.activities;

import android.os.Bundle;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.fragments.MainFragment;
import com.bignerdranch.android.cinemaquiz.interfaces.FragmentHandler;
import com.google.android.gms.ads.MobileAds;
import com.orhanobut.hawk.Hawk;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements FragmentHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        Hawk.init(this).build();
        MobileAds.initialize(this, getString(R.string.Ad_app_id));

        createFragment(MainFragment.newInstance());
    }

    @Override
    public void createFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    public void createFragmentWithBackStack(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment, tag)
                .addToBackStack(null)
                .commit();
    }


}
