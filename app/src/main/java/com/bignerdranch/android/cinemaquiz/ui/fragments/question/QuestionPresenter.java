package com.bignerdranch.android.cinemaquiz.ui.fragments.question;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.common.BasePresenter;
import com.bignerdranch.android.cinemaquiz.common.SharedPrefHelper;
import com.bignerdranch.android.cinemaquiz.model.Points;
import com.bignerdranch.android.cinemaquiz.model.Question;
import com.bignerdranch.android.cinemaquiz.repositories.SoundRep;
import com.bignerdranch.android.cinemaquiz.utils.Utils;
import com.bignerdranch.android.cinemaquiz.view.cell.AnswerCell;
import com.bignerdranch.android.cinemaquiz.view.cell.GameCell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.bignerdranch.android.cinemaquiz.common.Constants.ALPHABET;
import static com.bignerdranch.android.cinemaquiz.common.Constants.ALPHABET_SIZE;
import static com.bignerdranch.android.cinemaquiz.common.Constants.CategoryTitles.CINEMA_GEEK;
import static com.bignerdranch.android.cinemaquiz.common.Constants.CategoryTitles.HORROR;
import static com.bignerdranch.android.cinemaquiz.common.Constants.CategoryTitles.PUZZLE;
import static com.bignerdranch.android.cinemaquiz.common.Constants.CategoryTitles.SALTWORT_2;
import static com.bignerdranch.android.cinemaquiz.common.Constants.CategoryTitles.SERIES;
import static com.bignerdranch.android.cinemaquiz.common.Constants.CategoryTitles.SUPER;
import static com.bignerdranch.android.cinemaquiz.common.Constants.MAX_CELLS_COUNT;

public class QuestionPresenter extends BasePresenter implements QuestionContract.Presenter {

    private QuestionContract.View view;
    private Question currentQuestion;
    private String categoryTitle;
    private Points points;
    private SoundRep soundRep;
    private SharedPrefHelper sharedPrefHelper;
    private boolean isSecondHintUsed = false;

    private List<GameCell> gameCells = new ArrayList<>();
    private List<AnswerCell> answerCells = new ArrayList<>();
    private final List<Integer> NUMBERS = new ArrayList<>(MAX_CELLS_COUNT);

    private int questionId;
    private int lastAnsweredQuestionId;

    public QuestionPresenter(QuestionContract.View view,
                             String categoryTitle,
                             SharedPrefHelper sharedPrefHelper,
                             SoundRep soundRep,
                             Question question,
                             Lifecycle lifecycle) {
        initNumb();
        lifecycle.addObserver(this);
        this.categoryTitle = categoryTitle;
        this.view = view;
        this.soundRep = soundRep;
        this.points = new Points(sharedPrefHelper);
        this.sharedPrefHelper = sharedPrefHelper;
        this.currentQuestion = question;
        this.questionId = question.getId();
        this.lastAnsweredQuestionId = sharedPrefHelper.getQuestionId(categoryTitle);
        updateNextButtonBackground(categoryTitle);
    }

    private void initNumb() {
        for (int i = 0; i < MAX_CELLS_COUNT; i++) {
            NUMBERS.add(i);
        }
    }

    @Override
    public void initContent() {
        view.updateHintTitle(points.getCurrentPoints());
        view.updateQuestionTitle(questionId);
        view.updateQuestionText(currentQuestion.getQuestionText());
        view.createAnswerField(currentQuestion.getAnswer());
        view.createGameField();
        initGameCellsWithAnswer(Utils.removeSpaces(currentQuestion.getAnswer()));
        if (questionId == lastAnsweredQuestionId) {
            view.showCurrentQuestionConfiguration();
        } else if (questionId < lastAnsweredQuestionId) {
            showAnswer();
            view.showPassedQuestionConfiguration();
        } else {
            view.showNotPassedQuestionConfiguration();
        }
        updateNextButtonBackground(categoryTitle);
    }

