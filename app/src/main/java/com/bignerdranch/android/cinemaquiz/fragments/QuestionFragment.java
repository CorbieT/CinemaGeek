package com.bignerdranch.android.cinemaquiz.fragments;

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
import com.bignerdranch.android.cinemaquiz.common.AnimatorHelper;
import com.bignerdranch.android.cinemaquiz.common.RandomHelper;
import com.bignerdranch.android.cinemaquiz.fragments.dialogs.BonusDialogFragment;
import com.bignerdranch.android.cinemaquiz.interfaces.Function;
import com.bignerdranch.android.cinemaquiz.interfaces.QuestionContract;
import com.bignerdranch.android.cinemaquiz.model.AnswerCell;
import com.bignerdranch.android.cinemaquiz.model.GameCell;
import com.bignerdranch.android.cinemaquiz.model.Question;
import com.bignerdranch.android.cinemaquiz.presenters.QuestionPresenter;
import com.bignerdranch.android.cinemaquiz.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.bignerdranch.android.cinemaquiz.common.Constants.ALPHABET;
import static com.bignerdranch.android.cinemaquiz.common.Constants.ALPHABET_SIZE;
import static com.bignerdranch.android.cinemaquiz.common.Constants.MATCH_PARENT;
import static com.bignerdranch.android.cinemaquiz.common.Constants.MAX_CELLS_COUNT;
import static com.bignerdranch.android.cinemaquiz.common.Constants.WRAP_CONTENT;

public class QuestionFragment extends BaseFragment implements QuestionContract.View {

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

    private static final String CATEGORY_TAG = "CATEGORY_TITLE";

