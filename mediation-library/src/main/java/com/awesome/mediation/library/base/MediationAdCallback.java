package com.awesome.mediation.library.base;

import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.MediationAdType;

public abstract class MediationAdCallback<T extends MediationNetworkLoader> {
    public void onAdClicked(MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
    }

    public void onAdImpression(MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
    }

    public void onAdClosed(MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
    }

    public void onAdError(MediationAdNetwork mediationAdNetwork, MediationAdType adType, String errorMessage) {
    }

    public void onAdLoaded(MediationAdNetwork mediationAdNetwork, MediationAdType adType, T mediationNetworkLoader) {
    }

}
