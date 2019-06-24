package com.bignerdranch.android.cinemaquiz.repositories;

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
import com.bignerdranch.android.cinemaquiz.common.SharedPrefHelper;

import java.io.IOException;

public class SoundRep {

    private static final int MAX_SOUNDS = 5;

    private AssetManager assetManager;
    private SoundPool soundPool;

    private int hintSound;
    private int buttonClickSound;
    private int errorSound;
    private int points;
    private int swishUp;
    private int swishDown;

    private boolean isSound;

    private Context mContext;

    public SoundRep(Context context) {
        this.mContext = context;
        isSound = new SharedPrefHelper(context).isKeySound();
        loadSoundPool();
    }

    private void loadSoundPool() {
        createSoundPool();
        assetManager = mContext.getAssets();

        hintSound = loadSound("hint1.mp3");
        buttonClickSound = loadSound("button.mp3");
        errorSound = loadSound("wrong.mp3");
        points = loadSound("coins.mp3");
        swishUp = loadSound("swishup.mp3");
        swishDown = loadSound("swishdown.mp3");
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
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    private void createOldSoundPool() {
        soundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
    }

    private int loadSound(String fileName) {
        AssetFileDescriptor afd;
        try {
            afd = assetManager.openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(mContext.getApplicationContext(), mContext.getString(R.string.error_load_sound_file_message) + fileName, Toast.LENGTH_SHORT).show();
            return -1;
        }
        return soundPool.load(afd, 1);
    }

    public void playSound(int sound) {
        if (isSound && sound > 0) {
            soundPool.play(sound, 1, 1, 1, 0, 1);
        }
    }

    //Crash with call
    public void releaseSoundPool() {
        soundPool.release();
        soundPool = null;
    }

    public int getHintSound() {
        return hintSound;
    }

    public int getButtonClickSound() {
        return buttonClickSound;
    }

    public int getErrorSound() {
        return errorSound;
    }

    public int getPoints() {
        return points;
    }

    public int getSwishUp() {
        return swishUp;
    }

    public int getSwishDown() {
        return swishDown;
    }
}
