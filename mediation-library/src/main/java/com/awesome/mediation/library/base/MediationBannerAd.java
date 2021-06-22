package com.awesome.mediation.library.base;

import android.view.View;
import androidx.annotation.NonNull;

public abstract class MediationBannerAd extends MediationNetworkLoader{
    @NonNull
    public abstract View getView();
}