    @Override
    public void updateContent() {
        view.updateHintTitle(points.getCurrentPoints());
        lastAnsweredQuestionId = sharedPrefHelper.getQuestionId(categoryTitle);
        if (questionId == lastAnsweredQuestionId) {
            view.showCurrentQuestionConfiguration();
        } else if (questionId < lastAnsweredQuestionId) {
            showAnswer();
            view.showPassedQuestionConfiguration();
        } else {
            view.showNotPassedQuestionConfiguration();
        }
    }

    private void showAnswer() {
        for (AnswerCell answerCell : answerCells) {
            answerCell.showCorrectSymbol(false);
        }
    }

    @Override
    public void clickOnFirstHintButton() {
        view.setDefaultImageSecondHint();
        if (points.isFirstHintAvailable()) {
            soundRep.playSound(soundRep.getHintSound());
            for (AnswerCell answerCell : answerCells) {
                if (answerCell.isNotPrompted() && !answerCell.isEmpty()) {
                    answerCell.clear();
                    gameCells.get(answerCell.getGameCellPickedId()).showCell();
                }
            }
            for (GameCell gameCell : gameCells) {
                if (!gameCell.isRightSymbol()) gameCell.hideCell();
            }
            points.useFirstHint();
            view.updateHintTitle(points.getCurrentPoints());
            view.hideFirstHintButton();
        } else {
            soundRep.playSound(soundRep.getErrorSound());
            view.playAnimationWrongHintTitle();
        }
    }

    @Override
    public void clickOnBonusButton() {
        view.setDefaultImageSecondHint();
        view.showDialogForBonus();
    }

    @Override
    public void clickOnSecondHintButton() {
        if (points.checkSecondHint()) {
            isSecondHintUsed = !isSecondHintUsed;
            view.updateSecondHintBackground(isSecondHintUsed);
        } else {
            soundRep.playSound(soundRep.getErrorSound());
            view.playAnimationWrongHintTitle();
        }
    }

    @Override
    public void addAnswerCell(AnswerCell cell) {
        answerCells.add(cell);
    }

    @Override
    public void addGameCell(GameCell cell) {
        gameCells.add(cell);
    }

    @Override
    public void clickOnAnswerCell(int cellId) {
        AnswerCell cell = answerCells.get(cellId);
        if (isSecondHintUsed) {
            if (cell.isEmpty()) {
                soundRep.playSound(soundRep.getButtonClickSound());
                points.useSecondHint();
                view.updateHintTitle(points.getCurrentPoints());
                cell.showCorrectSymbol(true);
                hidePromptedGameCell(cell.getCorrectSymbol());
                checkForWin();
            }
            view.setDefaultImageSecondHint();
        } else if (!cell.isEmpty() && cell.isNotPrompted()) {
            soundRep.playSound(soundRep.getButtonClickSound());
            gameCells.get(cell.getGameCellPickedId()).showCell();
            cell.clear();
        }
    }

    @Override
    public void clickOnGameCell(int cellId) {
        GameCell cell = gameCells.get(cellId);
        soundRep.playSound(soundRep.getButtonClickSound());
        if (isSecondHintUsed) view.setDefaultImageSecondHint();
        for (AnswerCell answerCell : answerCells) {
            if (answerCell.isEmpty()) {
                answerCell.setSymbol(cell.getSymbol());
                answerCell.setGameCellPickedId(gameCells.indexOf(cell));
                cell.hideCell();
                checkForWin();
                return;
            }
        }
    }

    private void hidePromptedGameCell(String correctSymbol) {
        for (GameCell gameCell : gameCells) {
            if (gameCell.getSymbol().equals(correctSymbol) && gameCell.isRightSymbol() && gameCell.isVisible()) {
                gameCell.hideCell();
                return;
            }
        }
        hidePromptedAnswerCell(correctSymbol);
    }

    private void hidePromptedAnswerCell(String correctSymbol) {
        for (AnswerCell answerCell : answerCells) {
            GameCell gameCell = gameCells.get(answerCell.getGameCellPickedId());
            if (answerCell.getSymbol().equals(correctSymbol) && gameCell.isRightSymbol() && answerCell.isNotPrompted()) {
                answerCell.clear();
                gameCell.hideCell();
                return;
            }
        }
    }

