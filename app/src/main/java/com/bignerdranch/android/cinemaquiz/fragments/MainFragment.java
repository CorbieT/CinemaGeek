package com.bignerdranch.android.cinemaquiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.common.SharedPrefHelper;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainFragment extends BaseFragment {

    @BindView(R.id.main_title)
    TextView titleButton;

    @BindView(R.id.start_button)
    TextView startButton;

    @BindView(R.id.about_button)
    TextView aboutButton;

    @BindView(R.id.sound_button)
    ImageButton soundButton;

    @BindView(R.id.sound_container)
    LinearLayout soundContainer;

    private Unbinder unbinder;

    private SharedPrefHelper sharedPrefHelper;
    private boolean isSound;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        sharedPrefHelper = new SharedPrefHelper(Objects.requireNonNull(getActivity()));
        isSound = sharedPrefHelper.isKeySound();

        updateSoundImage(isSound);

        setViewsOnClickListeners(view);
        return view;
    }

    @OnClick({R.id.start_button, R.id.about_button, R.id.sound_container})
    public void setViewsOnClickListeners(View view) {
        switch (view.getId()) {
            case R.id.start_button:
                createFragmentWithBackStack(CategoriesFragment.newInstance());
                break;
            case R.id.about_button:
                createFragmentWithBackStack(AboutFragment.newInstance());
                break;
            case R.id.sound_container:
                isSound = !isSound;
                updateSoundImage(isSound);
                sharedPrefHelper.setKeySound(isSound);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void updateSoundImage(boolean isSoundOn) {
        if (isSoundOn) soundButton.setImageResource(R.drawable.volume_on);
        else soundButton.setImageResource(R.drawable.volume_off);
    }

}
