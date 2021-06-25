package com.awesome.mediation.library.base;

import android.view.View;
import android.view.ViewGroup;

import com.awesome.mediation.library.MediationAdType;
import com.awesome.mediation.library.util.MediationAdLogger;

public abstract class MediationBannerAd extends MediationNetworkLoader {
    protected int width = 320;
    protected int height = 50;
    private ViewGroup bannerRootView;

    public abstract void destroy();

    public void showView(ViewGroup bannerRootView) {
        this.bannerRootView = bannerRootView;
        View bannerAdView = getBannerAdView();
        if (bannerRootView == null || bannerAdView == null) {
            MediationAdLogger.logD("View is null");
            return;
        }
        bannerRootView.addView(bannerAdView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    protected abstract View getBannerAdView();

    public void hideView() {
        if (this.bannerRootView != null) {
            this.bannerRootView.removeAllViews();
        }
    }

    public MediationBannerAd setDimension(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    @Override
    protected MediationAdType getMediationAdType() {
        return MediationAdType.BANNER;
    }
}
