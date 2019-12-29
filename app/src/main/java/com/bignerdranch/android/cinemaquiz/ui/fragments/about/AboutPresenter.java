package com.bignerdranch.android.cinemaquiz.ui.fragments.about;

public class AboutPresenter implements AboutContract.Presenter {

    private AboutContract.View view;

    public AboutPresenter(AboutContract.View view) {
        this.view = view;
    }

    @Override
    public void onClickMailButton() {
        view.showMailChooser();
    }

    @Override
    public void onClickRateButton() {
        view.showRateDialog();
    }
}
