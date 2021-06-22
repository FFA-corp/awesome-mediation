package com.awesome.mediation.library.util;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MediationFirebaseConfigFetcher {
    private static MediationFirebaseConfigFetcher instance;
    private FirebaseRemoteConfig firebaseRemoteConfig;

    public static MediationFirebaseConfigFetcher instance() {
        if (instance == null) {
            instance = new MediationFirebaseConfigFetcher();
        }
        return instance;
    }

    public void fetch(OnFetchListener onFetchListener) {
        fetch(onFetchListener, -1);
    }

    public void fetch(OnFetchListener onFetchListener, long timeout) {
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings.Builder builder = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0);
        if (timeout > 0) {
            builder.setFetchTimeoutInSeconds(timeout);
        }
        FirebaseRemoteConfigSettings configSettings = builder
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(task -> {
                    MediationAdLogger.logD("Fetch configs successfully");
                    if (task.isSuccessful()) {
                        firebaseRemoteConfig.activate();

                        if (onFetchListener != null) {
                            onFetchListener.onSuccess(firebaseRemoteConfig);
                        }
                        return;
                    }
                    if (onFetchListener != null) {
                        onFetchListener.onFail();
                    }
                }).addOnFailureListener(e -> {
            MediationAdLogger.logE(e);
            e.printStackTrace();
        });
    }

    public void reset() {
        try {
            this.firebaseRemoteConfig.reset();
            this.fetch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetch() {
        fetch(null);
    }

    public interface OnFetchListener {
        void onSuccess(FirebaseRemoteConfig firebaseRemoteConfig);

        void onFail();
    }
}
