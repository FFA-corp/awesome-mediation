package com.awesome.mediation.library.base;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.MediationAdType;
import com.awesome.mediation.library.MediationInterstitialAdCache;
import com.awesome.mediation.library.config.MediationAdConfig;
import com.awesome.mediation.library.config.MediationAdManager;
import com.awesome.mediation.library.config.MediationPrefs;
import com.awesome.mediation.library.config.MediationRemoteConfig;
import com.awesome.mediation.library.util.MediationAdLogger;
import com.awesome.mediation.library.util.MediationDeviceUtil;

import java.util.Locale;

public abstract class MediationNetworkLoader {
    protected String adUnitId;
    private MediationAdCallback<MediationNetworkLoader> mediationAdCallback;
    protected String adPositionName;
    protected boolean adShowed = false;
    protected boolean adLoaded = false;
    protected Context context;

    public void setAdUnitId(String adUnitId) {
        this.adUnitId = adUnitId;
    }

    public boolean load(Context context) {
        this.context = context;
        this.adLoaded = false;
        if (getMediationNetwork() != MediationAdNetwork.APPODEAL && TextUtils.isEmpty(adUnitId)) {
            onAdError("Ad unit is empty");
            return false;
        }

        MediationPrefs instance = MediationPrefs.instance(context);
        boolean isInterAd = getMediationAdType() == MediationAdType.INTERSTITIAL;
        this.showDebugMessageIfPossible(context, instance, isInterAd);

        if (isInterAd && !instance.canRequestInterAd()) {
            return false;
        }

        if (!canLoadAd(context)) {
            onAdError("Can't load ad or App purchased");
            return false;
        }

        MediationRemoteConfig config = new MediationAdConfig(context).getConfig();
        if (!config.isLivePlacement(adPositionName) || !config.isLivePosition(getMediationNetwork(), adPositionName)) {
            onAdError(String.format(Locale.US, "Placement \"%s\" is disable", adPositionName));
            return false;
        }
        return true;
    }

    private void showDebugMessageIfPossible(Context context, MediationPrefs instance, boolean isInterAd) {
        if (isInterAd) {
            String message = "Last request \"" + adPositionName + "\" at " + instance.getLastInterAdRequestTime() + "\n"
                    + "Time delay: " + instance.getTimeItDelay() + "s | time used: "
                    + ((System.currentTimeMillis() - instance.getLastInterAdRequestTime()) / 1000) + "s\n"
                    + "Can request ads: " + instance.canRequestInterAd();
            if (MediationAdManager.getInstance(context).isDebugWithToastMode()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
            MediationAdLogger.logI(message);
        }
    }

    protected void logRequestAdTime() {
        MediationPrefs.instance(context).saveLastInterAdRequestTime();
    }

    protected void onAdError(String message) {
        MediationAdLogger.logE(getMediationNetwork() + " " + adPositionName + ": " + message);
        if (getMediationAdCallback() != null) {
            getMediationAdCallback().onAdError(adPositionName, getMediationNetwork(), getMediationAdType(), message);
        }
    }

    public void updateShowingState() {
        MediationInterstitialAdCache.instance().showing.postValue(true);
        this.adShowed = true;
    }

    protected boolean canShowAd(Context context) {
        if (!MediationDeviceUtil.isConnected(context) || !isAdLoaded()) {
            return false;
        }
        return !MediationAdManager.getInstance(context).getAppDelegate().isAppPurchased();
    }

    protected void onAdClicked() {
        MediationAdLogger.showCurrentMethodName();
        if (getMediationAdCallback() != null) {
            getMediationAdCallback().onAdClicked(adPositionName, getMediationNetwork(), getMediationAdType());
        }
    }

    protected void onAdImpression() {
        MediationAdLogger.showCurrentMethodName();
        if (getMediationAdType() == MediationAdType.INTERSTITIAL) {
            adShowed = true;
            MediationInterstitialAdCache.instance().showing.postValue(true);
        }
        if (getMediationAdCallback() != null) {
            getMediationAdCallback().onAdImpression(adPositionName, getMediationNetwork(), getMediationAdType());
        }
    }

    protected void onAdClosed() {
        MediationAdLogger.showCurrentMethodName();

        if (getMediationAdType() == MediationAdType.INTERSTITIAL) {
            MediationInterstitialAdCache instance = MediationInterstitialAdCache.instance();
            instance.markShowAd(adPositionName);
            instance.showing.setValue(false);
            instance.showing.postValue(false);
        }
        if (getMediationAdCallback() != null) {
            getMediationAdCallback().onAdClosed(adPositionName, getMediationNetwork(), getMediationAdType());
        }
    }

    protected void onAdLoaded() {
        MediationAdLogger.logD(getMediationNetwork() + " " + adPositionName);
        adLoaded = true;
    }

    protected abstract MediationAdNetwork getMediationNetwork();

    protected abstract MediationAdType getMediationAdType();

    protected boolean canLoadAd(Context context) {
        return MediationDeviceUtil.isConnected(context)
                && !MediationAdManager.getInstance(context).getAppDelegate().isAppPurchased();
    }

    public void setAdLoaderCallback(MediationAdCallback<MediationNetworkLoader> mediationAdCallback) {
        this.mediationAdCallback = mediationAdCallback;
    }

    public MediationAdCallback<MediationNetworkLoader> getMediationAdCallback() {
        return mediationAdCallback;
    }

    public void setAdPositionName(String adPositionName) {
        this.adPositionName = adPositionName;
    }

    public boolean isAdShowed() {
        return adShowed;
    }

    public boolean isAdLoaded() {
        return adLoaded;
    }
}
