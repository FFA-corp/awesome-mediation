package com.awesome.mediation.admob;

import android.app.Activity;
import android.content.Context;

import com.awesome.mediation.library.MediationNativeAdView;
import com.awesome.mediation.library.base.MediationNativeAd;
import com.awesome.mediation.library.config.MediationAdConfig;
import com.awesome.mediation.library.config.MediationRemoteConfig;
import com.awesome.mediation.library.util.MediationDeviceUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;

import org.jetbrains.annotations.NotNull;

public class AdMobNativeAd extends MediationNativeAd {
    private NativeAd loadedAd;

    @Override
    public boolean load(Context context) {
        if (!super.load(context)) {
            return false;
        }
        AdLoader adLoader = configAd(context);
        if (adLoader == null || adLoader.isLoading()) {
            return false;
        }
        adLoader.loadAd(AdMobAdUtil.getAdRequestBuilderWithTestDevice(context).build());
        return true;
    }

    @Override
    public void showAd(MediationNativeAdView mediationNativeAdView) {

    }

    public NativeAd getLoadedAd() {
        return loadedAd;
    }

    private AdLoader configAd(Context context) {
        MediationRemoteConfig config = new MediationAdConfig(context).getConfig();
        if (!MediationDeviceUtil.isConnected(context)) {
            onAdLoadFailed("Not connect to internet!");
            return null;
        }
        if (!config.isLiveAdMob(adPositionName) || !config.isLivePlacement(adPositionName)) {
            onAdLoadFailed("Ad is turn off!");
            return null;
        }

        com.google.android.gms.ads.AdLoader.Builder builder = new AdLoader.Builder(context, adUnitId)
                .forNativeAd(unifiedNativeAd -> {
                    boolean adDestroyed = false;
                    if (context instanceof Activity) {
                        if (((Activity) context).isDestroyed()) {
                            unifiedNativeAd.destroy();
                            adDestroyed = true;
                        }
                    }
                    if (adDestroyed) {
                        if (getMediationAdCallback() != null) {
                            getMediationAdCallback().onAdError("Context destroy => ad destroyed!");
                        }
                        return;
                    }
                    this.loadedAd = unifiedNativeAd;
                    if (getMediationAdCallback() != null) {
                        getMediationAdCallback().onAdLoaded(AdMobNativeAd.this);
                    }
                }).withNativeAdOptions(new NativeAdOptions.Builder()
                        .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT).build());
        return builder.withAdListener(new AdListener() {
            @Override
            public void onAdImpression() {
                super.onAdImpression();
                if (getMediationAdCallback() != null) {
                    getMediationAdCallback().onAdImpression();
                }
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                if (getMediationAdCallback() != null) {
                    getMediationAdCallback().onAdClicked();
                }
            }

            @Override
            public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                if (getMediationAdCallback() != null) {
                    getMediationAdCallback().onAdError(loadAdError.getMessage());
                }
            }
        }).build();
    }

    private void onAdLoadFailed(String message) {
        if (this.getMediationAdCallback() != null) {
            this.getMediationAdCallback().onAdError(message);
        }
    }
}

