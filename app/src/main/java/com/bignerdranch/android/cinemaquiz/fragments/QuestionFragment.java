package com.bignerdranch.android.cinemaquiz.fragments;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.common.SharedPrefHelper;
import com.bignerdranch.android.cinemaquiz.common.XmlPullParserHelper;
import com.bignerdranch.android.cinemaquiz.model.AnswerCell;
import com.bignerdranch.android.cinemaquiz.model.GameCell;
import com.bignerdranch.android.cinemaquiz.model.Points;
import com.bignerdranch.android.cinemaquiz.model.Question;
import com.bignerdranch.android.cinemaquiz.model.SoundRep;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
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
    TextView mHintTitle;

    @BindView(R.id.question_text)
    TextView mQuestionText;

    @BindView(R.id.question_number)
    TextView mQuestionTitle;

    @BindView(R.id.next_button)
    Button mNextButton;

    @BindView(R.id.hint_1)
    Button mButtonHint1;

    @BindView(R.id.hint_2)
    Button mButtonHint2;

    @BindView(R.id.hint_bonus)
    Button mButtonBonus;

    @BindView(R.id.scroll_view)
    ScrollView mScrollView;

    @BindView(R.id.answer_container)
    LinearLayout mAnswerContainer;

    @BindView(R.id.first_row_container)
    LinearLayout mFirstRowContainer;

    @BindView(R.id.second_row_container)
    LinearLayout mSecondRowContainer;

    @BindView(R.id.third_row_container)
    LinearLayout mThirdRowContainer;

    private Unbinder unbinder;

    private static final String CATEGORY_TAG = "CATEGORY_TITLE";

    private static final int WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT;

    private List<GameCell> mGameCells = new ArrayList<>();
    private List<AnswerCell> mAnswerCells = new ArrayList<>();
    private List<Question> mQuestions = new ArrayList<>();
    private final List<Integer> NUMBERS = new ArrayList<>(MAX_CELLS_COUNT);

    private int mQId = 0;
    private Points mPoints;
    private String categoryTitle = "";
    private boolean useSecondHint = false;
    private boolean bonusUsed = false;
    private SoundRep mSoundRep;
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;
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
        mPoints = new Points(sharedPrefHelper);
        mQId = sharedPrefHelper.getQuestionId(categoryTitle);
        mQuestions = XmlPullParserHelper.getQuestionsFromXMLByCategoryTitle(getActivity(), categoryTitle);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(getActivity());
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(getActivity());
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(getActivity());
        super.onDestroy();
    }

    private void loadBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            categoryTitle = bundle.getString(CATEGORY_TAG);
        }
    }

    private void initViewComponents() {
        initRewardedVideo();
        mSoundRep = new SoundRep(getActivity());
        setInterstitialAd();
        initGameField();
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
                mNextButton.setBackgroundResource(R.drawable.smiley_blood_bg);
                break;
            case "ГОЛОВОЛОМКИ":
                mNextButton.setBackgroundResource(R.drawable.smiley_puzzle);
                break;
            case "КИНОГИК":
                mNextButton.setBackgroundResource(R.drawable.smiley_geek_bg);
                break;
            case "СУПЕР":
                mNextButton.setBackgroundResource(R.drawable.smiley_haha_bg);
                break;
            default:
                mNextButton.setBackgroundResource(R.drawable.smiley_bg);
        }
    }

    private void setPuzzleNextButton() {
        switch (categoryTitle) {
            case "СОЛЯНКА-2":
                if (mQId == 32) mNextButton.setBackgroundResource(R.drawable.smiley_evolution_bg);
                break;
            case "СУПЕР":
                if (mQId == 27) mNextButton.setBackgroundResource(R.drawable.smiley_false_god_bg);
                break;
            case "СЕРИАЛЫ":
                if (mQId == 9) mNextButton.setBackgroundResource(R.drawable.smiley_missme_bg);
        }
    }

    private void updateContent() {
        /**
         * imQId + 1 - because indexing start from 0
         */
        mQuestionTitle.setText(getString(R.string.question_title, mQId + 1));
        mHintTitle.setText(getString(R.string.hints_title, mPoints.getCurrentPoints()));
        mQuestionText.setText(mQuestions.get(mQId).getQuestionText());
        createAnswerField(mQuestions.get(mQId).getAnswer().toUpperCase());
        showCellsGameField();
        initGameCellsWithAnswer(removeSpaces(mQuestions.get(mQId).getAnswer()));
        mScrollView.scrollTo(0, 0);
        mButtonHint1.setVisibility(View.VISIBLE);
        mButtonHint2.setVisibility(View.VISIBLE);
    }

    private void initGameCellsWithAnswer(@NonNull String answer) {
        if (answer.length() > MAX_CELLS_COUNT) {
            throw new IllegalArgumentException("word length it's too long");
        }
        initGameCellsWithRandomChars();
        setAnswerInGameField(answer);
    }

    //FIXME Collections.shuffle() refactor for mGameCells
    private void initGameCellsWithRandomChars() {
        Random mRandom = new Random();
        for (GameCell gameCell : mGameCells) {
            gameCell.setGameSymbol(ALPHABET.charAt(mRandom.nextInt(ALPHABET_SIZE)));
            gameCell.setRightSymbol(false);
        }
    }

    private void setAnswerInGameField(@NonNull String answer) {
        Collections.shuffle(NUMBERS);
        for (int i = 0; i < answer.length(); i++) {
            mGameCells.get((NUMBERS.get(i))).setRightSymbol(true);
            mGameCells.get((NUMBERS.get(i))).setGameSymbol(answer.charAt(i));
        }
    }

    private void showCellsGameField() {
        for (GameCell gameCell : mGameCells) {
            gameCell.showCell();
        }
    }

    @NonNull
    private String removeSpaces(String word) {
        return word.replaceAll("\\s", "").toUpperCase();
    }

    private void initGameField() {
        mFirstRowContainer.removeAllViews();
        mSecondRowContainer.removeAllViews();
        mThirdRowContainer.removeAllViews();

        setRowContainer(mFirstRowContainer);
        setRowContainer(mSecondRowContainer);
        setRowContainer(mThirdRowContainer);
    }

    private void setRowContainer(LinearLayout linearLayout) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1.0f);
        layoutParams.setMargins(3, 3, 3, 3);
        for (int i = 0; i < 6; i++) {
            final GameCell gameCell = new GameCell(getActivity());
            gameCell.setLayoutParams(layoutParams);
            linearLayout.addView(gameCell);
            mGameCells.add(gameCell);
            gameCell.setOnClickListener(view -> {
                mSoundRep.playSound(mSoundRep.getButtonClickSound());
                if (useSecondHint) setDefaultImageSecondHint();
                for (AnswerCell answerCell : mAnswerCells) {
                    if (answerCell.isEmpty() && !gameCell.isClicked()) {
                        answerCell.setAnswerSymbol(gameCell);
                        gameCell.hideCell();
                        gameCell.setClicked(true);
                        checkForWin();
                        return;
                    }
                }
            });
        }
    }

    private void createAnswerField(String answer) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        if (answer.length() > 13) layoutParams.setMargins(0, 0, 5, 0);
        else layoutParams.setMargins(0, 0, 10, 0);

        mAnswerCells.clear();
        mAnswerContainer.removeAllViewsInLayout();

        for (int i = 0; i < answer.length(); i++) {
            if (answer.charAt(i) != ' ') {
                final AnswerCell answerCell = new AnswerCell(getActivity(), answer.charAt(i), answer.length());
                mAnswerContainer.addView(answerCell, layoutParams);
                mAnswerCells.add(answerCell);
                answerCell.setOnClickListener(view -> {
                    if (useSecondHint) {
                        if (answerCell.isEmpty()) {
                            mSoundRep.playSound(mSoundRep.getButtonClickSound());
                            mPoints.useSecondHint();
                            mHintTitle.setText(getString(R.string.hints_title, mPoints.getCurrentPoints()));
                            hidePickedCell(answerCell.getCorrectSymbol());
                            answerCell.showCorrectSymbol();
                            checkForWin();
                        }
                        setDefaultImageSecondHint();
                        return;
                    }
                    if (!answerCell.isEmpty() && (answerCell.getGameCell() != null)) {
                        mSoundRep.playSound(mSoundRep.getButtonClickSound());
                        answerCell.clearAnswerCell();
                    }
                });
            } else {
                TextView emptyTextView = new TextView(getActivity());
                emptyTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 5);
                emptyTextView.setMinEms(1);
                emptyTextView.setVisibility(View.INVISIBLE);
                mAnswerContainer.addView(emptyTextView, layoutParams);
            }
        }
    }

    private void checkForWin() {
        for (AnswerCell answerCell : mAnswerCells) {
            if (answerCell.isEmpty()) return;
        }

        for (AnswerCell answerCell : mAnswerCells) {
            if (!answerCell.compareAnswerSymbols()) {
                for (AnswerCell animationCell : mAnswerCells) {
                    animationWrong(animationCell);
                }
                return;
            }
        }

        setPuzzleNextButton();
        incrementId();
        showInterstitialAd();
        sharedPrefHelper.setQuestionId(categoryTitle, mQId);
        animationShowNextButton();
        mPoints.increasePoints();
    }

    private void useHint1() {
        if (useSecondHint) setDefaultImageSecondHint();
        if (mPoints.checkFirstHint()) {
            mSoundRep.playSound(mSoundRep.getHintSound());
            for (AnswerCell answerCell : mAnswerCells) {
                answerCell.clearAnswerCell();
            }
            for (GameCell gameCell : mGameCells) {
                if (!gameCell.isRightSymbol()) gameCell.hideCell();
            }
            mPoints.useFirstHint();
            mHintTitle.setText(getString(R.string.hints_title, mPoints.getCurrentPoints()));
            blockFirstHint();
        } else {
            animationWrong(mHintTitle);
        }
    }

    private void useHint2() {
        if (mPoints.checkSecondHint()) {
            useSecondHint = !useSecondHint;
            if (useSecondHint) mButtonHint2.setBackgroundResource(R.drawable.hint_button_active);
            else mButtonHint2.setBackgroundResource(R.drawable.hint_button);
        } else {
            animationWrong(mHintTitle);
        }
    }

    private void useBonus() {
        mSoundRep.playSound(mSoundRep.getPoints());
        mHintTitle.setText(getString(R.string.hints_title, mPoints.getCurrentPoints()));
        bonusUsed = false;
    }

    private void blockFirstHint() {
        mButtonHint1.setVisibility(View.INVISIBLE);
    }

    private void animationShowNextButton() {
        mNextButton.setVisibility(View.VISIBLE);
        mSoundRep.playSound(mSoundRep.getSwishUp());
        float butStart = mNextButton.getTop() - mNextButton.getHeight();
        float butEnd = mNextButton.getTop();

        ObjectAnimator buttonAnimator = ObjectAnimator.ofFloat(mNextButton, "y", butStart, butEnd)
                .setDuration(200);
        buttonAnimator.start();
        mNextButton.setClickable(true);
    }

    private void animationHideNextButton() {
        updateContent();
        mSoundRep.playSound(mSoundRep.getSwishDown());
        mNextButton.setClickable(false);
        float butStart = mNextButton.getTop();
        float butEnd = mNextButton.getBottom();

        ObjectAnimator buttonAnimator = ObjectAnimator.ofFloat(mNextButton, "y", butStart, butEnd)
                .setDuration(200);
        buttonAnimator.start();
    }

    private void animationWrong(TextView textView) {
        mSoundRep.playSound(mSoundRep.getErrorSound());
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

    private void hidePickedCell(char correctSymbol) {
        boolean temp = true;
        for (GameCell gameCell : mGameCells) {
            if (gameCell.getGameSymbol() == correctSymbol && !gameCell.isClicked() && gameCell.isRightSymbol()) {
                gameCell.hideCell();
                gameCell.setClicked(true);
                temp = false;
                break;
            }
        }
        if (temp) {
            for (AnswerCell answerCell : mAnswerCells) {
                if (answerCell.getGameCell() != null && answerCell.getAnswerSymbol() == correctSymbol && answerCell.getGameCell().isRightSymbol()) {
                    GameCell gameCell = answerCell.getGameCell();
                    answerCell.clearAnswerCell();
                    gameCell.hideCell();
                    gameCell.setClicked(true);
                    break;
                }
            }
        }
    }

    private void setDefaultImageSecondHint() {
        useSecondHint = false;
        mButtonHint2.setBackgroundResource(R.drawable.hint_button);
    }

    private void initNumb() {
        for (int i = 0; i < MAX_CELLS_COUNT; i++) {
            NUMBERS.add(i);
        }
    }

    private void setInterstitialAd() {
        mInterstitialAd = new InterstitialAd(Objects.requireNonNull(getActivity()));
        mInterstitialAd.setAdUnitId(getString(R.string.test_interstitial_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    private void showInterstitialAd() {
        int mAdCounter = sharedPrefHelper.getAdCounter();
        if (mAdCounter >= 6) {
            if (mInterstitialAd.isLoaded()) mInterstitialAd.show();
            sharedPrefHelper.setAdCounter(0);
        } else {
            sharedPrefHelper.setAdCounter(mAdCounter + 1);
        }
    }

    private void initRewardedVideo() {
        RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                mButtonBonus.setEnabled(true);
                mButtonBonus.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.enable_bonus_button));
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                mButtonBonus.setEnabled(false);
                mButtonBonus.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.disabled_bonus_button));
                loadRewardVideo();
                if (bonusUsed) useBonus();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                mPoints.useBonusHint();
                bonusUsed = true;
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
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        mRewardedVideoAd.setRewardedVideoAdListener(rewardedVideoAdListener);
        loadRewardVideo();
    }

    private void loadRewardVideo() {
        mRewardedVideoAd.loadAd(getString(R.string.rewarded_video_id), new AdRequest.Builder().build());
    }

    private void showDialogForBonus() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialog.setTitle(getString(R.string.dialog_title));
        alertDialog.setMessage(getString(R.string.dialog_bonus_message));
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton(getString(R.string.positive_button), (dialogInterface, i) -> {
            if (mRewardedVideoAd.isLoaded()) {
                mRewardedVideoAd.show();
            }
        });
        alertDialog.setNegativeButton(getString(R.string.negative_button), (dialogInterface, i) -> {

        });
        alertDialog.show();
    }

    private void incrementId() {
        if (mQId != 49) {
            mQId++;
        } else {
            mQId = 0;
            mNextButton.setText(getString(R.string.end_game_text, categoryTitle));
            sharedPrefHelper.setCategoryComplete(categoryTitle);
        }
    }

}
