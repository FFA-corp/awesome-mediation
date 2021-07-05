package com.awesome.mediautionu;

import android.app.Application;
import android.util.Log;

import com.awesome.mediation.admob.AdMobAppOpenManager;
import com.awesome.mediation.appodeal.AppodealInitializer;
import com.awesome.mediation.library.MediationAppDelegate;
import com.awesome.mediation.library.config.MediationAdConfig;
import com.awesome.mediation.library.config.MediationAdManager;
import com.awesome.mediation.library.config.MediationRemoteConfig;
import com.awesome.mediation.unity.UnityInitializer;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;

public class MainApp extends Application {
    public AdMobAppOpenManager appOpenManager;

    @Override
    public void onCreate() {
        super.onCreate();
        initMediationAds();
//        initOpenApp();

    }

    private void initMediationAds() {

        UnityInitializer.getInstance().initializeUnityAds(this, "4185851", new IUnityAdsInitializationListener() {
            @Override
            public void onInitializationComplete() {
                Log.i("superman", "onInitializationComplete: ");
            }

            @Override
            public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                Log.i("superman", "onInitializationFailed: ");
            }
        });

        MediationAdManager.init(this, BuildConfig.DEBUG);
        AppodealInitializer.init("a09ff3fbe33f1be47614a928684d998d94f895e1e431052b", true);
        MediationAdManager.getInstance(this)
                .setDebugWithToastMode(true)
                .setAppDelegate(new MediationAppDelegate() {
                    @Override
                    public boolean isPolicyAccepted() {
                        return true;
                    }

                    @Override
                    public boolean isAppPurchased() {
                        return false;
                    }

                    @Override
                    public boolean isLocalAppPurchasedState() {
                        return false;
                    }
                });
    }

    private void initOpenApp() {
        MediationRemoteConfig config = MediationAdConfig.newInstance(this).getConfig();
        String oaReturn = config.getAdMobOpenAdUnit("oa_return", "ca-app-pub-3940256099942544/3419835294");
        appOpenManager = new AdMobAppOpenManager(this, oaReturn, isEnableOpenAppAd());
    }

    private boolean isEnableOpenAppAd() {
        return MediationAdConfig.newInstance(this).getConfig().isLivePlacement("oa_return");
    }
}
