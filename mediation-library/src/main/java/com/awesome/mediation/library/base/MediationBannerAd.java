package com.awesome.mediation.library.base;

import android.view.View;

import androidx.annotation.NonNull;

import com.awesome.mediation.library.MediationAdType;

public abstract class MediationBannerAd extends MediationNetworkLoader {
    @NonNull
    public abstract View getView();

    @Override
    protected MediationAdType getMediationAdType() {
        return MediationAdType.BANNER;
    }
}
