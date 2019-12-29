package com.bignerdranch.android.cinemaquiz.ui.fragments.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.interfaces.Function;

import java.util.Objects;

public class BonusDialogFragment extends DialogFragment {

    private Function positiveClick;

    public BonusDialogFragment(Function positiveClick) {
        this.positiveClick = positiveClick;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setTitle(getString(R.string.dialog_title))
                .setMessage(getString(R.string.dialog_bonus_message))
                .setPositiveButton(getString(R.string.positive_button), (dialog, which) -> positiveClick.execute())
                .setNegativeButton(getString(R.string.negative_button), null)
                .create();
    }
}
