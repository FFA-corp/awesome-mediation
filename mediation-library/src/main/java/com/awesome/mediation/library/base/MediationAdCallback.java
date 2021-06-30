package com.awesome.mediation.library.base;

import android.util.Log;

import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.MediationAdType;

public abstract class MediationAdCallback<T extends MediationNetworkLoader> {
    private static final String TAG = "MediationAdCallback";

    public void onAdClicked(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
        Log.i(TAG, "onAdClicked: " + mediationAdNetwork.getAdName() + " " + positionName);
    }

    public void onAdImpression(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
        Log.i(TAG, "onAdImpression: " + mediationAdNetwork.getAdName() + " " + positionName);
    }

    public void onAdClosed(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
        Log.i(TAG, "onAdClosed: " + mediationAdNetwork.getAdName() + " " + positionName);
    }

    public void onAdError(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType, String errorMessage) {
        Log.i(TAG, "onAdClosed: " + mediationAdNetwork.getAdName() + " " + positionName);
    }

    public void onAllAdError(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
        Log.i(TAG, "onAllAdError: " + mediationAdNetwork.getAdName() + " " + positionName);
    }

    public void onAdLoaded(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType, T mediationNetworkLoader) {
        Log.i(TAG, "onAdLoaded: " + mediationAdNetwork.getAdName() + " " + positionName);
    }

}
