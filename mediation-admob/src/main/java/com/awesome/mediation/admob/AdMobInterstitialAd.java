package com.awesome.mediation.admob;


import android.content.Context;

import com.awesome.mediation.library.base.MediationInterstitialAd;
import com.awesome.mediation.library.util.MediationAdLogger;

public class AdMobInterstitialAd extends MediationInterstitialAd {
    @Override
    public void showAd(Context context) {
        MediationAdLogger.logI("showAd");
    }

    @Override
    public void load(Context context) {
        MediationAdLogger.logI("load");

        getMediationAdCallback().onAdLoaded(AdMobInterstitialAd.this);
    }
}
