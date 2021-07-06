package com.awesome.mediation.library;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;

import com.awesome.mediation.library.base.MediationInterstitialAd;

import java.util.HashMap;
import java.util.Map;

public class MediationInterstitialAdCache {
    private static MediationInterstitialAdCache instance;
    private final Map<String, MediationInterstitialAd<Activity>> adCacheMap = new HashMap<>();
    public MutableLiveData<Boolean> showing = new MutableLiveData<>();

    public static MediationInterstitialAdCache instance() {
        if (instance == null) {
            instance = new MediationInterstitialAdCache();
        }
        return instance;
    }

    public void saveInterstitialAd(String position, MediationInterstitialAd<Activity> ad) {
        adCacheMap.put(position, ad);
    }

    public void markShowAd(String position) {
        adCacheMap.remove(position);
    }

    public boolean showAd(Activity activity, String position) {
        MediationInterstitialAd<Activity> interstitialAd = adCacheMap.get(position);
        if (interstitialAd != null) {
            interstitialAd.showAd(activity);
            return true;
        }
        return false;
    }

    public void destroy() {
        adCacheMap.clear();
    }

    public boolean hasCache(String adPosition) {
        return adCacheMap.containsKey(adPosition);
    }

    public MediationInterstitialAd<Activity> getAd(String adPosition) {
        return adCacheMap.get(adPosition);
    }
}
