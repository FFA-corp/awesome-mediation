package com.awesome.mediation.appodeal;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.Native;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeAdView;
import com.appodeal.ads.NativeCallbacks;
import com.appodeal.ads.NativeIconView;
import com.appodeal.ads.NativeMediaView;
import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.MediationNativeAdView;
import com.awesome.mediation.library.base.MediationNativeAd;

import java.util.List;

public class AppodealNativeAd extends MediationNativeAd {

    private static final long TIMEOUT = 7000;
    private com.appodeal.ads.NativeAd loadedAd;
    private final Handler handler = new Handler();
    private boolean destroyed;

    @Override
    public boolean load(Context context) {
        adLoaded = false;
        if (!super.load(context)) {
            return false;
        }

        Appodeal.setRequiredNativeMediaAssetType(Native.MediaAssetType.ALL);
        if (isAdLoaded()) {
            destroyHandler();
            onAdLoaded();
            return true;
        }

        handler.postDelayed(() -> {
            onAdError("Ad is timeout");
            destroy();
        }, TIMEOUT);

        AppodealInitializer.getInstance().initNativeAd(((Activity) context), new NativeCallbacks() {
            @Override
            public void onNativeLoaded() {
                Log.i("superman", "onNativeLoaded: ");
                destroyHandler();
                if (!adLoaded && !destroyed) {
                    onAdLoaded();
                    adLoaded = true;
                }
            }

            @Override
            public void onNativeFailedToLoad() {
                onAdError("Ad load fail");
                destroyHandler();
            }

            @Override
            public void onNativeShown(com.appodeal.ads.NativeAd nativeAd) {
            }

            @Override
            public void onNativeShowFailed(com.appodeal.ads.NativeAd nativeAd) {
                onAdError("Ad show fail");
                destroyHandler();
            }

            @Override
            public void onNativeClicked(com.appodeal.ads.NativeAd nativeAd) {
                onAdClicked();
            }

            @Override
            public void onNativeExpired() {
                onAdError("Ad is expired");
            }
        });

        return true;
    }

    @Override
    protected void onAdLoaded() {
        populateDataFromAdLoadedInstance(context);
        if (getMediationAdCallback() != null) {
            getMediationAdCallback().onAdLoaded(adPositionName, getMediationNetwork(), getMediationAdType(), this);
        }
    }

    @Override
    protected MediationAdNetwork getMediationNetwork() {
        return MediationAdNetwork.APPODEAL;
    }

    @Override
    public void showAd(MediationNativeAdView mediationNativeAdView) {
        onAdImpression();
        mediationNativeAdView.show(this);
    }

    @Override
    protected void populateDataFromAdLoadedInstance(Context context) {
        List<NativeAd> nativeAds = Appodeal.getNativeAds(1);
        this.loadedAd = nativeAds.get(0);
        super.adTitle = loadedAd.getTitle();
        super.adBody = loadedAd.getDescription();
        super.adCallToAction = loadedAd.getCallToAction();

        View providerView = loadedAd.getProviderView(context);
        if (providerView != null) {
            if (providerView.getParent() != null && providerView.getParent() instanceof ViewGroup) {
                ((ViewGroup) providerView.getParent()).removeView(providerView);
            }
            FrameLayout providerViewContainer = new FrameLayout(context);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            providerViewContainer.addView(providerView, layoutParams);
            super.adAdChoiceView = providerViewContainer;
        }
        super.adMediaView = new NativeMediaView(context);
        this.adMediaView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        super.adIconView = new NativeIconView(context);
    }

    @Override
    public Object getAdLoadedInstance() {
        return loadedAd;
    }

    @Override
    public void destroy() {
        if (loadedAd != null) {
            loadedAd.destroy();
        }
        destroyHandler();
        Appodeal.destroy(Appodeal.NATIVE);
        destroyed = true;
    }

    private void destroyHandler() {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public Class<?> getAdContainerClass() {
        return NativeAdView.class;
    }

    @Override
    public boolean isAdLoaded() {
        return Appodeal.isLoaded(Appodeal.NATIVE);
    }
}
