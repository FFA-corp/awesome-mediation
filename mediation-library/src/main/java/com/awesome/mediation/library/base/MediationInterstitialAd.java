package com.awesome.mediation.library.base;

import android.app.Activity;
import android.content.Context;

import com.awesome.mediation.library.MediationAdType;
import com.awesome.mediation.library.MediationInterstitialAdCache;
import com.awesome.mediation.library.config.MediationAdManager;
import com.awesome.mediation.library.util.MediationDeviceUtil;

public abstract class MediationInterstitialAd<T extends Activity> extends MediationNetworkLoader {
    public abstract void showAd(T context);

    public void updateShowingState() {
        MediationInterstitialAdCache.instance().showing.postValue(true);
        adShowed = true;
    }

    protected boolean canShowAd(Context context) {
        if (!MediationDeviceUtil.isConnected(context) || !isAdLoaded()) {
            return false;
        }
        return !MediationAdManager.getInstance(context).getAppDelegate().isAppPurchased();
    }

    @Override
    protected MediationAdType getMediationAdType() {
        return MediationAdType.INTERSTITIAL;
    }
}
