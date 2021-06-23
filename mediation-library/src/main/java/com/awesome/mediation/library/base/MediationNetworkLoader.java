package com.awesome.mediation.library.base;

import android.content.Context;
import android.util.Log;

import com.awesome.mediation.library.config.MediationAdConfig;
import com.awesome.mediation.library.config.MediationAdManager;
import com.awesome.mediation.library.config.MediationPrefs;
import com.awesome.mediation.library.config.MediationRemoteConfig;
import com.awesome.mediation.library.util.MediationDeviceUtil;

public abstract class MediationNetworkLoader {
    protected String adUnitId;
    private MediationAdCallback<MediationNetworkLoader> mediationAdCallback;
    protected String adPositionName;
    public boolean adShowed = false;

    public void setAdUnitId(String adUnitId) {
        this.adUnitId = adUnitId;
    }

    public boolean load(Context context) {
        MediationAdManager instance = MediationAdManager.getInstance(context);
        boolean canLoadAd = MediationDeviceUtil.isConnected(context) && !instance.getAppDelegate().isAppPurchased()
                && MediationPrefs.instance(context).canRequestAd();

        Log.i("superman", "load: " + canLoadAd);
        if (!canLoadAd) {
            if (getMediationAdCallback() != null) {
                getMediationAdCallback().onAdError("App purchased");
            }
            return false;
        }

        MediationRemoteConfig config = new MediationAdConfig(context).getConfig();
        Log.i("superman", "load: 2");
        if (!config.isLivePlacement(adPositionName)) {
            if (getMediationAdCallback() != null) {
                getMediationAdCallback().onAdError("Placement \"%s\" is disable");
            }
            return false;
        }
        Log.i("superman", "load: 1");
        return true;
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
}
