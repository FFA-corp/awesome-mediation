package com.awesome.mediation.admob;


import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.awesome.mediation.admob.util.AdMobAdUtil;
import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.MediationAdType;
import com.awesome.mediation.library.MediationInterstitialAdCache;
import com.awesome.mediation.library.base.MediationInterstitialAd;
import com.awesome.mediation.library.util.MediationAdLogger;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class AdMobInterstitialAd extends MediationInterstitialAd<Activity> {
    private InterstitialAd interstitialAdObj;

    @Override
    public void showAd(Activity context) {
        MediationAdLogger.logI("showAd");
        if (super.canShowAd(context)) {
            super.updateShowingState();
            this.interstitialAdObj.show(((Activity) context));
            return;
        }
        MediationAdLogger.logI("Skip inter ad");
    }

    @Override
    public boolean load(Context context) {
        if (!super.load(context)) {
            return false;
        }

        loadAd(context);
        return true;
    }

    @Override
    protected void onAdLoaded() {
        super.onAdLoaded();
        if (getMediationAdCallback() != null) {
            getMediationAdCallback().onAdLoaded(adPositionName, getMediationNetwork(), MediationAdType.INTERSTITIAL, this);
        }
    }

    @Override
    protected MediationAdNetwork getMediationNetwork() {
        return MediationAdNetwork.ADMOB;
    }


    private void loadAd(Context context) {

        final MediationInterstitialAdCache instance = MediationInterstitialAdCache.instance();
        if (instance.hasCache(adPositionName) && !instance.getAd(adPositionName).isAdShowed()) {
            MediationAdLogger.logD(adPositionName + " has cache instance. Return result now.");
            interstitialAdObj = ((AdMobInterstitialAd) instance.getAd(adPositionName)).interstitialAdObj;
            return;
        }

        super.logRequestAdTime();
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
                                onAdError(adError.getMessage());
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                                AdMobInterstitialAd.super.onAdImpression();
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                AdMobInterstitialAd.super.onAdClosed();
                                interstitialAdObj = null;
                            }

                        });
                        AdMobInterstitialAd.this.onAdLoaded();

                        instance.saveInterstitialAd(adPositionName, AdMobInterstitialAd.this);

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        MediationAdLogger.logE(loadAdError.getMessage());
                        interstitialAdObj = null;
                        onAdError(String.format(Locale.US, "Error %d: %s", loadAdError.getCode(), loadAdError.getMessage()));
                    }
                });
    }

    @Override
    public boolean isAdLoaded() {
        return interstitialAdObj != null;
    }
}
