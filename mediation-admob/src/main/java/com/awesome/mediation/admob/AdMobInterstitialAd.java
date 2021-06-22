package com.awesome.mediation.admob;


import android.content.Context;

import com.awesome.mediation.library.base.MediationInterstitialAd;
import com.awesome.mediation.library.util.MediationAdLogger;

public class AdMobInterstitialAd extends MediationInterstitialAd {
    @Override
    protected void showAd(Context context) {
    }

    @Override
    public void load(Context context) {
        MediationAdLogger.logI("load");
    }
}
