package com.bignerdranch.android.cinemaquiz.ui.fragments.about;

public interface AboutContract {
    interface View {
        void showMailChooser();
        void showRateDialog();
    }

    interface Presenter {
        void onClickMailButton();
        void onClickRateButton();
    }
}
