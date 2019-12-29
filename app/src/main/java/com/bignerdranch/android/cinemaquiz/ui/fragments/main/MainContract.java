package com.bignerdranch.android.cinemaquiz.ui.fragments.main;

import com.bignerdranch.android.cinemaquiz.ui.fragments.BaseViewContract;

public interface MainContract {
    interface View extends BaseViewContract{
        void updateSoundImage(boolean isSoundOn);
    }

    interface Presenter {
        void onClickStartButton();
        void onClickSoundButton();
        void onClickAboutButton();
    }
}
