package com.awesome.mediation.library.config;

import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.util.MediationFirebaseConfigFetcher;

public interface MediationRemoteConfig {

    void fetch();

    void fetch(MediationFirebaseConfigFetcher.OnFetchListener fetchListener);

    boolean isLivePlacement(String key);

    boolean isLivePlacement(String key, boolean defaultValue);

    boolean isLivePosition(MediationAdNetwork adNetwork, String adPosition);


    boolean isLiveAdMob(String key, boolean defaultValue);

    boolean isLiveAdMob(String key);

    String getAdMobInterAdUnit(String key, String defaultVal);

    String getAdMobNativeAdUnit(String key, String defaultVal);

    String getAdMobOpenAdUnit(String key, String defaultVal);

    String getAdMobRewardAdUnit(String key, String defaultVal);


    String getNativeAdTemplate(String adPositionName);

    String getNativeAdTemplate(String adPositionName, String defaultVal);

    boolean isLiveUnity(String key, boolean defaultValue);

    boolean isLiveUnity(String key);

    String getUnityBannerAdUnit(String key, String defaultVal);

    String getUnityInterAdUnit(String key, String defaultVal);

    String getUnityRewardAdUnit(String key, String defaultVal);


    boolean isLiveAppodeal(String key, boolean defaultValue);

    boolean isAppodealAvailable();

    boolean isLiveAppodeal(String key);

    String getAppodealBannerAdUnit(String key, String defaultVal);

    String getAppodealNativeAdUnit(String key, String defaultVal);

    String getAppodealInterAdUnit(String key, String defaultVal);

    String getAppodealRewardAdUnit(String key, String defaultVal);


    int getAdCacheTime();

    String getAdPriority(String defaultVal);

    String getAdPriorityNative();

    String getAdPriorityNative(String defaultVal);
}
