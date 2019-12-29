package com.bignerdranch.android.cinemaquiz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    protected abstract int getLayoutId();

    protected void createFragment(Fragment fragment) {
        fragmentHandler.createFragment(fragment);
    }

    protected void createFragmentWithBackStack(Fragment fragment) {
        fragmentHandler.createFragmentWithBackStack(fragment, null);
    }

}
