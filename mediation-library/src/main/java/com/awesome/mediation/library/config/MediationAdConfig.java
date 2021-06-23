package com.awesome.mediation.library.config;

import android.content.Context;

public class MediationAdConfig {
    private final MediationRemoteConfig config;

    public MediationAdConfig(Context context) {
        this.config = new MediationAdRemoteConfig(context);
    }

    public MediationRemoteConfig getConfig() {
        return config;
    }

    public static MediationAdConfig newInstance(Context context) {
        return new MediationAdConfig(context);
    }
}
