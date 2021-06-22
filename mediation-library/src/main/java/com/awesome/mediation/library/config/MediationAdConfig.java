package com.awesome.mediation.library.config;

import android.content.Context;

public class MediationAdConfig {
    private final MediationConfig config;

    public MediationAdConfig(Context context) {
        this.config = new MediationAdRemoteConfig(context);
    }

    public MediationConfig getConfig() {
        return config;
    }

    public static MediationAdConfig newInstance(Context context) {
        return new MediationAdConfig(context);
    }
}
