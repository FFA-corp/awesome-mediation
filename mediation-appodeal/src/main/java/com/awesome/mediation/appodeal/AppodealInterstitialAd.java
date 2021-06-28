package com.awesome.mediation.appodeal;

import android.app.Activity;
import android.content.Context;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.base.MediationInterstitialAd;
import com.awesome.mediation.library.util.MediationAdLogger;

public class AppodealInterstitialAd extends MediationInterstitialAd<Activity> {
    @Override
    public void showAd(Activity context) {
        if (super.canShowAd(context)) {
            super.updateShowingState();
            Appodeal.show(context, Appodeal.INTERSTITIAL);
            return;
        }
        MediationAdLogger.logI("Skip inter ad");
    }

    @Override
    public boolean load(Context context) {
        if (!super.load(context)) {
            return false;
        }
        assert context instanceof Activity;
        Activity activity = (Activity) context;
//        AppodealInitializer.getInstance().enableAutoCache(false, Appodeal.INTERSTITIAL);
        AppodealInitializer.getInstance().initInterstitialAd(activity, new InterstitialCallbacks() {
            @Override
            public void onInterstitialLoaded(boolean isPrecache) {
                onAdLoaded();
            }

            @Override
            public void onInterstitialFailedToLoad() {
                onAdError("Ad load fail");
            }

            @Override
            public void onInterstitialShown() {
                onAdImpression();
            }

            @Override
            public void onInterstitialShowFailed() {
                onAdError("Ad show fail");
            }

            @Override
            public void onInterstitialClicked() {
                onAdClicked();
            }

            @Override
            public void onInterstitialClosed() {
                onAdClosed();
            }

            @Override
            public void onInterstitialExpired() {
                onAdError("Ad is expired");
            }
        });
        return true;
    }

    @Override
    protected MediationAdNetwork getMediationNetwork() {
        return MediationAdNetwork.APPODEAL;
    }

    @Override
    protected void onAdLoaded() {
        super.onAdLoaded();
        if (getMediationAdCallback() != null) {
            getMediationAdCallback().onAdLoaded(getMediationNetwork(), getMediationAdType(), this);
        }
    }


    @Override
    public boolean isAdLoaded() {
        return Appodeal.isLoaded(Appodeal.INTERSTITIAL);
    }
}
