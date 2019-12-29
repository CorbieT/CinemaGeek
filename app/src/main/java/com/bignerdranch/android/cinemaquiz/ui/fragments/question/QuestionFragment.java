package com.bignerdranch.android.cinemaquiz.ui.fragments.question;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.common.AdHelper;
import com.bignerdranch.android.cinemaquiz.common.SharedPrefHelper;
import com.bignerdranch.android.cinemaquiz.common.factory.CellFactory;
import com.bignerdranch.android.cinemaquiz.repositories.SoundRep;
import com.bignerdranch.android.cinemaquiz.repositories.questions.QuestionRepositoryImpl;
import com.bignerdranch.android.cinemaquiz.ui.fragments.BaseFragment;
import com.bignerdranch.android.cinemaquiz.ui.fragments.dialogs.BonusDialogFragment;
import com.bignerdranch.android.cinemaquiz.utils.Utils;
import com.bignerdranch.android.cinemaquiz.view.cell.AnswerCell;
import com.bignerdranch.android.cinemaquiz.view.cell.CellType;
import com.bignerdranch.android.cinemaquiz.view.cell.GameCell;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.bignerdranch.android.cinemaquiz.common.Constants.MATCH_PARENT;
import static com.bignerdranch.android.cinemaquiz.common.Constants.WRAP_CONTENT;

public class QuestionFragment extends BaseFragment implements QuestionContract.View {

    @BindView(R.id.hint_container)
    LinearLayout hintContainer;

    @BindView(R.id.title_container)
    LinearLayout titleContainer;

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

    @BindView(R.id.progress_bar_question)
    ProgressBar progressBar;

    private Unbinder unbinder;

    public static final String CATEGORY_TAG = "CATEGORY_TITLE";

    private String categoryTitle = "";

    private AdHelper adHelper;
    private CellFactory cellFactory;

    private QuestionContract.Presenter presenter;

