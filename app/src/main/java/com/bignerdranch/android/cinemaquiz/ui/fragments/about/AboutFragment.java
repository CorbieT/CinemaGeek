package com.bignerdranch.android.cinemaquiz.ui.fragments.about;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.ui.fragments.BaseFragment;
import com.bignerdranch.android.cinemaquiz.ui.fragments.dialogs.RateDialogFragment;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AboutFragment extends BaseFragment implements AboutContract.View {

    private AboutContract.Presenter presenter;
    private Unbinder unbinder;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.about_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        setViewsOnClickListeners(view);
        presenter = new AboutPresenter(this);
    }

    @OnClick({R.id.mail_button, R.id.rate_button})
    public void setViewsOnClickListeners(View view) {
        switch (view.getId()) {
            case R.id.mail_button:
                presenter.onClickMailButton();
                break;
            case R.id.rate_button:
                presenter.onClickRateButton();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showMailChooser() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",getString(R.string.email_link), null));
        intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.email_link));
        startActivity(Intent.createChooser(intent, getString(R.string.send_email)));
    }

    @Override
    public void showRateDialog() {
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
