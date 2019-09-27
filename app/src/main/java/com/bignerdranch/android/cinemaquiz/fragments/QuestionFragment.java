package com.bignerdranch.android.cinemaquiz.fragments;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.common.AdHelper;
import com.bignerdranch.android.cinemaquiz.common.SharedPrefHelper;
import com.bignerdranch.android.cinemaquiz.common.XmlPullParserHelper;
import com.bignerdranch.android.cinemaquiz.fragments.dialogs.BonusDialogFragment;
import com.bignerdranch.android.cinemaquiz.model.AnswerCell;
import com.bignerdranch.android.cinemaquiz.model.GameCell;
import com.bignerdranch.android.cinemaquiz.model.Points;
import com.bignerdranch.android.cinemaquiz.model.Question;
import com.bignerdranch.android.cinemaquiz.repositories.SoundRep;
import com.bignerdranch.android.cinemaquiz.utils.Utils;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.bignerdranch.android.cinemaquiz.common.Constants.ALPHABET;
import static com.bignerdranch.android.cinemaquiz.common.Constants.ALPHABET_SIZE;
import static com.bignerdranch.android.cinemaquiz.common.Constants.MAX_CELLS_COUNT;

public class QuestionFragment extends BaseFragment {

    @BindView(R.id.hint_count)
    TextView hintTitle;

    @BindView(R.id.question_text)
    TextView questionText;

    @BindView(R.id.question_number)
    TextView questionTitle;

    @BindView(R.id.next_button)
    Button nextButton;

    @BindView(R.id.hint_1)
    Button buttonHint1;

    @BindView(R.id.hint_2)
    Button buttonHint2;

    @BindView(R.id.hint_bonus)
    Button buttonBonus;

    @BindView(R.id.scroll_view)
    ScrollView scrollView;

    @BindView(R.id.answer_container)
    LinearLayout answerContainer;

    @BindView(R.id.game_cell_container)
    LinearLayout gameCellContainer;

    private Unbinder unbinder;

    private static final String CATEGORY_TAG = "CATEGORY_TITLE";

