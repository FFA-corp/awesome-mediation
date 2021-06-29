package com.awesome.mediation.admob;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.awesome.mediation.admob.util.AdMobAdUtil;
import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.base.MediationAdCallback;
import com.awesome.mediation.library.base.MediationBannerAd;
import com.awesome.mediation.library.base.MediationNetworkLoader;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

public class AdMobBannerAd extends MediationBannerAd {

    private AdView adView;

    @Override
    protected MediationAdNetwork getMediationNetwork() {
        return MediationAdNetwork.ADMOB;
    }

    @Override
    protected void onAdLoaded() {
        super.onAdLoaded();
        MediationAdCallback<MediationNetworkLoader> mediationAdCallback = getMediationAdCallback();
        if (mediationAdCallback != null) {
            mediationAdCallback.onAdLoaded(adPositionName, getMediationNetwork(), getMediationAdType(), this);
        }
    }

    @Override
    public boolean load(Context context) {
        if (!super.load(context)) {
            return false;
        }
        adView = new AdView(context);
        adView.setAdUnitId(adUnitId);
        adView.setAdSize(new AdSize(width, height));
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                AdMobBannerAd.this.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                AdMobBannerAd.this.onAdError(loadAdError.getMessage());
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                AdMobBannerAd.this.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                AdMobBannerAd.this.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                AdMobBannerAd.this.onAdImpression();
            }
        });
        adView.loadAd(AdMobAdUtil.getAdRequestBuilderWithTestDevice(context).build());
        return true;
    }

    @Override
    public void destroy() {
        if (adView != null) {
            adView.destroy();
        }
    }

    @Override
    protected View getBannerAdView() {
        return adView;
    }
}
