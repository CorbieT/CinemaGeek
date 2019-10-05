package com.bignerdranch.android.cinemaquiz.interfaces;

import com.bignerdranch.android.cinemaquiz.model.Question;

import java.util.List;

public interface QuestionContract {
    interface Model {
        List<Question> getAllQuestions();
        int getQuestionId();
        void setQuestionId(int qId);
        int getAdCounter();
        void setAdCounter(int count);
        boolean canUseFirstHint();
        boolean canUseSecondHint();
        void setPointsForFirstHint();
        void setPointsForSecondHint();
        void setPointsForBonus();
        void setPointsForWin();
        int getCurrentPoints();
        void setCategoryComplete();
    }

    interface View {
        void updateContent(Question question, int currentPoints);
        void showNextButton();
        void hideNextButton();
        void playWrongAnimationAnswer();
        void playWrongAnimationHintsTitle();
        void setDefaultImageSecondHint();
        void setCompleteTextNextButton(String categoryTitle);
        void updateHintTitle(int currentPoints);
        void createGameField();
        void setPuzzleNextButton(String category, int qId);
        void setNextButtonBackground(String category);
        void showDialogForBonus(Function positiveClick);
        void changeSecondHintBackground(boolean isUse);
        void enableBonusButton();
        void disableBonusButton();
        void hideNonPromptedAnswerCells();
        void hideIncorrectGameCells();
        void hideFirstHintButton();
    }

    interface Presenter {
        void clickBonus();
        void clickFirstHint();
        void clickSecondHint();
        void clickNextButton();
        void incrementId();
        void userWon();
        void userLost();
        void clickAnswerCell();
        void clickGameCell();
        void secondHintUsed();
        boolean isUsingSecondHint();
        void disableSecondHint();
    }
}
