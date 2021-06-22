package com.awesome.mediation.library.base;

import android.content.Context;
import android.util.Log;

public abstract class MediationNetworkLoader{
    protected String adUnitId;
    private MediationAdCallback mediationAdCallback;

    public void setAdUnitId(String adUnitId) {
        this.adUnitId = adUnitId;
    }

    public  void load(Context context){
        Log.i("superman", "load: ");
    }

    public void setAdLoaderCallback(MediationAdCallback mediationAdCallback) {
        this.mediationAdCallback = mediationAdCallback;
    }

    public MediationAdCallback getMediationAdCallback() {
        return mediationAdCallback;
    }
}
