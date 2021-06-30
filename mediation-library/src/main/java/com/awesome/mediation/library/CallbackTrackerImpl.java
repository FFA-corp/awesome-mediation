package com.awesome.mediation.library;

import android.content.Context;

import com.awesome.mediation.library.base.MediationAdCallback;
import com.awesome.mediation.library.base.MediationNetworkLoader;
import com.awesome.mediation.library.util.MediationAdEventTracker;

public class CallbackTrackerImpl<T extends MediationNetworkLoader> extends MediationAdCallback<T> {
    private final Context context;

    private static final String SUCCESS = "success";
    private static final String IMPRESS = "impress";
    private static final String CLICK = "click";
    private static final String DISMISS = "dismiss";
    private static final String ERROR = "error";
    private static final String ALL_ERROR = "all_error";

    public CallbackTrackerImpl(Context context) {
        this.context = context;
    }

    @Override
    public void onAdClicked(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
        logEvent(mediationAdNetwork, positionName, CLICK);
    }

    @Override
    public void onAdClosed(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
        logEvent(mediationAdNetwork, positionName, DISMISS);
    }

    @Override
    public void onAdError(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType, String errorMessage) {
        logEvent(mediationAdNetwork, positionName, ERROR);
    }

    @Override
    public void onAdImpression(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
        logEvent(mediationAdNetwork, positionName, IMPRESS);
    }

    @Override
    public void onAdLoaded(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType, T mediationNetworkLoader) {
        logEvent(mediationAdNetwork, positionName, SUCCESS);
    }

    @Override
    public void onAllAdError(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
        logEvent(mediationAdNetwork, positionName, ALL_ERROR);
    }

    private void logEvent(MediationAdNetwork mediationAdNetwork, String adPlacement, String event) {
        MediationAdEventTracker instance = MediationAdEventTracker.instance(context);
        if (ALL_ERROR.equalsIgnoreCase(event)) {
            instance.log(mediationAdNetwork.getAdName() + "_" + adPlacement + "_" + event);
        } else {
            instance.log(adPlacement + "_" + event);
        }
    }

}
