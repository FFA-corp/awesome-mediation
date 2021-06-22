package com.awesome.mediation.unity;

import android.content.Context;

import com.awesome.mediation.library.base.MediationInterstitialAd;
import com.awesome.mediation.library.util.MediationAdLogger;

public class UnityInterstitialAd extends MediationInterstitialAd {

    @Override
    public void showAd(Context context) {

    }

    @Override
    public void load(Context context) {
        MediationAdLogger.logI("load");
    }
}
