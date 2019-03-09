package com.bignerdranch.android.cinemaquiz.fragments;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.cinemaquiz.model.AnswerCell;
import com.bignerdranch.android.cinemaquiz.model.GameCell;
import com.bignerdranch.android.cinemaquiz.model.Points;
import com.bignerdranch.android.cinemaquiz.model.Question;
import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.common.SoundRep;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuestionFragment extends Fragment{

    private TextView mHintTitle;
    private TextView mQuestionText;
    private TextView mQuestionTitle;

    private Button mNextButton;
    private Button mButtonHint1;
    private Button mButtonHint2;
    private Button mButtonBonus;

    private ScrollView mScrollView;

    public static final String APP_TAG = "cinema_quiz";
    public static final String AD_COUNTER_TAG = "ad_counter";
    public static final String CATEGORY_TAG = "CATEGORY_TITLE";
    public static final String ALPHABET = "ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ";

    public static final int MAX_CELLS_COUNT = 18;
    public static final int ALPHABET_SIZE = ALPHABET.length();
    public static final int WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT;

    private static final char EMPTY = ' ';

    private List<GameCell> mGameCells;
    private List<AnswerCell> mAnswerCells;
    private List<Question> mQuestions = new ArrayList<>();
    private final List<Integer> NUMBERS = new ArrayList<>(MAX_CELLS_COUNT);

    private LinearLayout mAnswerContainer;
    private LinearLayout mAdContainer;
    private LinearLayout.LayoutParams mParams;
    private LinearLayout mFirstRowContainer;
    private LinearLayout mSecondRowContainer;
    private LinearLayout mThirdRowContainer;

    private int mQId = 0;
    private int wordLength;
    private int mAdCounter;
    private Points mPoints;
    private String category = "";
    private boolean useSecondHint = false;
    private boolean bonusUsed = false;
    private Random mRandom = new Random();
    private SharedPreferences mPref;
    private SoundRep mSoundRep;
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;

    public static QuestionFragment newInstance(String category){
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY_TAG, category);
        QuestionFragment fragment = new QuestionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.question_fragment, container, false);
        initViewComponents(view);
        parseDocument();
        updateContent();
        setNextButtonBackground();
        return view;
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

    private void initViewComponents(View view){
        setRewardedVideo();
        mGameCells = new ArrayList<>(MAX_CELLS_COUNT);
        mAnswerContainer = view.findViewById(R.id.answer_container);
        mQuestionTitle = view.findViewById(R.id.question_number);
        mQuestionText = view.findViewById(R.id.question_text);
        mScrollView = view.findViewById(R.id.scroll_view);
        mNextButton = view.findViewById(R.id.next_button);
        mHintTitle = view.findViewById(R.id.hint_count);
        mButtonBonus = view.findViewById(R.id.hint_bonus);
        mButtonBonus.setEnabled(false);
        mButtonBonus.setTextColor(ContextCompat.getColor(getContext(), R.color.disabled_bonus_button));
        mButtonBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (useSecondHint) setDefaultImageSecondHint();
                showDialogForBonus();
            }
        });
        mButtonHint1 = view.findViewById(R.id.hint_1);
        mButtonHint1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useHint1();
            }
        });
        mButtonHint2 = view.findViewById(R.id.hint_2);
        mButtonHint2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useHint2();
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationHideNextButton();
            }
        });
        mPref = getActivity().getSharedPreferences(APP_TAG, Context.MODE_PRIVATE);
        mParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        mSoundRep = new SoundRep(getContext());
        mPoints = new Points(mPref);
        setInterstitialAd();
        setBanner();
        initGameField(view);
        initNumb();
    }

    private void setNextButtonBackground(){
        switch (category){
            case "УЖАСЫ": mNextButton.setBackgroundResource(R.drawable.smiley_blood_bg);
                break;
            case "ГОЛОВОЛОМКИ": mNextButton.setBackgroundResource(R.drawable.smiley_puzzle);
                break;
            case "КИНОГИК": mNextButton.setBackgroundResource(R.drawable.smiley_geek_bg);
                break;
            case "СУПЕР": mNextButton.setBackgroundResource(R.drawable.smiley_haha_bg);
                break;
            default: mNextButton.setBackgroundResource(R.drawable.smiley_bg);
        }
    }

    private void setPuzzleNextButton(){
        switch (category){
            case "СОЛЯНКА-2": if (mQId == 32) mNextButton.setBackgroundResource(R.drawable.smiley_evolution_bg);
                break;
            case "СУПЕР": if (mQId == 27) mNextButton.setBackgroundResource(R.drawable.smiley_false_god_bg);
                break;
            case "СЕРИАЛЫ": if (mQId == 9) mNextButton.setBackgroundResource(R.drawable.smiley_missme_bg);
        }
    }

    private void updateContent(){
        mQuestionTitle.setText(getString(R.string.question_title, mQId + 1));
        mHintTitle.setText(getString(R.string.hints_title, mPoints.getCurrentPoints()));
        mQuestionText.setText(mQuestions.get(mQId).getQuestionText());
        createAnswerField(mQuestions.get(mQId).getAnswer().toUpperCase());
        showCellsGameField();
        setRandomGameField();
        setAnswerInGameField(removeSpaces(mQuestions.get(mQId).getAnswer()));
        mScrollView.scrollTo(0, 0);
        mButtonHint1.setVisibility(View.VISIBLE);
        mButtonHint2.setVisibility(View.VISIBLE);
    }

    private void setRandomGameField(){
        for(GameCell gameCell: mGameCells){
            gameCell.setGameSymbol(ALPHABET.charAt(mRandom.nextInt(ALPHABET_SIZE)));
            gameCell.setRightSymbol(false);
        }
    }

    private void showCellsGameField(){
        for (GameCell gameCell: mGameCells){
            gameCell.showCell();
        }
    }

    @NonNull
    private String removeSpaces(String word){
        StringBuilder noSpaceWord = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != EMPTY){
                noSpaceWord.append(word.charAt(i));
            }
        }
        return noSpaceWord.toString().toUpperCase();
    }

    private void initGameField(View v){
        mFirstRowContainer = v.findViewById(R.id.first_row_container);
        mSecondRowContainer = v.findViewById(R.id.second_row_container);
        mThirdRowContainer = v.findViewById(R.id.third_row_container);

        mFirstRowContainer.removeAllViews();
        mSecondRowContainer.removeAllViews();
        mThirdRowContainer.removeAllViews();

        setRowContainer(mFirstRowContainer);
        setRowContainer(mSecondRowContainer);
        setRowContainer(mThirdRowContainer);
    }

    private void setRowContainer(LinearLayout linearLayout){
        LinearLayout.LayoutParams mGameParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1.0f);
        mGameParams.setMargins(3, 3, 3, 3);
        for (int i = 0; i < 6; i++){
            final GameCell gameCell = new GameCell(getActivity());
            gameCell.setLayoutParams(mGameParams);
            linearLayout.addView(gameCell);
            mGameCells.add(gameCell);
            gameCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                mSoundRep.playSound(mSoundRep.getButtonClickSound());
                if (useSecondHint) setDefaultImageSecondHint();
                for(AnswerCell answerCell: mAnswerCells){
                    if (answerCell.isEmpty() && !gameCell.isClicked()){
                        answerCell.setAnswerSymbol(gameCell);
                        gameCell.hideCell();
                        gameCell.setClicked(true);
                        checkForWin();
                        return;
                    }
                }
                }
            });
        }
    }

    private void saveId(){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(category, mQId).apply();
    }

    private void loadId(){
        mQId = mPref.getInt(category, 0);
    }

    private void setAnswerInGameField(String word){
        if (word.length() > MAX_CELLS_COUNT){
            throw new IllegalArgumentException("word length it's too long");
        }
        Collections.shuffle(NUMBERS);
        for (int i = 0; i < word.length(); i++) {
            (mGameCells.get((NUMBERS.get(i)))).setRightSymbol(true);
            (mGameCells.get((NUMBERS.get(i)))).setGameSymbol(word.charAt(i));
        }
    }

    private void createAnswerField(String word){
        wordLength = word.length();
        if (mAnswerCells == null) mAnswerCells = new ArrayList<>(word.length());
        else mAnswerCells.clear();
        mAnswerContainer.removeAllViewsInLayout();
        if (wordLength > 13) mParams.setMargins(0, 0, 5, 0);
        else mParams.setMargins(0, 0, 10, 0);
        mParams.weight = 1;

        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != EMPTY){
                final AnswerCell answerCell = new AnswerCell(getActivity(), word.charAt(i), wordLength);
                mAnswerContainer.addView(answerCell, mParams);
                mAnswerCells.add(answerCell);
                answerCell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (useSecondHint){
                            if (answerCell.isEmpty()){
                                mSoundRep.playSound(mSoundRep.getButtonClickSound());
                                mPoints.useSecondHint();
                                mHintTitle.setText(getString(R.string.hints_title, mPoints.getCurrentPoints()));
                                hidePickCell(answerCell.getCorrectSymbol());
                                answerCell.showCorrectSymbol();
                                checkForWin();
                            }
                            setDefaultImageSecondHint();
                        }
                        if (!answerCell.isEmpty() && (answerCell.getGameCell() != null)) {
                            mSoundRep.playSound(mSoundRep.getButtonClickSound());
                            answerCell.clearAnswerCell();
                        }
                    }
                });
            }else{
                TextView emptyTextView = new TextView(getActivity());
                emptyTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 5);
                emptyTextView.setMinEms(1);
                emptyTextView.setVisibility(View.INVISIBLE);
                mAnswerContainer.addView(emptyTextView, mParams);
            }
        }
    }

    private void checkForWin(){
        boolean win = true;
        boolean answerComplete = true;

        for(AnswerCell answerCell: mAnswerCells){
            if (!answerCell.compareAnswerSymbols()) win = false;
            if (answerCell.isEmpty()) answerComplete = false;
        }
        if (answerComplete){
            if (win){
                setPuzzleNextButton();
                incrementId();
                showInterstitialAd();
                saveId();
                animationShowNextButton();
                mPoints.increasePoints();
            }else{
                for(AnswerCell answerCell: mAnswerCells){
                    animationWrong(answerCell);
                }
            }
        }
    }

    private void loadBundle(){
        Bundle bundle = getArguments();
        if (bundle != null){
            category = bundle.getString(CATEGORY_TAG);
        }
    }

    private void parseDocument(){
        loadBundle();
        loadId();
        try {
            XmlPullParser parser = getResources().getXml(R.xml.text);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if(parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("category")
                        && parser.getAttributeValue(0).equals(category)){
                    for (int i = 1; i <= 50; i++) {
                        while (!(parser.getEventType() == XmlPullParser.START_TAG
                                && parser.getName().equals("question")
                                && parser.getAttributeValue(0).equals(String.valueOf(i)))) {
                            parser.next();
                        }
                        Question question = new Question();
                        if (parser.next() == XmlPullParser.START_TAG
                                && parser.getName().equals("text")) {
                            if (parser.next() == XmlPullParser.TEXT) {
                                question.setQuestionText(parser.getText());
                            }
                        }
                        while(!(parser.getEventType() == XmlPullParser.START_TAG
                                && parser.getName().equals("answer"))){
                            parser.next();
                        }
                        if(parser.next() == XmlPullParser.TEXT){
                            question.setAnswer(parser.getText());
                        }
                        mQuestions.add(question);
                    }
                }
                parser.next();
            }
        }catch (Throwable t){
            Toast.makeText(getActivity(), "Error loading XML document: " + t.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void useHint1(){
        if (useSecondHint) setDefaultImageSecondHint();
        if (mPoints.checkFirstHint()){
            mSoundRep.playSound(mSoundRep.getHintSound());
            for (AnswerCell answerCell : mAnswerCells) {
                answerCell.clearAnswerCell();
            }
            for (GameCell gameCell : mGameCells) {
                if (!gameCell.isRightSymbol()) gameCell.hideCell();
            }
            mPoints.useFirstHint();
            mHintTitle.setText(getString(R.string.hints_title, mPoints.getCurrentPoints()));
            blockHint1();
        }else{
            animationWrong(mHintTitle);
        }
    }

    private void useHint2(){
        boolean z = false;
        if (mPoints.checkSecondHint()) {
            if (!useSecondHint) {
                z = true;
            }
            useSecondHint = z;
            if (useSecondHint) mButtonHint2.setBackgroundResource(R.drawable.hint_button_active);
            else mButtonHint2.setBackgroundResource(R.drawable.hint_button);
        }else{
            animationWrong(mHintTitle);
        }
    }

    private void useBonus(){
        mSoundRep.playSound(mSoundRep.getPoints());
        mHintTitle.setText(getString(R.string.hints_title, mPoints.getCurrentPoints()));
        bonusUsed = false;
    }

    private void blockHint1(){
        mButtonHint1.setVisibility(View.INVISIBLE);
    }

    private void animationShowNextButton(){
        mNextButton.setVisibility(View.VISIBLE);
        mSoundRep.playSound(mSoundRep.getSwishUp());
        float butStart = mNextButton.getTop() - mNextButton.getHeight();
        float butEnd = mNextButton.getTop();

        ObjectAnimator buttonAnimator = ObjectAnimator.ofFloat(mNextButton, "y", butStart, butEnd)
                .setDuration(200);
        buttonAnimator.start();
        mNextButton.setClickable(true);
    }

    private void animationHideNextButton(){
        updateContent();
        mSoundRep.playSound(mSoundRep.getSwishDown());
        mNextButton.setClickable(false);
        float butStart = mNextButton.getTop();
        float butEnd = mNextButton.getBottom();

        ObjectAnimator buttonAnimator = ObjectAnimator.ofFloat(mNextButton, "y", butStart, butEnd)
                .setDuration(200);
        buttonAnimator.start();
    }

    private void animationWrong(TextView textView){
        mSoundRep.playSound(mSoundRep.getErrorSound());
        ObjectAnimator wrongAnimator = ObjectAnimator
                .ofInt(textView, "textColor", textView.getCurrentTextColor(), ContextCompat.getColor(getContext(), R.color.wrongAnswer))
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

    private void hidePickCell(char correctSymbol){
        boolean temp = true;
        for(GameCell gameCell: mGameCells){
            if (gameCell.getGameSymbol() == correctSymbol && !gameCell.isClicked() && gameCell.isRightSymbol()){
                gameCell.hideCell();
                gameCell.setClicked(true);
                temp = false;
                break;
            }
        }
        if (temp){
            for(AnswerCell answerCell: mAnswerCells){
                if (answerCell.getGameCell() != null && answerCell.getAnswerSymbol() == correctSymbol && answerCell.getGameCell().isRightSymbol()){
                    GameCell gameCell = answerCell.getGameCell();
                    answerCell.clearAnswerCell();
                    gameCell.hideCell();
                    gameCell.setClicked(true);
                    break;
                }
            }
        }
    }

    private void setDefaultImageSecondHint(){
        useSecondHint = false;
        mButtonHint2.setBackgroundResource(R.drawable.hint_button);
    }

    private void initNumb(){
        for (int i = 0; i < MAX_CELLS_COUNT; i++) {
            NUMBERS.add(i);
        }
    }

    private void setInterstitialAd(){
        mAdCounter = mPref.getInt(AD_COUNTER_TAG, 0);
        mInterstitialAd = new InterstitialAd(getActivity());
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

    private void showInterstitialAd(){
        if(mAdCounter != 12){
            mAdCounter++;
        }else{
            if(mInterstitialAd.isLoaded()) mInterstitialAd.show();
            mAdCounter = 0;
        }
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(AD_COUNTER_TAG, mAdCounter).apply();
    }

    private void setRewardedVideo(){
        RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                mButtonBonus.setEnabled(true);
                mButtonBonus.setTextColor(ContextCompat.getColor(getContext(), R.color.enable_bonus_button));
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
                mButtonBonus.setTextColor(ContextCompat.getColor(getContext(), R.color.disabled_bonus_button));
                loadRewardVideo();
                if(bonusUsed) useBonus();
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

    private void loadRewardVideo(){
        mRewardedVideoAd.loadAd(getString(R.string.test_rewarded_video_id), new AdRequest.Builder().build());
    }

    private void showDialogForBonus(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(getString(R.string.dialog_title));
        alertDialog.setMessage(getString(R.string.dialog_bonus_message));
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton(getString(R.string.positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mRewardedVideoAd.isLoaded()){
                    mRewardedVideoAd.show();
                }
            }
        });
        alertDialog.setNegativeButton(getString(R.string.negative_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    private void setBanner(){
        final AdView mAdView = new AdView(getActivity());
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(getString(R.string.test_banner_id));
        mAdView.loadAd(new AdRequest.Builder().build());
        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if(QuestionFragment.this.getView() != null) {
                    mAdContainer = QuestionFragment.this.getView().findViewById(R.id.container_for_Ad);
                    mAdContainer.removeAllViews();
                    mAdContainer.addView(mAdView);
                }
            }
        });
    }

    private void incrementId(){
        if (mQId != 49) {
            mQId++;
        }else{
            mQId = 0;
            mNextButton.setText(getString(R.string.end_game_text, category));
            SharedPreferences.Editor editor = mPref.edit();
            editor.putBoolean(category + "_complete", true).apply();
        }
    }

}
