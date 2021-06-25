package com.awesome.mediation.unity;

import android.content.Context;

import com.awesome.mediation.library.config.MediationAdManager;
import com.awesome.mediation.library.util.MediationAdLogger;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

public class UnityInitializer {

    private static UnityInitializer unityInitializerInstance;

    public static synchronized UnityInitializer getInstance() {
        if (unityInitializerInstance == null) {
            unityInitializerInstance = new UnityInitializer();
        }
        return unityInitializerInstance;
    }

    public void initializeUnityAds(Context context, String gameId, IUnityAdsInitializationListener
            initializationListener) {

        if (UnityAds.isInitialized()) {
            initializationListener.onInitializationComplete();
            return;
        }
        boolean testMode = MediationAdManager.getInstance(context).isDebugMode();
        UnityAds.initialize(context, gameId, testMode, true, new IUnityAdsInitializationListener() {
            @Override
            public void onInitializationComplete() {
                MediationAdLogger.showCurrentMethodName();
                if (initializationListener != null) {
                    initializationListener.onInitializationComplete();
                }
            }

            @Override
            public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                MediationAdLogger.logE(error.name() + ": " + message);
                if (initializationListener != null) {
                    initializationListener.onInitializationFailed(error, message);
                }
            }
        });
    }
}
