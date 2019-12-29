package com.bignerdranch.android.cinemaquiz.ui.fragments.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.common.SharedPrefHelper;
import com.bignerdranch.android.cinemaquiz.ui.fragments.BaseFragment;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.bignerdranch.android.cinemaquiz.ui.fragments.main.MainContract.Presenter;

public class MainFragment extends BaseFragment implements MainContract.View {

    @BindView(R.id.sound_button)
    ImageButton soundButton;

    private Unbinder unbinder;

    private Presenter presenter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        setViewsOnClickListeners(view);
        presenter = new MainPresenter(this, new SharedPrefHelper(Objects.requireNonNull(getActivity()).getApplicationContext()));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.main_fragment;
    }

    @OnClick({R.id.start_button, R.id.about_button, R.id.sound_container})
    public void setViewsOnClickListeners(@NonNull View view) {
        switch (view.getId()) {
            case R.id.start_button:
                presenter.onClickStartButton();
                break;
            case R.id.sound_container:
            case R.id.sound_button:
                presenter.onClickSoundButton();
                break;
            case R.id.about_button:
                presenter.onClickAboutButton();
                break;
            default:
                break;
        }
    }

    @Override
    public void updateSoundImage(boolean isSoundOn) {
        if (isSoundOn) soundButton.setImageResource(R.drawable.volume_on);
        else soundButton.setImageResource(R.drawable.volume_off);
    }

    @Override
    public void openFragment(Fragment fragment) {
        createFragmentWithBackStack(fragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
