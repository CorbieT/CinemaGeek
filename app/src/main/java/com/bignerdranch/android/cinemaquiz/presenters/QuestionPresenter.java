package com.bignerdranch.android.cinemaquiz.presenters;

import android.content.Context;

import com.bignerdranch.android.cinemaquiz.common.AdHelper;
import com.bignerdranch.android.cinemaquiz.interfaces.QuestionContract;
import com.bignerdranch.android.cinemaquiz.model.Question;
import com.bignerdranch.android.cinemaquiz.model.QuestionModel;
import com.bignerdranch.android.cinemaquiz.repositories.SoundRep;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.List;

public class QuestionPresenter implements QuestionContract.Presenter {

    private QuestionContract.Model model;
    private QuestionContract.View view;

    private String categoryTitle;
    private int qId;
    private List<Question> questions;

    private boolean useSecondHint = false;
    private SoundRep soundRep;
    private AdHelper adHelper;

    public QuestionPresenter(Context context, QuestionContract.View view, String categoryTitle) {
        this.view = view;
        this.categoryTitle = categoryTitle;
        this.model = new QuestionModel(context, categoryTitle);
        this.soundRep = new SoundRep(context);
        initAdMob(context);

        this.qId = model.getQuestionId();
        this.questions = model.getAllQuestions();
        this.view.createGameField();
        this.view.updateContent(questions.get(qId), model.getCurrentPoints());
        this.view.setNextButtonBackground(categoryTitle);
    }

    private void initAdMob(Context context) {
        RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                view.enableBonusButton();
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                view.disableBonusButton();
                adHelper.loadRewardVideo();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                userBonusRewarded();
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        };
        adHelper = new AdHelper(context, rewardedVideoAdListener);
        adHelper.loadRewardVideo();
    }

    @Override
    public void clickBonus() {
        view.showDialogForBonus(() -> adHelper.showRewardVideo());
    }

    @Override
    public void clickFirstHint() {
        if (model.canUseFirstHint()) {
            soundRep.playSound(soundRep.getHintSound());
            view.hideNonPromptedAnswerCells();
            view.hideIncorrectGameCells();
            view.hideFirstHintButton();
            model.setPointsForFirstHint();
            view.updateHintTitle(model.getCurrentPoints());
        } else {
            soundRep.playSound(soundRep.getErrorSound());
            view.playWrongAnimationHintsTitle();
        }
    }

    @Override
    public void clickSecondHint() {
        if (model.canUseSecondHint()) {
            useSecondHint = !useSecondHint;
            view.changeSecondHintBackground(useSecondHint);
        } else {
            soundRep.playSound(soundRep.getErrorSound());
            view.playWrongAnimationHintsTitle();
        }
    }

    @Override
    public void secondHintUsed() {
        soundRep.playSound(soundRep.getButtonClickSound());
        useSecondHint = false;
        view.setDefaultImageSecondHint();
        model.setPointsForSecondHint();
        view.updateHintTitle(model.getCurrentPoints());
    }

    @Override
    public boolean isUsingSecondHint() {
        return useSecondHint;
    }

    @Override
    public void disableSecondHint() {
        if (useSecondHint) {
            useSecondHint = false;
            view.setDefaultImageSecondHint();
        }
    }

    @Override
    public void clickNextButton() {
        soundRep.playSound(soundRep.getSwishDown());
        view.updateContent(questions.get(qId), model.getCurrentPoints());
        view.hideNextButton();
    }

    @Override
    public void incrementId() {
        if (qId != 49) {
            qId++;
        } else {
            qId = 0;
            view.setCompleteTextNextButton(categoryTitle);
            model.setCategoryComplete();
        }
        model.setQuestionId(qId);
    }

    @Override
    public void userWon() {
        soundRep.playSound(soundRep.getSwishUp());
        incrementId();
        view.setPuzzleNextButton(categoryTitle, qId);
        checkForShowInterstitialAd();
        view.showNextButton();
        model.setPointsForWin();
    }

    @Override
    public void userLost() {
        soundRep.playSound(soundRep.getErrorSound());
        view.playWrongAnimationAnswer();
    }

    @Override
    public void clickAnswerCell() {
        soundRep.playSound(soundRep.getButtonClickSound());
    }

    @Override
    public void clickGameCell() {
        soundRep.playSound(soundRep.getButtonClickSound());
    }

    private void userBonusRewarded() {
        soundRep.playSound(soundRep.getPoints());
        model.setPointsForBonus();
        view.updateHintTitle(model.getCurrentPoints());
    }

    private void checkForShowInterstitialAd() {
        int adCounter = model.getAdCounter();
        if (adCounter >= 6) {
            adHelper.showInterstitialAd();
            model.setAdCounter(0);
        } else {
            model.setAdCounter(adCounter + 1);
        }
    }
}
