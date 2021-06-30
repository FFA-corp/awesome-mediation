package com.awesome.mediation.appodeal;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.IdRes;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.NativeCallbacks;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.appodeal.ads.UserSettings;
import com.awesome.mediation.library.util.MediationAdLogger;

public class AppodealInitializer {

    private static AppodealInitializer instance;
    private String appKey = "a09ff3fbe33f1be47614a928684d998d94f895e1e431052b";

    public AppodealInitializer(String appKey) {
        this.appKey = appKey;
    }

    public AppodealInitializer(String appKey, boolean testMode) {
        this.appKey = appKey;
        if (testMode) {
            Appodeal.setTesting(true);
            Appodeal.setLogLevel(com.appodeal.ads.utils.Log.LogLevel.debug);
        }
    }

    public AppodealInitializer() {
    }

    public static void init(String appKey, boolean testMode) {
        instance = new AppodealInitializer(appKey, testMode);
    }

    public static void init(String appKey) {
        init(appKey, false);
    }

    public static synchronized AppodealInitializer getInstance() {
        instance = new AppodealInitializer();
        return instance;
    }

    /**
     * Enable cache mode for Ad style
     *
     * @param enableCache enable cache or not
     * @param adTypes     ad type of Appodeal
     * @see com.appodeal.ads.Appodeal
     */
    public void enableAutoCache(boolean enableCache, int... adTypes) {
        for (int adType : adTypes) {
            Appodeal.setAutoCache(adType, enableCache);
        }
    }

    public void enableCache(Activity context, int adType) {
        Appodeal.cache(context, adType);
    }

    public void initAdPlacement(Activity activity, int adType, boolean enableConsentForm) {
        Appodeal.initialize(activity, appKey, adType, enableConsentForm);
    }

    public void initAdPlacement(Activity activity, int adType) {
        initAdPlacement(activity, adType, false);
    }

    public void requestRequirePermission(Activity activity, AppodealPermissionCallback callbacks) {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        boolean storagePermissionGranted = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean locationPermissionGranted = activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!storagePermissionGranted || !locationPermissionGranted) {
            Appodeal.requestAndroidMPermissions(activity, callbacks);
        }
    }

    public void initProperties(Activity activity, int userAge, UserSettings.Gender gender, boolean enableConsentForm) {
        Appodeal.setUserAge(userAge);
        Appodeal.setUserGender(gender);
        Appodeal.initialize(activity, appKey, Appodeal.NONE, enableConsentForm);
    }

    public void initProperties(Activity activity, int userAge, UserSettings.Gender gender) {
        initProperties(activity, userAge, gender, false);
    }

    public void initInterstitialAd(Activity activity, boolean enableConsentForm, InterstitialCallbacks callbacks) {
        Appodeal.initialize(activity, appKey, Appodeal.INTERSTITIAL, enableConsentForm);
        Appodeal.setInterstitialCallbacks(callbacks);
    }

    public void initInterstitialAd(Activity activity, InterstitialCallbacks callbacks) {
        initInterstitialAd(activity, false, callbacks);
    }

    public void initRewardAd(Activity activity, boolean enableConsentForm, RewardedVideoCallbacks interstitialCallbacks) {
        Appodeal.initialize(activity, appKey, Appodeal.REWARDED_VIDEO, enableConsentForm);
        Appodeal.setRewardedVideoCallbacks(interstitialCallbacks);
    }

    public void initRewardAd(Activity activity, RewardedVideoCallbacks interstitialCallbacks) {
        Appodeal.initialize(activity, appKey, Appodeal.REWARDED_VIDEO, false);
        Appodeal.setRewardedVideoCallbacks(interstitialCallbacks);
    }

    public void initNativeAd(Activity activity, NativeCallbacks callbacks) {
        initNativeAd(activity, false, callbacks);
    }

    public void initNativeAd(Activity activity, boolean enableConsentForm, NativeCallbacks callbacks) {
        MediationAdLogger.logI(appKey);
        Appodeal.setNativeCallbacks(callbacks);
        Appodeal.initialize(activity, appKey, Appodeal.NATIVE, enableConsentForm);
//        enableAutoCache(true, Appodeal.NATIVE);
//        enableAutoCache(true, Appodeal.NATIVE);
    }

    public void initAdForActivity(Activity activity, boolean enableConsentForm) {
        Appodeal.setUserAge(25);
        Appodeal.setUserGender(UserSettings.Gender.MALE);
        Appodeal.initialize(activity, appKey, Appodeal.NONE, enableConsentForm);
    }

    public void initBannerAd(Activity activity, @IdRes int bannerViewId, BannerCallbacks callbacks) {
        initBannerAd(activity, bannerViewId, false, callbacks);
    }

    public void initBannerAd(Activity activity, @IdRes int bannerViewId,
                             boolean enableConsentForm, BannerCallbacks callbacks) {
        Appodeal.setBannerViewId(bannerViewId);
        Appodeal.initialize(activity, appKey, Appodeal.BANNER, enableConsentForm);
        Appodeal.setBannerCallbacks(callbacks);
    }
}