    @Override
    public void clickOnNextButton() {
        soundRep.playSound(soundRep.getSwishDown());
        if (questionId == 49) view.closeQuestionFragment();
        else view.animationHideNextButton();
        view.hideNextButton();
        updateContent();
    }

    private void initGameCellsWithAnswer(@NonNull String answer) {
        if (answer.length() > MAX_CELLS_COUNT) {
            throw new IllegalArgumentException("word length it's too long");
        }
        initGameCellsWithRandomChars();
        setAnswerInGameField(answer.toUpperCase());
    }

    private void initGameCellsWithRandomChars() {
        Random mRandom = new Random();
        for (GameCell gameCell : gameCells) {
            gameCell.setSymbol(String.valueOf(ALPHABET.charAt(mRandom.nextInt(ALPHABET_SIZE))));
            gameCell.setRightSymbol(false);
        }
    }

    private void setAnswerInGameField(@NonNull String answer) {
        Collections.shuffle(NUMBERS);
        for (int i = 0; i < answer.length(); i++) {
            gameCells.get((NUMBERS.get(i))).setRightSymbol(true);
            gameCells.get((NUMBERS.get(i))).setSymbol(String.valueOf(answer.charAt(i)));
        }
    }

    private void checkForWin() {
        if (hasEmptyAnswerFields()) return;
        if (hasIncorrectAnswerSymbol()) {
            soundRep.playSound(soundRep.getErrorSound());
            view.playAnimationWrongAnswer(answerCells);
            return;
        }

        checkForPuzzleNextButton(categoryTitle, questionId);
        incrementId();
        checkForShowInterstitialAd();
        sharedPrefHelper.setQuestionId(categoryTitle, questionId + 1);
        soundRep.playSound(soundRep.getSwishUp());
        view.animationShowNextButton();
        points.increasePoints();
    }

    private boolean hasEmptyAnswerFields() {
        for (AnswerCell answerCell : answerCells) {
            if (answerCell.isEmpty()) return true;
        }
        return false;
    }

    private boolean hasIncorrectAnswerSymbol() {
        for (AnswerCell answerCell : answerCells) {
            if (!answerCell.isCorrectSymbol()) return true;
        }
        return false;
    }

    private void incrementId() {
        if (questionId == 49) {
            view.setNextButtonCategoryCompleteText(categoryTitle);
            sharedPrefHelper.setCategoryComplete(categoryTitle);
        }
    }

    private void checkForShowInterstitialAd() {
        int mAdCounter = sharedPrefHelper.getAdCounter();
        if (mAdCounter >= 6) {
            view.showInterstitialAd();
            sharedPrefHelper.setAdCounter(0);
        } else {
            sharedPrefHelper.setAdCounter(mAdCounter + 1);
        }
    }

    @Override
    public void bonusUsed() {
        points.useBonusHint();
        view.updateHintTitle(points.getCurrentPoints());
        soundRep.playSound(soundRep.getPoints());
    }

    @Override
    public void disableUsedSecondHint() {
        isSecondHintUsed = false;
    }

    private void updateNextButtonBackground(String categoryTitle) {
        switch (categoryTitle) {
            case HORROR:
                view.setNextButtonBackground(R.drawable.smiley_blood_bg);
                break;
            case PUZZLE:
                view.setNextButtonBackground(R.drawable.smiley_puzzle);
                break;
            case CINEMA_GEEK:
                view.setNextButtonBackground(R.drawable.smiley_geek_bg);
                break;
            case SUPER:
                view.setNextButtonBackground(R.drawable.smiley_haha_bg);
                break;
            default:
                view.setNextButtonBackground(R.drawable.smiley_bg);
        }
    }

    private void checkForPuzzleNextButton(String categoryTitle, int questionId) {
        switch (categoryTitle) {
            case SALTWORT_2:
                if (questionId == 32)
                    view.setNextButtonBackground(R.drawable.smiley_evolution_bg);
                break;
            case SUPER:
                if (questionId == 27)
                    view.setNextButtonBackground(R.drawable.smiley_false_god_bg);
                break;
            case SERIES:
                if (questionId == 9)
                    view.setNextButtonBackground(R.drawable.smiley_missme_bg);
        }
    }

    @Override
    public int getQuestionId() {
        return questionId;
    }
}
