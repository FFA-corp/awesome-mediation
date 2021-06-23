package com.awesome.mediation.unity;

import android.content.Context;
import android.util.Log;

import com.awesome.mediation.library.base.MediationInterstitialAd;
import com.awesome.mediation.library.util.MediationAdLogger;

public class UnityInterstitialAd extends MediationInterstitialAd {

    @Override
    public void showAd(Context context) {
        MediationAdLogger.logI("showAd");
    }

    @Override
    public boolean load(Context context) {
        if (!super.load(context)) {
            return false;
        }
        return true;
    }
}
