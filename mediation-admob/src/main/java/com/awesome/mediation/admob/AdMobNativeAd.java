package com.awesome.mediation.admob;

import android.app.Activity;
import android.content.Context;

import com.awesome.mediation.admob.util.AdMobAdUtil;
import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.MediationNativeAdView;
import com.awesome.mediation.library.base.MediationNativeAd;
import com.awesome.mediation.library.config.MediationAdConfig;
import com.awesome.mediation.library.config.MediationRemoteConfig;
import com.awesome.mediation.library.util.MediationDeviceUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.AdChoicesView;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
    protected void onAdLoaded() {
        super.onAdLoaded();
        getMediationAdCallback().onAdLoaded(getMediationNetwork(), getMediationAdType(), this);
    }

    @Override
    protected MediationAdNetwork getMediationNetwork() {
        return MediationAdNetwork.ADMOB;
    }

    @Override
    public void showAd(MediationNativeAdView mediationNativeAdView) {
        mediationNativeAdView.show(this);
    }

    private AdLoader configAd(Context context) {
        MediationRemoteConfig config = new MediationAdConfig(context).getConfig();
        if (!MediationDeviceUtil.isConnected(context)) {
            onAdError("Not connect to internet!");
            return null;
        }
        if (!config.isLiveAdMob(adPositionName) || !config.isLivePlacement(adPositionName)) {
            onAdError("Ad is turn off!");
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
                        onAdError("Context destroy => ad destroyed!");
                        return;
                    }
                    this.loadedAd = unifiedNativeAd;
                    populateDataFromAdLoadedInstance(context);

                    onAdLoaded();
                }).withNativeAdOptions(new NativeAdOptions.Builder()
                        .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT).build());
        return builder.withAdListener(new AdListener() {
            @Override
            public void onAdImpression() {
                super.onAdImpression();
                adShowed = true;
                AdMobNativeAd.super.onAdImpression();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                AdMobNativeAd.this.onAdClicked();
            }

            @Override
            public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                onAdError(loadAdError.getMessage());
            }
        }).build();
    }

    protected void populateDataFromAdLoadedInstance(Context context) {
        if (loadedAd == null) {
            return;
        }
        super.adBody = loadedAd.getBody();
        super.adCallToAction = loadedAd.getCallToAction();
        super.adTitle = loadedAd.getHeadline();
        NativeAd.AdChoicesInfo adChoicesInfo = loadedAd.getAdChoicesInfo();
        if (adChoicesInfo != null) {
            super.adAdChoiceView = new AdChoicesView(context);
        }
        super.adMediaView = new MediaView(context);
        NativeAd.Image icon = loadedAd.getIcon();
        if (icon != null) {
            super.adIconDrawable = icon.getDrawable();
        } else {
            List<NativeAd.Image> images = loadedAd.getImages();
            if (images.size() > 0) {
                NativeAd.Image image = images.get(0);
                if (image != null) {
                    super.adIconDrawable = image.getDrawable();
                }
            }
        }
    }

    @Override
    public Object getAdLoadedInstance() {
        return loadedAd;
    }

    @Override
    public Class<?> getAdContainerClass() {
        return NativeAdView.class;
    }
}