    private static final int WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT;
    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
    private LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, 1.0f);
    private LinearLayout.LayoutParams cellLayoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1.0f);

    private List<GameCell> gameCells = new ArrayList<>();
    private List<AnswerCell> answerCells = new ArrayList<>();
    private List<Question> questions = new ArrayList<>();
    private final List<Integer> NUMBERS = new ArrayList<>(MAX_CELLS_COUNT);

    private int mQId = 0;
    private Points points;
    private String categoryTitle = "";
    private boolean useSecondHint = false;
    private SoundRep soundRep;
    private AdHelper adHelper;
    private SharedPrefHelper sharedPrefHelper;

    public static QuestionFragment newInstance(String category) {
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY_TAG, category);
        QuestionFragment fragment = new QuestionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadBundle();
        sharedPrefHelper = new SharedPrefHelper(Objects.requireNonNull(getActivity()));
        points = new Points(sharedPrefHelper);
        mQId = sharedPrefHelper.getQuestionId(categoryTitle);
        questions = XmlPullParserHelper.getQuestionsFromXMLByCategoryTitle(getActivity(), categoryTitle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.question_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        setViewsOnClickListeners(view);
        initViewComponents();
        updateContent();
        setNextButtonBackground();
        return view;
    }

    private void loadBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            categoryTitle = bundle.getString(CATEGORY_TAG);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initViewComponents() {
//        initAdMob();
        soundRep = new SoundRep(getActivity());
        createGameField();
        initNumb();
    }

    @OnClick({R.id.hint_bonus, R.id.hint_1, R.id.hint_2, R.id.next_button})
    public void setViewsOnClickListeners(View view) {
        switch (view.getId()) {
            case R.id.hint_bonus:
                if (useSecondHint) setDefaultImageSecondHint();
                showDialogForBonus();
                break;
            case R.id.hint_1:
                useHint1();
                break;
            case R.id.hint_2:
                useHint2();
                break;
            case R.id.next_button:
                animationHideNextButton();
                break;
            default:
                break;
        }
    }

    private void setNextButtonBackground() {
        switch (categoryTitle) {
            case "УЖАСЫ":
                nextButton.setBackgroundResource(R.drawable.smiley_blood_bg);
                break;
            case "ГОЛОВОЛОМКИ":
                nextButton.setBackgroundResource(R.drawable.smiley_puzzle);
                break;
            case "КИНОГИК":
                nextButton.setBackgroundResource(R.drawable.smiley_geek_bg);
                break;
            case "СУПЕР":
                nextButton.setBackgroundResource(R.drawable.smiley_haha_bg);
                break;
            default:
                nextButton.setBackgroundResource(R.drawable.smiley_bg);
        }
    }

    private void setPuzzleNextButton() {
        switch (categoryTitle) {
            case "СОЛЯНКА-2":
                if (mQId == 32) nextButton.setBackgroundResource(R.drawable.smiley_evolution_bg);
                break;
            case "СУПЕР":
                if (mQId == 27) nextButton.setBackgroundResource(R.drawable.smiley_false_god_bg);
                break;
            case "СЕРИАЛЫ":
                if (mQId == 9) nextButton.setBackgroundResource(R.drawable.smiley_missme_bg);
        }
    }

    private void updateContent() {
        /**
         * imQId + 1 - because indexing start from 0
         */
        questionTitle.setText(getString(R.string.question_title, mQId + 1));
        hintTitle.setText(getString(R.string.hints_title, points.getCurrentPoints()));
        questionText.setText(questions.get(mQId).getQuestionText());
        createAnswerField(questions.get(mQId).getAnswer().toUpperCase());
        showCellsGameField();
        initGameCellsWithAnswer(Utils.removeSpaces(questions.get(mQId).getAnswer()));
        scrollView.scrollTo(0, 0);
        buttonHint1.setVisibility(View.VISIBLE);
        buttonHint2.setVisibility(View.VISIBLE);
    }

    private void initGameCellsWithAnswer(@NonNull String answer) {
        if (answer.length() > MAX_CELLS_COUNT) {
            throw new IllegalArgumentException("word length it's too long");
        }
        initGameCellsWithRandomChars();
        setAnswerInGameField(answer);
    }

    private void initGameCellsWithRandomChars() {
        Random mRandom = new Random();
        for (GameCell gameCell : gameCells) {
            gameCell.setText(String.valueOf(ALPHABET.charAt(mRandom.nextInt(ALPHABET_SIZE))));
            gameCell.setRightSymbol(false);
        }
    }

    private void setAnswerInGameField(@NonNull String answer) {
        Collections.shuffle(NUMBERS);
        for (int i = 0; i < answer.length(); i++) {
            gameCells.get((NUMBERS.get(i))).setRightSymbol(true);
            gameCells.get((NUMBERS.get(i))).setText(String.valueOf(answer.charAt(i)));
        }
    }

    private void showCellsGameField() {
        for (GameCell gameCell : gameCells) {
            gameCell.showCell();
        }
    }

    private void createAnswerField(String answer) {
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1.0f);
        linearLayoutParams.setMargins(0, 0, 0, 20);
        linearLayoutParams.gravity = Gravity.CENTER;
        cellLayoutParams.setMargins(0, 0, 10, 0);
        answerCells.clear();
        answerContainer.removeAllViewsInLayout();

        String[] answerWords = Utils.getSplitBySpacesString(answer);
        for (String answerWord : answerWords) {
            //FIXME fix this shit below
            if (answerWord.length() > 10) {
                String splittedWord = answerWord.substring(0, 9);
                for (int i = 0; i < 2; i++) {
                    LinearLayout linearLayout = new LinearLayout(getActivity());
                    answerContainer.addView(linearLayout, linearLayoutParams);
                    for (int j = 0; j < splittedWord.length(); j++) {
                        linearLayout.addView(createAnswerCell(String.valueOf(splittedWord.charAt(j))), cellLayoutParams);
                    }
                    if (i == 0) { linearLayout.addView(createDashView(), cellLayoutParams); }
                    splittedWord = answerWord.substring(9);
                }
                continue;
            }
            LinearLayout linearLayout = new LinearLayout(getActivity());
            answerContainer.addView(linearLayout, linearLayoutParams);
            for (int i = 0; i < answerWord.length(); i++) {
                linearLayout.addView(createAnswerCell(String.valueOf(answerWord.charAt(i))), cellLayoutParams);
            }
        }
    }

    private AnswerCell createAnswerCell(String correctSymbol) {
        final AnswerCell answerCell = new AnswerCell(getActivity(), String.valueOf(correctSymbol));
        answerCell.setOnClickListener(view -> {
            if (useSecondHint) {
                if (answerCell.isEmpty()) {
                    soundRep.playSound(soundRep.getButtonClickSound());
                    points.useSecondHint();
                    hintTitle.setText(getString(R.string.hints_title, points.getCurrentPoints()));
                    hidePromptedGameCell(answerCell.getCorrectSymbol());
                    answerCell.showCorrectSymbol();
                    checkForWin();
                }
                setDefaultImageSecondHint();
                return;
            }
            if (!answerCell.isEmpty() && !answerCell.isPrompted()) {
                soundRep.playSound(soundRep.getButtonClickSound());
                gameCells.get(answerCell.getGameCellPickedId()).showCell();
                answerCell.clearAnswerCell();
            }
        });
        answerCells.add(answerCell);
        return answerCell;
    }

    private TextView createDashView() {
        TextView textView = new TextView(getActivity());
        textView.setVisibility(View.VISIBLE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setMinEms(1);
        textView.setTextColor(getResources().getColor(R.color.textColor));
        textView.setText("—");
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTypeface(null, Typeface.BOLD);
        return textView;
    }

    private void createGameField() {
        gameCellContainer.removeAllViews();
        linearLayoutParams.setMargins(0, 0, 0, 15);
        cellLayoutParams.setMargins(3, 3, 3, 3);
        for (int i = 0; i < 3; i++) {
            LinearLayout linearLayout = new LinearLayout(getActivity());
            gameCellContainer.addView(linearLayout, linearLayoutParams);
            for (int j = 0; j < 6; j++) {
                linearLayout.addView(createGameCell());
            }
        }
//        createRulesButtons();
    }

    private GameCell createGameCell() {
        final GameCell gameCell = new GameCell(getActivity());
        gameCells.add(gameCell);
        gameCell.setLayoutParams(cellLayoutParams);
        gameCell.setOnClickListener(view -> {
            soundRep.playSound(soundRep.getButtonClickSound());
            if (useSecondHint) setDefaultImageSecondHint();
            for (AnswerCell answerCell : answerCells) {
                if (answerCell.isEmpty()) {
                    answerCell.setText(gameCell.getText());
                    answerCell.setGameCellPickedId(gameCells.indexOf(gameCell));
                    gameCell.hideCell();
                    checkForWin();
                    return;
                }
            }
        });
        return gameCell;
    }

    private void createRulesButtons() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1.0f);
        LinearLayout ruleButtonsContainer = new LinearLayout(getActivity());
        for (int i = 0; i < 3; i++) {
            if (i == 1) {
                layoutParams.weight = 1.0f;
            } else {
                layoutParams.weight = 3.0f;
            }
            ruleButtonsContainer.addView(new Button(getActivity()), layoutParams);
        }
        gameCellContainer.addView(ruleButtonsContainer);
    }

    private void checkForWin() {
        if (hasEmptyAnswerFields()) return;
        if (hasIncorrectAnswerSymbol()) {
            playWrongAnimationAnswer();
            return;
        }

        setPuzzleNextButton();
        incrementId();
        checkForShowInterstitialAd();
        sharedPrefHelper.setQuestionId(categoryTitle, mQId);
        animationShowNextButton();
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

    private void playWrongAnimationAnswer() {
        for (AnswerCell animationCell : answerCells) {
            animationWrong(animationCell);
        }
    }

    private void useHint1() {
        if (useSecondHint) setDefaultImageSecondHint();
        if (points.checkFirstHint()) {
            soundRep.playSound(soundRep.getHintSound());
            for (AnswerCell answerCell : answerCells) {
                answerCell.clearAnswerCell();
            }
            for (GameCell gameCell : gameCells) {
                if (!gameCell.isRightSymbol()) gameCell.hideCell();
            }
            points.useFirstHint();
            hintTitle.setText(getString(R.string.hints_title, points.getCurrentPoints()));
            hideFirstHintButton();
        } else {
            animationWrong(hintTitle);
        }
    }

    private void hideFirstHintButton() {
        buttonHint1.setVisibility(View.INVISIBLE);
    }

    private void useHint2() {
        if (points.checkSecondHint()) {
            useSecondHint = !useSecondHint;
            if (useSecondHint) buttonHint2.setBackgroundResource(R.drawable.hint_button_active);
            else buttonHint2.setBackgroundResource(R.drawable.hint_button);
        } else {
            animationWrong(hintTitle);
        }
    }

    private void bonusUsed() {
        points.useBonusHint();
        soundRep.playSound(soundRep.getPoints());
        hintTitle.setText(getString(R.string.hints_title, points.getCurrentPoints()));
    }

    private void animationShowNextButton() {
        nextButton.setVisibility(View.VISIBLE);
        soundRep.playSound(soundRep.getSwishUp());
        float butStart = nextButton.getTop() - nextButton.getHeight();
        float butEnd = nextButton.getTop();

        ObjectAnimator buttonAnimator = ObjectAnimator.ofFloat(nextButton, "y", butStart, butEnd)
                .setDuration(200);
        buttonAnimator.start();
        nextButton.setClickable(true);
    }

    private void animationHideNextButton() {
        updateContent();
        soundRep.playSound(soundRep.getSwishDown());
        nextButton.setClickable(false);
        float butStart = nextButton.getTop();
        float butEnd = nextButton.getBottom();

        ObjectAnimator buttonAnimator = ObjectAnimator.ofFloat(nextButton, "y", butStart, butEnd)
                .setDuration(200);
        buttonAnimator.start();
    }

    private void animationWrong(TextView textView) {
        soundRep.playSound(soundRep.getErrorSound());
        ObjectAnimator wrongAnimator = ObjectAnimator
                .ofInt(textView, "textColor", textView.getCurrentTextColor(), ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.wrongAnswer))
                .setDuration(200);
        wrongAnimator.setRepeatCount(1);
        wrongAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator normalAnimator = ObjectAnimator
                .ofInt(textView, "textColor", ContextCompat.getColor(getContext(), R.color.wrongAnswer), textView.getCurrentTextColor())
                .setDuration(200);
        normalAnimator.setRepeatCount(1);
        normalAnimator.setEvaluator(new ArgbEvaluator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(wrongAnimator)
                .before(normalAnimator);
        animatorSet.start();
    }

    private void hidePromptedGameCell(String correctSymbol) {
        for (GameCell gameCell : gameCells) {
            if (gameCell.getText().equals(correctSymbol) && gameCell.isRightSymbol() && gameCell.isVisible()) {
                gameCell.hideCell();
                return;
            }
        }
        hidePromptedAnswerCell(correctSymbol);
    }

    private void hidePromptedAnswerCell(String correctSymbol) {
        for (AnswerCell answerCell : answerCells) {
            GameCell gameCell = gameCells.get(answerCell.getGameCellPickedId());
            if (answerCell.getText().equals(correctSymbol) && gameCell.isRightSymbol() && !answerCell.isPrompted()) {
                answerCell.clearAnswerCell();
                gameCell.hideCell();
                return;
            }
        }
    }

    private void setDefaultImageSecondHint() {
        useSecondHint = false;
        buttonHint2.setBackgroundResource(R.drawable.hint_button);
    }

    private void initNumb() {
        for (int i = 0; i < MAX_CELLS_COUNT; i++) {
            NUMBERS.add(i);
        }
    }

    private void checkForShowInterstitialAd() {
        int mAdCounter = sharedPrefHelper.getAdCounter();
        if (mAdCounter >= 6) {
            adHelper.showInterstitialAd();
            sharedPrefHelper.setAdCounter(0);
        } else {
            sharedPrefHelper.setAdCounter(mAdCounter + 1);
        }
    }

    private void initAdMob() {
        RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                buttonBonus.setEnabled(true);
                buttonBonus.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.enable_bonus_button));
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                buttonBonus.setEnabled(false);
                buttonBonus.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.disabled_bonus_button));
                adHelper.loadRewardVideo();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                bonusUsed();
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
        adHelper = new AdHelper(getActivity(), rewardedVideoAdListener);
        adHelper.loadRewardVideo();
    }

    private void showDialogForBonus() {
        new BonusDialogFragment(() -> adHelper.showRewardVideo())
                .show(Objects.requireNonNull(getFragmentManager()), null);
    }

    private void incrementId() {
        if (mQId != 49) {
            mQId++;
        } else {
            mQId = 0;
            nextButton.setText(getString(R.string.end_game_text, categoryTitle));
            sharedPrefHelper.setCategoryComplete(categoryTitle);
        }
    }

}
