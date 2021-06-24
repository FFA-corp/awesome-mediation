package com.awesome.mediation.admob;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.awesome.mediation.admob.util.AdMobAdUtil;
import com.awesome.mediation.library.MediationInterstitialAdCache;
import com.awesome.mediation.library.base.MediationAdCallback;
import com.awesome.mediation.library.base.MediationInterstitialAd;
import com.awesome.mediation.library.base.MediationNetworkLoader;
import com.awesome.mediation.library.config.MediationAdManager;
import com.awesome.mediation.library.config.MediationPrefs;
import com.awesome.mediation.library.util.MediationAdLogger;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class AdMobInterstitialAd extends MediationInterstitialAd {
    private InterstitialAd interstitialAdObj;

    @Override
    public void showAd(Context context) {
        MediationAdLogger.logI("showAd");
        if (context instanceof Activity) {
            if (MediationAdManager.getInstance(context).getAppDelegate().isAppPurchased()) {
                MediationAdLogger.logI("App purchased. Skip inter ad");
                return;
            }
            this.adShowed = true;
            MediationInterstitialAdCache.instance().showing.postValue(true);
            this.interstitialAdObj.show(((Activity) context));
            return;
        }
        MediationAdLogger.logE("invalid context");
    }

    @Override
    public boolean load(Context context) {
        boolean b = !super.load(context);
        if (b) {
            return false;
        }

        loadAd(context);
        return true;
    }

    private void loadAd(Context context) {

        MediationAdCallback<MediationNetworkLoader> mediationAdCallback = getMediationAdCallback();
        if (mediationAdCallback != null && TextUtils.isEmpty(adUnitId)) {
            MediationAdLogger.logE("AD UNIT is empty");
            return;
        }
        final MediationInterstitialAdCache instance = MediationInterstitialAdCache.instance();
        if (instance.hasCache(adPositionName) && !instance.getAd(adPositionName).isAdShowed()) {
            MediationAdLogger.logD(adPositionName + " has cache instance. Return result now.");
            interstitialAdObj = ((AdMobInterstitialAd) instance.getAd(adPositionName)).interstitialAdObj;
            return;
        }

        MediationPrefs.instance(context).saveLastInterAdRequestTime();
        InterstitialAd.load(context, adUnitId, AdMobAdUtil.getAdRequestBuilderWithTestDevice(context).build(),
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        MediationAdLogger.logD("onAdLoaded");
                        interstitialAdObj = interstitialAd;
                        interstitialAdObj.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdFailedToShowFullScreenContent(@NotNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                                MediationAdLogger.logD(adError.getMessage());
                                if (getMediationAdCallback() != null) {
                                    getMediationAdCallback().onAdError(adError.getMessage());
                                }
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                                MediationAdLogger.showCurrentMethodName();
                                adShowed = true;
                                instance.showing.postValue(true);
                                if (getMediationAdCallback() != null) {
                                    getMediationAdCallback().onAdImpression();
                                }
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                MediationAdLogger.showCurrentMethodName();
                                instance.markShowAd(adPositionName);
                                instance.showing.setValue(false);
                                instance.showing.postValue(false);
                                if (getMediationAdCallback() != null) {
                                    getMediationAdCallback().onAdClosed();
                                }
                            }

                        });
                        if (getMediationAdCallback() != null) {
                            MediationAdLogger.logD("onAdLoaded");
                            getMediationAdCallback().onAdLoaded(AdMobInterstitialAd.this);
                        }

                        instance.saveInterstitialAd(adPositionName, AdMobInterstitialAd.this);

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        MediationAdLogger.logE(loadAdError.getMessage());
                        interstitialAdObj = null;
                        if (getMediationAdCallback() != null) {
                            getMediationAdCallback().onAdError(String.format(Locale.US, "Error %d: %s", loadAdError.getCode(), loadAdError.getMessage()));
                        }
                    }
                });
    }
}
