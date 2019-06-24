package com.bignerdranch.android.cinemaquiz.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.fragments.dialogs.RateDialogFragment;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AboutFragment extends BaseFragment {

    private Unbinder unbinder;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        setViewsOnClickListeners(view);
        return view;
    }

    @OnClick({R.id.mail_button, R.id.rate_button})
    public void setViewsOnClickListeners(View view) {
        switch (view.getId()) {
            case R.id.mail_button:
                sendEmail();
                break;
            case R.id.rate_button:
                showDialogForRate();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",getString(R.string.email_link), null));
        intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.email_link));
        startActivity(Intent.createChooser(intent, getString(R.string.send_email)));

    }

    private void showDialogForRate() {
        new RateDialogFragment(this::redirectToMarket)
                .show(Objects.requireNonNull(getFragmentManager()), null);
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
