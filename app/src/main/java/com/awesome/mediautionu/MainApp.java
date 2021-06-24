package com.awesome.mediautionu;

import android.app.Application;

import com.awesome.mediation.admob.AdMobAppOpenManager;
import com.awesome.mediation.library.MediationAppDelegate;
import com.awesome.mediation.library.config.MediationAdConfig;
import com.awesome.mediation.library.config.MediationAdManager;
import com.awesome.mediation.library.config.MediationRemoteConfig;

public class MainApp extends Application {
    public AdMobAppOpenManager appOpenManager;

    @Override
    public void onCreate() {
        super.onCreate();
        initMediationAds();
//        initOpenApp();
    }

    private void initMediationAds() {
        MediationAdManager.init(this, BuildConfig.DEBUG);
        MediationAdManager.getInstance(this)
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
