package com.awesome.mediation.library.base;

import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.MediationAdType;

public abstract class MediationAdCallback<T extends MediationNetworkLoader> {

    public void onAdClicked(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
    }

    public void onAdImpression(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
    }

    public void onAdClosed(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
    }

    public void onAdError(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType, String errorMessage) {
    }

    public void onAllAdError(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
    }

    public void onAdLoaded(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType, T mediationNetworkLoader) {
    }

}
