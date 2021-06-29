package com.awesome.mediation.library.base;

import android.content.Context;

import androidx.annotation.NonNull;

import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.MediationAdType;
import com.awesome.mediation.library.util.MediationAdEventTracker;
import com.awesome.mediation.library.util.MediationAdLogger;


public class MediationAdCallbackImpl<T extends MediationNetworkLoader> extends MediationAdCallback<T> {

    private static final String SUCCESS = "success";
    private static final String IMPRESS = "impress";
    private static final String CLICK = "click";
    private static final String DISMISS = "dismiss";
    private static final String ERROR = "error";

    private final String adPlacement;
    private final Context context;

    public MediationAdCallbackImpl(Context context, @NonNull String adPlacement) {
        super();
        this.adPlacement = adPlacement;
        this.context = context;
    }

    @Override
    public void onAdClicked(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
        super.onAdClicked(positionName, mediationAdNetwork, adType);
        logEvent(mediationAdNetwork.getAdName(), adPlacement, CLICK);
    }

    @Override
    public void onAdClosed(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
        super.onAdClosed(positionName, mediationAdNetwork, adType);
        logEvent(mediationAdNetwork.getAdName(), adPlacement, DISMISS);
    }

    @Override
    public void onAdError(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType, String errorMessage) {
        super.onAdError(positionName, mediationAdNetwork, adType, errorMessage);
        MediationAdLogger.logE("Error " + errorMessage);
        logEvent(mediationAdNetwork.getAdName(), adPlacement, ERROR);
    }

    @Override
    public void onAdImpression(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
        super.onAdImpression(positionName, mediationAdNetwork, adType);
        logEvent(mediationAdNetwork.getAdName(), adPlacement, IMPRESS);
    }

    @Override
    public void onAdLoaded(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType, T mediationNetworkLoader) {
        super.onAdLoaded(positionName, mediationAdNetwork, adType, mediationNetworkLoader);
        logEvent(mediationAdNetwork.getAdName(), adPlacement, SUCCESS);
    }

    private void logEvent(String prefix, String adPlacement, String event) {
        MediationAdEventTracker.instance(context).log(prefix + "_" + adPlacement + "_" + event);
    }
}
