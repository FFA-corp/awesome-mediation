package com.awesome.mediation.library.base;

public abstract class MediationAdCallback<T extends MediationNetworkLoader> {
    public void onAdClicked() {
    }

    public void onAdImpression() {
    }

    public void onAdClosed() {
    }

    public void onAdError() {
    }

    public void onAdLoaded(T mediationNetworkLoader) {
    }
}
