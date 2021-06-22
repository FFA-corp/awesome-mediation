package com.awesome.mediation.unity;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.awesome.mediation.library.base.MediationBannerAd;
import com.awesome.mediation.library.util.MediationAdLogger;

public class UnityBannerAd extends MediationBannerAd {
    @NonNull
    @Override
    public View getView() {
        return null;
    }

    @Override
    public void load(Context context) {
        MediationAdLogger.logI("load");
    }
}
