package com.bignerdranch.android.cinemaquiz.ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.interfaces.FragmentHandler;
import com.bignerdranch.android.cinemaquiz.ui.fragments.main.MainFragment;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.MobileAds;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements FragmentHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_fragment);

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
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter_back, R.anim.exit_back)
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(tag)
                .commit();
    }

}
