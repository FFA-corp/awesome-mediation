package com.awesome.mediation.library.base;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

import com.awesome.mediation.library.MediationNativeAdView;

public abstract class MediationNativeAd extends MediationNetworkLoader {
    protected String adTitle;
    protected String adBody;
    protected String adCallToAction;
    protected String adIconUrl;
    protected ViewGroup adMediaView;
    protected ViewGroup adAdChoiceView;
    protected Drawable adIconDrawable;
    protected Class<?> adContainerClass;

    public abstract void showAd(MediationNativeAdView mediationNativeAdView);

    protected void populateDataFromAdLoadedInstance() {
    }

    public String getAdTitle() {
        return adTitle;
    }

    public String getAdBody() {
        return adBody;
    }

    public String getAdCallToAction() {
        return adCallToAction;
    }

    public ViewGroup getAdMediaView() {
        return adMediaView;
    }

    public String getAdIconUrl() {
        return adIconUrl;
    }

    public ViewGroup getAdAdChoiceView() {
        return adAdChoiceView;
    }

    public Drawable getAdIconDrawable() {
        return adIconDrawable;
    }

    public Class<?> getAdContainerClass() {
        return adContainerClass;
    }

    public Object getAdLoadedInstance() {
        return null;
    }

    @Override
    public String toString() {
        return "MediationNativeAd{" +
                "adTitle='" + adTitle + '\'' +
                ", adBody='" + adBody + '\'' +
                ", adCallToAction='" + adCallToAction + '\'' +
                ", adIconUrl='" + adIconUrl + '\'' +
                '}';
    }
}
