package com.bignerdranch.android.cinemaquiz.interfaces;

import androidx.fragment.app.Fragment;

public interface FragmentHandler {
    void createFragment(Fragment fragment);
    void createFragmentWithBackStack(Fragment fragment, String tag);
}
