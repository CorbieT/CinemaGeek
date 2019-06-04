package com.bignerdranch.android.cinemaquiz.model;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.widget.Toast;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.common.HawkManager;

import java.io.IOException;

public class SoundRep {

    private static final int MAX_SOUNDS = 5;

    private AssetManager mAssetManager;
    private SoundPool mSoundPool;

    private int mHintSound;
    private int mButtonClickSound;
    private int mErrorSound;
    private int mPoints;
    private int mSwishUp;
    private int mSwishDown;

    private boolean isSound;

    private Context mContext;

    public SoundRep(Context context) {
        mContext = context;
        isSound = HawkManager.getInstance().isKeySound();
        loadSoundPool();
    }

    private void loadSoundPool() {
        createSoundPool();
        mAssetManager = mContext.getAssets();

        mHintSound = loadSound("hint1.mp3");
        mButtonClickSound = loadSound("button.mp3");
        mErrorSound = loadSound("wrong.mp3");
        mPoints = loadSound("coins.mp3");
        mSwishUp = loadSound("swishup.mp3");
        mSwishDown = loadSound("swishdown.mp3");
    }

    private void createSoundPool() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            createOldSoundPool();
        } else {
            createNewSoundPool();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    private void createOldSoundPool() {
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
    }

    private int loadSound(String fileName) {
        AssetFileDescriptor afd;
        try {
            afd = mAssetManager.openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(mContext.getApplicationContext(), mContext.getString(R.string.error_load_sound_file_message) + fileName, Toast.LENGTH_SHORT).show();
            return -1;
        }
        return mSoundPool.load(afd, 1);
    }

    public void playSound(int sound) {
        if (isSound && sound > 0) {
            mSoundPool.play(sound, 1, 1, 1, 0, 1);
        }
    }

    //Crash with call
    public void releaseSoundPool() {
        mSoundPool.release();
        mSoundPool = null;
    }

    public int getHintSound() {
        return mHintSound;
    }

    public int getButtonClickSound() {
        return mButtonClickSound;
    }

    public int getErrorSound() {
        return mErrorSound;
    }

    public int getPoints() {
        return mPoints;
    }

    public int getSwishUp() {
        return mSwishUp;
    }

    public int getSwishDown() {
        return mSwishDown;
    }
}
