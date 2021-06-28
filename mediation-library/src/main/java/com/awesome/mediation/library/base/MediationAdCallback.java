package com.awesome.mediation.library.base;

import android.util.Log;

import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.MediationAdType;

public abstract class MediationAdCallback<T extends MediationNetworkLoader> {
    private static final String TAG = "MediationAdCallback";

    public void onAdClicked(MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
        Log.i(TAG, "onAdClicked: " + mediationAdNetwork.getAdName() + " " + adType);
    }

    public void onAdImpression(MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
        Log.i(TAG, "onAdImpression: " + mediationAdNetwork.getAdName() + " " + adType);
    }

    public void onAdClosed(MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
        Log.i(TAG, "onAdClosed: " + mediationAdNetwork.getAdName() + " " + adType);
    }

    public void onAdError(MediationAdNetwork mediationAdNetwork, MediationAdType adType, String errorMessage) {
        Log.i(TAG, "onAdClosed: " + mediationAdNetwork.getAdName() + " " + adType);
    }

    public void onAdLoaded(MediationAdNetwork mediationAdNetwork, MediationAdType adType, T mediationNetworkLoader) {
        Log.i(TAG, "onAdLoaded: " + mediationAdNetwork.getAdName() + " " + adType);
    }

}
