package com.awesome.mediation.library.base;

import android.content.Context;

import androidx.annotation.NonNull;

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
    public void onAdClosed() {
        super.onAdClosed();
        logEvent(adPlacement, DISMISS);
    }

    private void logEvent(String adPlacement, String event) {
        MediationAdEventTracker.instance(context).log("gad_" + adPlacement + "_" + event);
    }

    @Override
    public void onAdError(String errorMessage) {
        super.onAdError(errorMessage);
        MediationAdLogger.logE("Error " + errorMessage);
        logEvent(adPlacement, ERROR);
    }

    @Override
    public void onAdLoaded(T loader) {
        super.onAdLoaded(loader);
        logEvent(adPlacement, SUCCESS);
    }

    @Override
    public void onAdClicked() {
        super.onAdClicked();
        logEvent(adPlacement, CLICK);
    }

    @Override
    public void onAdImpression() {
        super.onAdImpression();
        logEvent(adPlacement, IMPRESS);
    }
}
