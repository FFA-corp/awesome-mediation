package com.awesome.mediation.unity;

import android.app.Activity;
import android.content.Context;

import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.base.MediationAdCallback;
import com.awesome.mediation.library.base.MediationNetworkLoader;
import com.awesome.mediation.library.base.MediationRewardedAd;
import com.awesome.mediation.library.base.RewardAdRewardListener;
import com.awesome.mediation.library.util.MediationAdLogger;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;

public class UnityRewardedAd extends MediationRewardedAd {

    @Override
    public void showAd(Context context, RewardAdRewardListener rewardAdRewardListener) {
        if (!super.canShowAd(context)) {
            MediationAdLogger.logI("Can't show ad");
            return;
        }

        super.updateShowingState();
        UnityAds.show(((Activity) context), adUnitId, new IUnityAdsShowListener() {
            @Override
            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                UnityRewardedAd.super.onAdError(message);
            }

            @Override
            public void onUnityAdsShowStart(String placementId) {
                UnityRewardedAd.super.onAdImpression();
            }

            @Override
            public void onUnityAdsShowClick(String placementId) {
                UnityRewardedAd.super.onAdClicked();
            }

            @Override
            public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                if (rewardAdRewardListener != null) {
                    rewardAdRewardListener.onUserEarnedReward();
                }
            }
        });
    }

    @Override
    public boolean load(Context context) {
        if (!super.load(context)) {
            return false;
        }
        UnityAds.load(adUnitId, new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {
                onAdLoaded();
            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                onAdError(message);
            }
        });
        return true;
    }

    @Override
    protected MediationAdNetwork getMediationNetwork() {
        return MediationAdNetwork.UNITY;
    }

    @Override
    protected void onAdLoaded() {
        super.onAdLoaded();
        MediationAdCallback<MediationNetworkLoader> mediationAdCallback = getMediationAdCallback();
        if (mediationAdCallback != null) {
            mediationAdCallback.onAdLoaded(getMediationNetwork(), getMediationAdType(), this);
        }
    }
}
