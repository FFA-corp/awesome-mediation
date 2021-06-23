package com.awesome.mediation.library.base;

import android.content.Context;

import com.awesome.mediation.library.MediationNativeAdView;

public abstract class MediationNativeAd extends MediationNetworkLoader {
    public abstract void showAd(MediationNativeAdView mediationNativeAdView);

    public String getHeadline() {
        return null;
    }

    public String getBody() {
        return null;
    }

    public String getCallToAction() {
        return null;
    }
}
