package com.bignerdranch.android.cinemaquiz.common;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.bignerdranch.android.cinemaquiz.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class AdHelper implements LifecycleObserver {
    private Context context;
    private RewardedVideoAd rewardedVideoAd;
    private InterstitialAd interstitialAd;

    public AdHelper(Context context, RewardedVideoAdListener rewardedVideoAdListener) {
        this.context = context;
        initRewardedVideo(context, rewardedVideoAdListener);
        initInterstitialAd(context);
    }

    private void initRewardedVideo(Context context, RewardedVideoAdListener rewardedVideoAdListener) {
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
        rewardedVideoAd.setRewardedVideoAdListener(rewardedVideoAdListener);
    }

    public void loadRewardVideo() {
        rewardedVideoAd.loadAd(context.getString(R.string.rewarded_video_id), new AdRequest.Builder().build());
    }

    public void showRewardVideo() {
        if (rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.show();
        }
    }

    private void initInterstitialAd(Context context) {
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(context.getString(R.string.interstitial_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    public void showInterstitialAd() {
        if (interstitialAd.isLoaded()) interstitialAd.show();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void resume() {
        rewardedVideoAd.resume(context);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void pause() {
        rewardedVideoAd.pause(context);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void destroy() {
        rewardedVideoAd.destroy(context);
    }
}
