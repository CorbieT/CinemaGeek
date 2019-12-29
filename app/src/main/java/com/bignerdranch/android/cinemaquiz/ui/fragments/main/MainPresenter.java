package com.bignerdranch.android.cinemaquiz.ui.fragments.main;

import com.bignerdranch.android.cinemaquiz.common.SharedPrefHelper;
import com.bignerdranch.android.cinemaquiz.ui.fragments.about.AboutFragment;
import com.bignerdranch.android.cinemaquiz.ui.fragments.categories.CategoriesFragment;

import static com.bignerdranch.android.cinemaquiz.ui.fragments.main.MainContract.Presenter;
import static com.bignerdranch.android.cinemaquiz.ui.fragments.main.MainContract.View;

public class MainPresenter implements Presenter {

    private View view;
    private SharedPrefHelper prefHelper;
    private boolean isSoundOn;

    public MainPresenter(View view, SharedPrefHelper prefHelper) {
        this.view = view;
        this.prefHelper = prefHelper;
        isSoundOn = prefHelper.isKeySound();
        view.updateSoundImage(isSoundOn);
    }

    @Override
    public void onClickStartButton() {
        view.openFragment(CategoriesFragment.newInstance());
    }

    @Override
    public void onClickSoundButton() {
        isSoundOn = !isSoundOn;
        view.updateSoundImage(isSoundOn);
        prefHelper.setKeySound(isSoundOn);
    }

    @Override
    public void onClickAboutButton() {
        view.openFragment(AboutFragment.newInstance());
    }
}
