package com.bignerdranch.android.cinemaquiz.fragments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bignerdranch.android.cinemaquiz.interfaces.FragmentHandler;

public abstract class BaseFragment extends Fragment {

    private FragmentHandler fragmentHandler;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            fragmentHandler = (FragmentHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FragmentHandler");
        }
    }

    protected void createFragment(Fragment fragment) {
        fragmentHandler.createFragment(fragment);
    }

    protected void createFragmentWithBackStack(Fragment fragment, String tag) {
        fragmentHandler.createFragmentWithBackStack(fragment, tag);
    }

}
