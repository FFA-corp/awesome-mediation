package com.awesome.mediation.unity;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.base.MediationAdCallback;
import com.awesome.mediation.library.base.MediationBannerAd;
import com.awesome.mediation.library.base.MediationNetworkLoader;
import com.awesome.mediation.library.util.MediationAdLogger;

public class UnityBannerAd extends MediationBannerAd {
    @NonNull
    @Override
    public View getView() {
        return null;
    }

    @Override
    public boolean load(Context context) {
        MediationAdLogger.logI("load");
        return super.load(context);
    }

    @Override
    protected void onAdLoaded() {
        super.onAdLoaded();
        MediationAdCallback<MediationNetworkLoader> mediationAdCallback = getMediationAdCallback();
        if (mediationAdCallback != null) {
            mediationAdCallback.onAdLoaded(getMediationNetwork(), getMediationAdType(), this);
        }
    }

    @Override
    protected MediationAdNetwork getMediationNetwork() {
        return MediationAdNetwork.UNITY;
    }
}
