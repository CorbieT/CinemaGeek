package com.bignerdranch.android.cinemaquiz.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.common.HawkManager;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainFragment extends BaseFragment {

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

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cinema_quiz, container, false);
        unbinder = ButterKnife.bind(this, view);

        isSound = HawkManager.getInstance().isKeySound();

        updateSoundImage(isSound);

        setViewsOnClickListeners(view);
        return view;
    }

    @OnClick({R.id.start_button, R.id.rules_button, R.id.faq_button, R.id.rate_button, R.id.sound_button})
    public void setViewsOnClickListeners(View view) {
        switch (view.getId()) {
            case R.id.start_button:
                createFragmentWithBackStack(CategoriesFragment.newInstance(), null);
                break;
            case R.id.rules_button:
                createFragmentWithBackStack(RulesFragment.newInstance(), null);
                break;
            case R.id.faq_button:
                createFragmentWithBackStack(FaqFragment.newInstance(), null);
                break;
            case R.id.rate_button:
                showDialogForRate();
                break;
            case R.id.sound_button:
                isSound = !isSound;
                updateSoundImage(isSound);
                HawkManager.getInstance().setKeySound(isSound);
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
        if (isSoundOn) mSoundButton.setImageResource(R.drawable.volume_on);
        else mSoundButton.setImageResource(R.drawable.volume_off);
    }

    private void showDialogForRate() {
        new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setTitle(getString(R.string.dialog_title))
                .setMessage(getString(R.string.dialog_rate_message))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.positive_button), (dialogInterface, i) -> redirectToMarket())
                .setNegativeButton(getString(R.string.negative_button), (dialogInterface, i) -> {})
                .show();
    }

    private void redirectToMarket() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getString(R.string.market_url)));
        if (isActivityStarted(intent)) {
            intent.setData(Uri.parse(getString(R.string.market_browser_url)));
        } else {
            Toast.makeText(getActivity(), getString(R.string.error_open_market_message), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isActivityStarted(Intent intent) {
        try {
            startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }
}
