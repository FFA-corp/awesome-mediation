package com.awesome.mediautionu;

import android.app.Application;

import com.awesome.mediation.library.MediationAppDelegate;
import com.awesome.mediation.library.config.MediationAdManager;

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MediationAdManager.init(this, BuildConfig.DEBUG);
        MediationAdManager.getInstance(this)
                .setAppDelegate(new MediationAppDelegate() {
                    @Override
                    public boolean isPolicyAccepted() {
                        return false;
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
}
