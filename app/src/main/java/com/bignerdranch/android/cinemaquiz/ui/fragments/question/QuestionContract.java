package com.bignerdranch.android.cinemaquiz.ui.fragments.question;

import com.bignerdranch.android.cinemaquiz.view.cell.AnswerCell;
import com.bignerdranch.android.cinemaquiz.view.cell.GameCell;

import java.util.List;

public interface QuestionContract {
    interface View {
        void setNextButtonBackground(int drawableId);
        void setDefaultImageSecondHint();
        void updateHintTitle(int currentPoints);
        void hideFirstHintButton();
        void playAnimationWrongHintTitle();
        void updateSecondHintBackground(boolean criteriaUpdate);
        void animationShowNextButton();
        void animationHideNextButton();
        void hideNextButton();
        void showDialogForBonus();
        void createAnswerField(String answer);
        void createGameField();
        void updateQuestionTitle(int questionId);
        void updateQuestionText(String text);
        void playAnimationWrongAnswer(List<AnswerCell> answerCells);
        void setNextButtonCategoryCompleteText(String category);
        void showInterstitialAd();
        void closeQuestionFragment();
        void showPassedQuestionConfiguration();
        void showNotPassedQuestionConfiguration();
        void showCurrentQuestionConfiguration();
    }

    interface Presenter {
        void clickOnFirstHintButton();
        void clickOnBonusButton();
        void clickOnSecondHintButton();
        void clickOnAnswerCell(int cellId);
        void clickOnGameCell(int cellId);
        void clickOnNextButton();
        void bonusUsed();
        void disableUsedSecondHint();
        void addAnswerCell(AnswerCell cell);
        void addGameCell(GameCell cell);
        void initContent();
        void updateContent();
        int getQuestionId();
    }
}
