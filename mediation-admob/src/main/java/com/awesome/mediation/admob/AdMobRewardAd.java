package com.awesome.mediation.admob;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.awesome.mediation.admob.util.AdMobAdUtil;
import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.base.MediationRewardedAd;
import com.awesome.mediation.library.base.RewardAdRewardListener;
import com.awesome.mediation.library.util.MediationAdLogger;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AdMobRewardAd extends MediationRewardedAd {
    private static final String TAG = "ADMOB-REWARD";
    private RewardedAd rewardedAd;

    @Override
    public void showAd(Context context, RewardAdRewardListener rewardAdRewardListener) {
        if (!canShowAd(context)) {
            return;
        }
        if (context instanceof Activity) {
            if (rewardedAd != null) {
                rewardedAd.show(((Activity) context), rewardItem -> {
                    if (rewardAdRewardListener != null) {
                        rewardAdRewardListener.onUserEarnedReward();
                    }
                });
            }
            return;
        }
        MediationAdLogger.logE("invalid context");
    }

    @Override
    public boolean load(Context context) {
        if (!super.load(context)) {
            return false;
        }

        RewardedAd.load(context, adUnitId,
                AdMobAdUtil.getAdRequestBuilderWithTestDevice(context).build(), new RewardedAdLoadCallback() {

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        MediationAdLogger.logD(TAG, loadAdError.getMessage());
                        onAdError(loadAdError.getMessage());
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        AdMobRewardAd.this.rewardedAd = rewardedAd;
                        configCallbackForRewardAd();
                        AdMobRewardAd.this.onAdLoaded();
                        MediationAdLogger.logD(TAG, "Ad was loaded.");
                    }
                });
        return true;
    }

    @Override
    protected void onAdLoaded() {
        super.onAdLoaded();
        if (getMediationAdCallback() != null) {
            getMediationAdCallback().onAdLoaded(getMediationNetwork(), getMediationAdType(), AdMobRewardAd.this);
        }
    }

    @Override
    protected MediationAdNetwork getMediationNetwork() {
        return MediationAdNetwork.ADMOB;
    }

    private void configCallbackForRewardAd() {
        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                MediationAdLogger.logD(TAG, "Ad was shown.");
                rewardedAd = null;
                AdMobRewardAd.this.onAdImpression();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                MediationAdLogger.logD(TAG, "Ad failed to show.");
                onAdError(adError.getMessage());
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                MediationAdLogger.logD(TAG, "Ad was dismissed.");
                onAdClosed();
            }
        });
    }
}
