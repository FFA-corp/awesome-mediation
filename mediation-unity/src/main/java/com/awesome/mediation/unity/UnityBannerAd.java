package com.awesome.mediation.unity;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.base.MediationAdCallback;
import com.awesome.mediation.library.base.MediationBannerAd;
import com.awesome.mediation.library.base.MediationNetworkLoader;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

public class UnityBannerAd extends MediationBannerAd {
    private BannerView bannerView;

    @Override
    public boolean load(Context context) {
        if (!super.load(context)) {
            return false;
        }
        bannerView = new BannerView(((Activity) context), adUnitId, new UnityBannerSize(width, height));
        bannerView.setListener(new BannerView.IListener() {
            @Override
            public void onBannerLoaded(BannerView bannerAdView) {
                onAdLoaded();
            }

            @Override
            public void onBannerClick(BannerView bannerAdView) {
                onAdClicked();
            }

            @Override
            public void onBannerFailedToLoad(BannerView bannerAdView, BannerErrorInfo errorInfo) {
                onAdError(errorInfo.errorMessage);
            }

            @Override
            public void onBannerLeftApplication(BannerView bannerView) {
            }
        });
        bannerView.load();
        return true;
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
    protected MediationAdNetwork getMediationNetwork() {
        return MediationAdNetwork.UNITY;
    }

    @Override
    public void destroy() {
        if (bannerView != null) {
            bannerView.destroy();
        }
    }

    @Override
    protected View getBannerAdView() {
        return bannerView;
    }
}
