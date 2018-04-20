package com.bignerdranch.android.cinemaquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class CinemaQuizFragment extends Fragment{

    public static final String KEY_SOUND = "sound";

    private TextView mTitleButton;
    private TextView mStartButton;
    private TextView mRulesButton;
    private TextView mFaqButton;

    private ImageButton mSoundButton;
    private boolean isSound;
    private SharedPreferences mPref;

    public static CinemaQuizFragment newInstance(){
        return new CinemaQuizFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_cinema_quiz, container, false);

        mPref = getActivity().getSharedPreferences(QuestionFragment.APP_TAG, Context.MODE_PRIVATE);
        isSound = mPref.getBoolean(KEY_SOUND, true);

        mStartButton = view.findViewById(R.id.start_button);
        mStartButton.setTypeface(SingletonFonts.getInstance(getActivity()).getFont1());
        mStartButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.fragmentContainer, CategoriesFragment.newInstance())
                        .addToBackStack(null).commit();
            }
        });

        mRulesButton = view.findViewById(R.id.rules_button);
        mRulesButton.setTypeface(SingletonFonts.getInstance(getActivity()).getFont1());
        mRulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.fragmentContainer, RulesFragment.newInstance())
                        .addToBackStack(null).commit();
            }
        });

        mFaqButton = view.findViewById(R.id.faq_button);
        mFaqButton.setTypeface(SingletonFonts.getInstance(getActivity()).getFont1());
        mFaqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.fragmentContainer, FaqFragment.newInstance())
                        .addToBackStack(null).commit();
            }
        });

        mSoundButton = view.findViewById(R.id.sound_button);
        setSoundImage();
        mSoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSound = !isSound;
                setSoundImage();
                SharedPreferences.Editor editor = mPref.edit();
                editor.putBoolean(KEY_SOUND, isSound).apply();
            }
        });

        mTitleButton = view.findViewById(R.id.main_title);
        mTitleButton.setTypeface(SingletonFonts.getInstance(getActivity()).getFont1());

        return view;
    }

    private void setSoundImage(){
        if (!isSound)  mSoundButton.setImageResource(R.drawable.sound_off);
        else  mSoundButton.setImageResource(R.drawable.sound_on);
    }
}