    public static QuestionFragment newInstance(String category) {
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY_TAG, category);
        QuestionFragment fragment = new QuestionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.question_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadBundle(getArguments());
        unbinder = ButterKnife.bind(this, view);
        cellFactory = new CellFactory(getActivity());
        presenter = new QuestionPresenter(this,
                categoryTitle,
                new SharedPrefHelper(Objects.requireNonNull(getActivity())),
                new SoundRep(getActivity()),
                new QuestionRepositoryImpl(Utils.getXmlParser(Objects.requireNonNull(getContext()), R.xml.text), categoryTitle),
                getLifecycle());
        setViewsOnClickListeners(view);
        initAdMob();
        createGameField();
    }

    private void loadBundle(Bundle bundle) {
        if (bundle != null) {
            categoryTitle = bundle.getString(CATEGORY_TAG);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.hint_bonus, R.id.hint_1, R.id.hint_2, R.id.next_button})
    public void setViewsOnClickListeners(View view) {
        switch (view.getId()) {
            case R.id.hint_bonus:
                presenter.clickOnBonusButton();
                break;
            case R.id.hint_1:
                presenter.clickOnFirstHintButton();
                break;
            case R.id.hint_2:
                presenter.clickOnSecondHintButton();
                break;
            case R.id.next_button:
                presenter.clickOnNextButton();
                break;
            default:
                break;
        }
    }

    @Override
    public void showLoader() {
        titleContainer.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
        hintContainer.setVisibility(View.GONE);
        gameCellContainer.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        progressBar.setVisibility(View.GONE);
        titleContainer.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        hintContainer.setVisibility(View.VISIBLE);
        gameCellContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void createAnswerField(String answer) {
        answerContainer.removeAllViewsInLayout();
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1.0f);
        linearLayoutParams.setMargins(0, 0, 0, 20);
        linearLayoutParams.gravity = Gravity.CENTER;
        LinearLayout.LayoutParams cellLayoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1.0f);
        cellLayoutParams.setMargins(0, 0, 10, 0);

        String[] answerWords = Utils.getStringSplittedBySpace(answer);
        int cellAddedCounter = 0;
        for (String answerWord : answerWords) {
            //FIXME fix this shit below
            if (answerWord.length() > 10) {
                String splittedWord = answerWord.substring(0, 9);
                for (int i = 0; i < 2; i++) {
                    LinearLayout linearLayout = new LinearLayout(getActivity());
                    answerContainer.addView(linearLayout, linearLayoutParams);
                    for (int j = 0; j < splittedWord.length(); j++) {
                        linearLayout.addView(createAnswerCell(String.valueOf(splittedWord.charAt(j)), cellAddedCounter), cellLayoutParams);
                        cellAddedCounter++;
                    }
                    if (i == 0) { linearLayout.addView(cellFactory.createCell(CellType.DASH), cellLayoutParams); }
                    splittedWord = answerWord.substring(9);
                }
                continue;
            }
            LinearLayout linearLayout = new LinearLayout(getActivity());
            answerContainer.addView(linearLayout, linearLayoutParams);
            for (int i = 0; i < answerWord.length(); i++) {
                linearLayout.addView(createAnswerCell(String.valueOf(answerWord.charAt(i)), cellAddedCounter), cellLayoutParams);
                cellAddedCounter++;
            }
        }
    }

    private AnswerCell createAnswerCell(String correctSymbol, int cellId) {
        final AnswerCell answerCell = (AnswerCell) cellFactory.createCell(CellType.ANSWER);
        answerCell.setCorrectSymbol(correctSymbol.toUpperCase());
        answerCell.setOnClickListener(view -> presenter.clickOnAnswerCell(cellId));
        presenter.addAnswerCell(answerCell);
        return answerCell;
    }

    private void createGameField() {
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, 1.0f);
        linearLayoutParams.setMargins(0, 0, 0, 15);
        int cellAddedCounter = 0;
        for (int i = 0; i < 3; i++) {
            LinearLayout linearLayout = new LinearLayout(getActivity());
            gameCellContainer.addView(linearLayout, linearLayoutParams);
            for (int j = 0; j < 6; j++) {
                linearLayout.addView(createGameCell(cellAddedCounter));
                cellAddedCounter++;
            }
        }
    }

    private GameCell createGameCell(int cellId) {
        LinearLayout.LayoutParams cellLayoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1.0f);
        cellLayoutParams.setMargins(3, 3, 3, 3);
        final GameCell gameCell = (GameCell) cellFactory.createCell(CellType.GAME);
        gameCell.setLayoutParams(cellLayoutParams);
        gameCell.setOnClickListener(view -> presenter.clickOnGameCell(cellId));
        presenter.addGameCell(gameCell);
        return gameCell;
    }

    @Override
    public void disableNextButton() {
        nextButton.setClickable(false);
    }

    @Override
    public void setDefaultImageSecondHint() {
        presenter.disableUsedSecondHint();
        buttonHint2.setBackgroundResource(R.drawable.hint_button);
    }

    @Override
    public void showInterstitialAd() {
        adHelper.showInterstitialAd();
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
                presenter.bonusUsed();
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
        getLifecycle().addObserver(adHelper);
        adHelper.loadRewardVideo();
    }

    @Override
    public void showDialogForBonus() {
        new BonusDialogFragment(() -> adHelper.showRewardVideo())
                .show(Objects.requireNonNull(getFragmentManager()), null);
    }

    @Override
    public void setNextButtonCategoryCompleteText(String category) {
        nextButton.setText(getString(R.string.end_game_text, category));
    }

    @Override
    public void setNextButtonBackground(int drawableId) {
        nextButton.setBackgroundResource(drawableId);
    }

    @Override
    public void scrollQuestionTextToUp() {
        scrollView.scrollTo(0, 0);
    }

    /**VIEW VISIBILITY*/
    @Override
    public void hideFirstHintButton() {
        buttonHint1.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showHintButtons() {
        buttonHint1.setVisibility(View.VISIBLE);
        buttonHint2.setVisibility(View.VISIBLE);
    }

    /**UPDATE CONTENT*/

    @Override
    public void updateSecondHintBackground(boolean criteriaUpdate) {
        if (criteriaUpdate) buttonHint2.setBackgroundResource(R.drawable.hint_button_active);
        else buttonHint2.setBackgroundResource(R.drawable.hint_button);
    }

    @Override
    public void updateHintTitle(int currentPoints) {
        hintTitle.setText(getString(R.string.hints_title, currentPoints));
    }

    @Override
    public void updateQuestionTitle(int questionId) {
        questionTitle.setText(getString(R.string.question_title, questionId + 1));
    }

    @Override
    public void updateQuestionText(String text) {
        questionText.setText(text);
    }

    /**ANIMATION*/

    @Override
    public void animationShowNextButton() {
        nextButton.setVisibility(View.VISIBLE);
        float butStart = nextButton.getTop() - nextButton.getHeight();
        float butEnd = nextButton.getTop();

        ObjectAnimator buttonAnimator = ObjectAnimator.ofFloat(nextButton, "y", butStart, butEnd)
                .setDuration(200);
        buttonAnimator.start();
        nextButton.setClickable(true);
    }

    @Override
    public void animationHideNextButton() {
        float butStart = nextButton.getTop();
        float butEnd = nextButton.getBottom();

        ObjectAnimator buttonAnimator = ObjectAnimator.ofFloat(nextButton, "y", butStart, butEnd)
                .setDuration(200);
        buttonAnimator.start();
    }

    @Override
    public void playAnimationWrongAnswer(List<AnswerCell> answerCells) {
        for (AnswerCell animationCell : answerCells) {
            playAnimationWrong(animationCell);
        }
    }

    @Override
    public void playAnimationWrongHintTitle() {
        playAnimationWrong(hintTitle);
    }

    private void playAnimationWrong(TextView textView) {
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

    @Override
    public void showErrorLoadingQuestions(String msg) {
        Utils.showToast(getContext(), getString(R.string.error_msg, msg));
    }
}
