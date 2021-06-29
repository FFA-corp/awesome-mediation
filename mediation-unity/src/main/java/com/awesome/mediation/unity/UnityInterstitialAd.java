package com.awesome.mediation.unity;

import android.app.Activity;
import android.content.Context;

import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.base.MediationAdCallback;
import com.awesome.mediation.library.base.MediationInterstitialAd;
import com.awesome.mediation.library.base.MediationNetworkLoader;
import com.awesome.mediation.library.util.MediationAdLogger;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;

public class UnityInterstitialAd extends MediationInterstitialAd<Activity> {

    @Override
    public void showAd(Activity context) {
        if (!super.canShowAd(context)) {
            MediationAdLogger.logI("Can't show ad");
            return;
        }

        super.updateShowingState();
        UnityAds.show(((Activity) context), adUnitId, new IUnityAdsShowListener() {
            @Override
            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                UnityInterstitialAd.super.onAdError(message);
            }

            @Override
            public void onUnityAdsShowStart(String placementId) {
            }

            @Override
            public void onUnityAdsShowClick(String placementId) {
                UnityInterstitialAd.super.onAdClicked();
            }

            @Override
            public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                UnityInterstitialAd.super.onAdImpression();
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
    protected void onAdLoaded() {
        super.onAdLoaded();
        MediationAdCallback<MediationNetworkLoader> mediationAdCallback = getMediationAdCallback();
        if (mediationAdCallback != null) {
            mediationAdCallback.onAdLoaded(adPositionName, getMediationNetwork(), getMediationAdType(), UnityInterstitialAd.this);
        }
    }

    @Override
    protected MediationAdNetwork getMediationNetwork() {
        return MediationAdNetwork.UNITY;
    }
}
