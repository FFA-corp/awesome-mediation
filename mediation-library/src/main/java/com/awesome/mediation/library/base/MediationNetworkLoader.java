package com.awesome.mediation.library.base;

import android.content.Context;
import android.util.Log;

public abstract class MediationNetworkLoader {
    protected String adUnitId;

    public void setAdUnitId(String adUnitId) {
        this.adUnitId = adUnitId;
    }

    public  void load(Context context){
        Log.i("superman", "load: ");
    }
}
