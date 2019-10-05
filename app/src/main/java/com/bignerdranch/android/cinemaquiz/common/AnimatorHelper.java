package com.bignerdranch.android.cinemaquiz.common;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bignerdranch.android.cinemaquiz.R;

public class AnimatorHelper {

    private Context context;

    public AnimatorHelper(Context context) {
        this.context = context;
    }

    public void animationTopDown(View view) {
        float butStart = view.getTop() - view.getHeight();
        float butEnd = view.getTop();

        ObjectAnimator buttonAnimator = ObjectAnimator.ofFloat(view, "y", butStart, butEnd)
                .setDuration(200);
        buttonAnimator.start();
    }

    public void animationHideDown(View view) {
        float butStart = view.getTop();
        float butEnd = view.getBottom();

        ObjectAnimator buttonAnimator = ObjectAnimator.ofFloat(view, "y", butStart, butEnd)
                .setDuration(200);
        buttonAnimator.start();
    }

    public void animationWrongText(TextView textView) {
        ObjectAnimator wrongAnimator = ObjectAnimator
                .ofInt(textView, "textColor", textView.getCurrentTextColor(), ContextCompat.getColor(context, R.color.wrongAnswer))
                .setDuration(200);
        wrongAnimator.setRepeatCount(1);
        wrongAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator normalAnimator = ObjectAnimator
                .ofInt(textView, "textColor", ContextCompat.getColor(context, R.color.wrongAnswer), textView.getCurrentTextColor())
                .setDuration(200);
        normalAnimator.setRepeatCount(1);
        normalAnimator.setEvaluator(new ArgbEvaluator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(wrongAnimator)
                .before(normalAnimator);
        animatorSet.start();
    }
}
