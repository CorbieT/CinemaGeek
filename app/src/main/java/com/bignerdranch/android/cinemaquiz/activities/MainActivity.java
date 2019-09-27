package com.bignerdranch.android.cinemaquiz.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.fragments.MainFragment;
import com.bignerdranch.android.cinemaquiz.interfaces.FragmentHandler;

public class MainActivity extends AppCompatActivity implements FragmentHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
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
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter_back, R.anim.exit_back);
        fragmentTransaction.replace(R.id.fragmentContainer, fragment, tag).commit();
    }

}
