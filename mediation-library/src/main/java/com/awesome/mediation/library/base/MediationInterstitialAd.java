package com.awesome.mediation.library.base;

import android.app.Activity;

import com.awesome.mediation.library.MediationAdType;

public abstract class MediationInterstitialAd<T extends Activity> extends MediationNetworkLoader {
    public abstract void showAd(T context);

    @Override
    protected MediationAdType getMediationAdType() {
        return MediationAdType.INTERSTITIAL;
    }
}
