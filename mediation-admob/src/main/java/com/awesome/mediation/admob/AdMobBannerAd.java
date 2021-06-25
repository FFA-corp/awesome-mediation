package com.awesome.mediation.admob;

import android.view.View;

import androidx.annotation.NonNull;

import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.base.MediationAdCallback;
import com.awesome.mediation.library.base.MediationBannerAd;
import com.awesome.mediation.library.base.MediationNetworkLoader;

public class AdMobBannerAd extends MediationBannerAd {
    @NonNull
    @Override
    public View getView() {
        return null;
    }

    @Override
    protected MediationAdNetwork getMediationNetwork() {
        return MediationAdNetwork.ADMOB;
    }

    @Override
    protected void onAdLoaded() {
        super.onAdLoaded();
        MediationAdCallback<MediationNetworkLoader> mediationAdCallback = getMediationAdCallback();
        if (mediationAdCallback != null) {
            mediationAdCallback.onAdLoaded(getMediationNetwork(), getMediationAdType(), this);
        }
    }
}
