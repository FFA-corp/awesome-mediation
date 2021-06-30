package com.awesome.mediation.appodeal;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.base.MediationRewardedAd;
import com.awesome.mediation.library.base.RewardAdRewardListener;
import com.awesome.mediation.library.util.MediationAdLogger;

public class AppodealRewardAd extends MediationRewardedAd {

    private RewardAdRewardListener rewardAdRewardListener;

    @Override
    public boolean load(Context context) {
        if (!super.load(context)) {
            return false;
        }
        assert context instanceof Activity;
        Activity activity = (Activity) context;
//        AppodealInitializer.getInstance().enableAutoCache(false, Appodeal.INTERSTITIAL);
        AppodealInitializer.getInstance().initRewardAd(activity, new RewardedVideoCallbacks() {
            @Override
            public void onRewardedVideoLoaded(boolean b) {
                onAdLoaded();
            }

            @Override
            public void onRewardedVideoFailedToLoad() {
                onAdError("Rewarded video failed to load");
            }

            @Override
            public void onRewardedVideoShown() {
                Log.i("superman", "onRewardedVideoShown: ");
            }

            @Override
            public void onRewardedVideoShowFailed() {
                onAdError("Rewarded video show failed");
            }

            @Override
            public void onRewardedVideoFinished(double v, String s) {
                Log.i("superman", "onRewardedVideoFinished: " + v + "  " + s);
                if (rewardAdRewardListener != null) {
                    rewardAdRewardListener.onUserEarnedReward();
                }
            }

            @Override
            public void onRewardedVideoClosed(boolean b) {
                onAdClosed();
            }

            @Override
            public void onRewardedVideoExpired() {
                onAdError("Reward video expired");
            }

            @Override
            public void onRewardedVideoClicked() {
                onAdClicked();
            }
        });
        if (isAdLoaded()) {
            onAdLoaded();
            return true;
        }
        return true;
    }

    @Override
    protected MediationAdNetwork getMediationNetwork() {
        return MediationAdNetwork.APPODEAL;
    }

    @Override
    protected void onAdLoaded() {
        super.onAdLoaded();
        if (getMediationAdCallback() != null) {
            getMediationAdCallback().onAdLoaded(adPositionName, getMediationNetwork(), getMediationAdType(), this);
        }
    }


    @Override
    public boolean isAdLoaded() {
        return Appodeal.isLoaded(Appodeal.REWARDED_VIDEO);
    }

    @Override
    public void showAd(Context context, RewardAdRewardListener rewardAdRewardListener) {
        this.rewardAdRewardListener = rewardAdRewardListener;
        if (!canShowAd(context)) {
            return;
        }
        if (context instanceof Activity) {
            Appodeal.show(((Activity) context), Appodeal.REWARDED_VIDEO);
            return;
        }
        MediationAdLogger.logE("invalid context");
    }
}