    private LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, 1.0f);
    private LinearLayout.LayoutParams cellLayoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1.0f);

    private List<GameCell> gameCells = new ArrayList<>();
    private List<AnswerCell> answerCells = new ArrayList<>();

    private String categoryTitle = "";
    private AnimatorHelper animatorHelper;

    private QuestionPresenter presenter;

    private Unbinder unbinder;

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
        animatorHelper = new AnimatorHelper(getActivity());
    }

    private void loadBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            categoryTitle = bundle.getString(CATEGORY_TAG);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.question_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter = new QuestionPresenter(getActivity(), this, categoryTitle);
        setViewsOnClickListeners(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.hint_bonus, R.id.hint_1, R.id.hint_2, R.id.next_button})
    public void setViewsOnClickListeners(View view) {
        presenter.disableSecondHint();
        switch (view.getId()) {
            case R.id.hint_bonus:
                presenter.clickBonus();
                break;
            case R.id.hint_1:
                presenter.clickFirstHint();
                break;
            case R.id.hint_2:
                presenter.clickSecondHint();
                break;
            case R.id.next_button:
                presenter.clickNextButton();
                break;
            default:
                break;
        }
    }

    @Override
    public void createGameField() {
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
    }

    private GameCell createGameCell() {
        final GameCell gameCell = new GameCell(getActivity());
        gameCells.add(gameCell);
        gameCell.setLayoutParams(cellLayoutParams);
        gameCell.setOnClickListener(view -> {
            presenter.clickGameCell();
            for (AnswerCell answerCell : answerCells) {
                if (answerCell.isEmpty()) {
                    answerCell.setText(gameCell.getText());
                    answerCell.setGameCellPickedId(gameCells.indexOf(gameCell));
                    gameCell.hideCell();
                    checkWin();
                    return;
                }
            }
        });
        return gameCell;
    }

    @Override
    public void updateContent(Question question, int currentPoints) {
        questionTitle.setText(getString(R.string.question_title, question.getId() + 1));
        questionText.setText(question.getQuestionText());
        updateHintTitle(currentPoints);
        createAnswerField(question.getAnswer().toUpperCase());
        showCellsGameField();
        initGameCellsWithAnswer(Utils.removeSpaces(question.getAnswer()));
        scrollView.scrollTo(0, 0);
        buttonHint1.setVisibility(View.VISIBLE);
        buttonHint2.setVisibility(View.VISIBLE);
    }

    private void createAnswerField(String answer) {
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1.0f);
        linearLayoutParams.setMargins(0, 0, 0, 20);
        linearLayoutParams.gravity = Gravity.CENTER;
        cellLayoutParams.setMargins(0, 0, 10, 0);
        answerCells.clear();
        answerContainer.removeAllViews();

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
                    if (i == 0) {
                        linearLayout.addView(createDashView(), cellLayoutParams);
                    }
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
            if (presenter.isUsingSecondHint() && answerCell.isEmpty()) {
                answerCell.showCorrectSymbol();
                hidePromptedGameCell(answerCell.getCorrectSymbol());
                presenter.secondHintUsed();
                checkWin();
            } else if (!answerCell.isEmpty() && !answerCell.isPrompted()) {
                presenter.clickAnswerCell();
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

    private void initGameCellsWithAnswer(@NonNull String answer) {
        if (answer.length() > MAX_CELLS_COUNT) {
            throw new IllegalArgumentException("word length it's too long");
        }
        initGameCellsWithRandomChars();
        setAnswerInGameField(answer);
    }

    private void initGameCellsWithRandomChars() {
        Random random = new Random();
        for (GameCell gameCell : gameCells) {
            gameCell.setText(String.valueOf(ALPHABET.charAt(random.nextInt(ALPHABET_SIZE))));
            gameCell.setRightSymbol(false);
        }
    }

    private void setAnswerInGameField(@NonNull String answer) {
        RandomHelper randomHelper = new RandomHelper(MAX_CELLS_COUNT);
        for (int i = 0; i < answer.length(); i++) {
            int randomGameCellPosition = randomHelper.nextUniqueInt();
            gameCells.get(randomGameCellPosition).setRightSymbol(true);
            gameCells.get(randomGameCellPosition).setText(String.valueOf(answer.charAt(i)));
        }
    }

    private void checkWin() {
        if (hasEmptyAnswerFields()) return;
        if (hasIncorrectAnswerSymbol()) {
            presenter.userLost();
            return;
        }
        presenter.userWon();
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

    @Override
    public void setNextButtonBackground(String category) {
        switch (category) {
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

    @Override
    public void setPuzzleNextButton(String category, int qId) {
        switch (category) {
            case "СОЛЯНКА-2":
                if (qId == 32) nextButton.setBackgroundResource(R.drawable.smiley_evolution_bg);
                break;
            case "СУПЕР":
                if (qId == 27) nextButton.setBackgroundResource(R.drawable.smiley_false_god_bg);
                break;
            case "СЕРИАЛЫ":
                if (qId == 9) nextButton.setBackgroundResource(R.drawable.smiley_missme_bg);
        }
    }

    private void showCellsGameField() {
        for (GameCell gameCell : gameCells) {
            gameCell.showCell();
        }
    }

    @Override
    public void hideNonPromptedAnswerCells() {
        for (AnswerCell answerCell : answerCells) {
            if (!answerCell.isPrompted()) {
                answerCell.clearAnswerCell();
                gameCells.get(answerCell.getGameCellPickedId()).showCell();
            }
        }
    }

    @Override
    public void hideIncorrectGameCells() {
        for (GameCell gameCell : gameCells) {
            if (!gameCell.isRightSymbol()) gameCell.hideCell();
        }
    }

    @Override
    public void hideFirstHintButton() {
        buttonHint1.setVisibility(View.INVISIBLE);
    }

    @Override
    public void changeSecondHintBackground(boolean isUse) {
        if (isUse) buttonHint2.setBackgroundResource(R.drawable.hint_button_active);
        else buttonHint2.setBackgroundResource(R.drawable.hint_button);
    }

    @Override
    public void enableBonusButton() {
        buttonBonus.setEnabled(true);
        buttonBonus.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.enable_bonus_button));
    }

    @Override
    public void disableBonusButton() {
        buttonBonus.setEnabled(false);
        buttonBonus.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.disabled_bonus_button));
    }

    @Override
    public void showNextButton() {
        nextButton.setVisibility(View.VISIBLE);
        animatorHelper.animationTopDown(nextButton);
        nextButton.setClickable(true);
    }

    @Override
    public void hideNextButton() {
        nextButton.setClickable(false);
        animatorHelper.animationHideDown(nextButton);
    }

    @Override
    public void playWrongAnimationHintsTitle() {
        showAnimationWrong(hintTitle);
    }

    @Override
    public void playWrongAnimationAnswer() {
        for (AnswerCell animationCell : answerCells) {
            showAnimationWrong(animationCell);
        }
    }

    private void showAnimationWrong(TextView textView) {
        animatorHelper.animationWrongText(textView);
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
                return;
            }
        }
    }

    @Override
    public void setDefaultImageSecondHint() {
        buttonHint2.setBackgroundResource(R.drawable.hint_button);
    }

    @Override
    public void showDialogForBonus(Function positiveClick) {
        new BonusDialogFragment(positiveClick)
                .show(Objects.requireNonNull(getFragmentManager()), null);
    }

    @Override
    public void updateHintTitle(int currentPoints) {
        hintTitle.setText(getString(R.string.hints_title, currentPoints));
    }

    @Override
    public void setCompleteTextNextButton(String categoryTitle) {
        nextButton.setText(getString(R.string.end_game_text, categoryTitle));
    }
}
