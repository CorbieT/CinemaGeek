package com.bignerdranch.android.cinemaquiz.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.utils.SingletonFonts;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainFragment extends Fragment {

    public static final String KEY_SOUND = "sound";

    @BindView(R.id.main_title)
    TextView mTitleButton;

    @BindView(R.id.start_button)
    TextView mStartButton;

    @BindView(R.id.rules_button)
    TextView mRulesButton;

    @BindView(R.id.faq_button)
    TextView mFaqButton;

    @BindView(R.id.sound_button)
    ImageButton mSoundButton;

    @BindView(R.id.rate_button)
    ImageButton mRateButton;

    private Unbinder unbinder;


    private boolean isSound;
    private SharedPreferences mPref;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cinema_quiz, container, false);
        unbinder = ButterKnife.bind(this, view);

        mPref = Objects.requireNonNull(getActivity()).getSharedPreferences(QuestionFragment.APP_TAG, Context.MODE_PRIVATE);
        isSound = mPref.getBoolean(KEY_SOUND, true);

        mStartButton.setTypeface(SingletonFonts.getInstance(getActivity()).getFont1());
        mStartButton.setOnClickListener(view1 -> {
            FragmentTransaction trans = Objects.requireNonNull(getFragmentManager()).beginTransaction();
            trans.replace(R.id.fragmentContainer, CategoriesFragment.newInstance())
                    .addToBackStack(null).commit();
        });

        mRulesButton.setTypeface(SingletonFonts.getInstance(getActivity()).getFont1());
        mRulesButton.setOnClickListener(view12 -> {
            FragmentTransaction trans = Objects.requireNonNull(getFragmentManager()).beginTransaction();
            trans.replace(R.id.fragmentContainer, RulesFragment.newInstance())
                    .addToBackStack(null).commit();
        });

        mFaqButton.setTypeface(SingletonFonts.getInstance(getActivity()).getFont1());
        mFaqButton.setOnClickListener(view13 -> {
            FragmentTransaction trans = Objects.requireNonNull(getFragmentManager()).beginTransaction();
            trans.replace(R.id.fragmentContainer, FaqFragment.newInstance())
                    .addToBackStack(null).commit();
        });

        mRateButton.setOnClickListener(view14 -> showDialogForRate());

        setSoundImage();
        mSoundButton.setOnClickListener(view15 -> {
            isSound = !isSound;
            setSoundImage();
            SharedPreferences.Editor editor = mPref.edit();
            editor.putBoolean(KEY_SOUND, isSound).apply();
        });

        mTitleButton.setTypeface(SingletonFonts.getInstance(getActivity()).getFont1());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setSoundImage() {
        if (!isSound) mSoundButton.setImageResource(R.drawable.volume_off);
        else mSoundButton.setImageResource(R.drawable.volume_on);
    }

    private boolean isActivityStarted(Intent intent) {
        try {
            startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    private void showDialogForRate() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialog.setTitle(getString(R.string.dialog_title));
        alertDialog.setMessage(getString(R.string.dialog_rate_message));
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton(getString(R.string.positive_button), (dialogInterface, i) -> rateThisApp());
        alertDialog.setNegativeButton(getString(R.string.negative_button), (dialogInterface, i) -> {
        });
        alertDialog.show();
    }

    private void rateThisApp() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.bignerdranch.android.cinemaquiz"));
        if (isActivityStarted(intent)) {
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.bignerdranch.android.cinemaquiz"));
        } else {
            Toast.makeText(getActivity(), "Could not open Android market, please check if the market app installed or not. Try again later", Toast.LENGTH_SHORT).show();
        }
    }
}
