package com.awesome.mediation.library.config;

import android.content.Context;
import android.text.TextUtils;

import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.MediationAppDelegate;
import com.awesome.mediation.library.util.MediationDeviceUtil;
import com.awesome.mediation.library.util.MediationFirebaseConfigFetcher;
import com.awesome.mediation.library.util.MediationAdLogger;
import com.awesome.mediation.library.util.MediationGooglePlayInstallerUtils;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MediationAdRemoteConfig implements MediationConfig {

    private static volatile MediationAdRemoteConfig instance;
    private Context context;

    private MediationAdRemoteConfig() {
    }

    public static MediationAdRemoteConfig instance() {
        if (instance == null) {
            synchronized (MediationAdRemoteConfig.class) {
                if (instance == null) {
                    instance = new MediationAdRemoteConfig();
                }
            }
        }
        return instance;
    }

    public MediationAdRemoteConfig(Context context) {
        this.context = context;
    }

    @Override
    public boolean isLiveAdMob(String key, boolean defaultValue) {
        if (!isAdAvailable()) {
            return false;
        }
        return MediationPrefs.instance(context).getBoolean(key, defaultValue);
    }

    @Override
    public boolean isLiveAdMob(String key) {
        return isLiveAdMob(MediationAdNetwork.ADMOB.getAdName() + "_" + key, true);
    }

    @Override
    public void fetch() {
        fetch(null);
    }

    @Override
    public void fetch(MediationFirebaseConfigFetcher.OnFetchListener fetchListener) {
        MediationFirebaseConfigFetcher.instance().fetch(new MediationFirebaseConfigFetcher.OnFetchListener() {
            @Override
            public void onSuccess(FirebaseRemoteConfig firebaseRemoteConfig) {
                MediationAdLogger.logD("Fetch ad configs success");
                saveConfigByPrefix(firebaseRemoteConfig, "it_");
                saveConfigByPrefix(firebaseRemoteConfig, "nt_");
                saveConfigByPrefix(firebaseRemoteConfig, "oa_");
                if (fetchListener != null) {
                    fetchListener.onSuccess(firebaseRemoteConfig);
                }
            }

            @Override
            public void onFail() {
                MediationAdLogger.logI("Fetch config failed");
                if (fetchListener != null) {
                    fetchListener.onFail();
                }
            }
        });
    }

    private void saveConfigByPrefix(FirebaseRemoteConfig firebaseRemoteConfig, String prefix) {
        Set<String> subConfigKeys = firebaseRemoteConfig.getKeysByPrefix(prefix);
        MediationAdLogger.logD(subConfigKeys.toString());
        for (String subConfigKey : subConfigKeys) {
            saveConfigs(firebaseRemoteConfig, subConfigKey);
        }
    }

    private void saveConfigs(FirebaseRemoteConfig firebaseRemoteConfig, String subConfigKey) {
        if (firebaseRemoteConfig == null) {
            return;
        }
        String inputStringFirebase = firebaseRemoteConfig.getString(subConfigKey);
        if (TextUtils.isEmpty(inputStringFirebase)) {
            return;
        }

        try {
            Pattern queryLangPattern = Pattern.compile("true|false", Pattern.CASE_INSENSITIVE);
            Matcher matcher = queryLangPattern.matcher(inputStringFirebase);
            if (matcher.matches()) {
                MediationPrefs.instance(context).putBoolean(subConfigKey, Boolean.valueOf(inputStringFirebase));
                return;
            }
            throw new Exception("Invalid type");
        } catch (Exception exception) {
            try {
                MediationPrefs.instance(context).putInt(subConfigKey, Integer.parseInt(inputStringFirebase));
            } catch (NumberFormatException e) {
                MediationPrefs.instance(context).putString(subConfigKey, inputStringFirebase);
            }
        }
    }

    @Override
    public boolean isLivePlacement(String key) {
        return isLivePlacement(key, true);
    }

    @Override
    public boolean isLivePlacement(String key, boolean defaultValue) {
        if (!isAdAvailable()) {
            return false;
        }

        return MediationPrefs.instance(context).getBoolean(key, defaultValue);
    }

    private boolean isAdAvailable() {
        if (!MediationDeviceUtil.isConnected(context)) {
            return false;
        }
        MediationAppDelegate appDelegate = MediationAdManager.getInstance(context).getAppDelegate();
        if (appDelegate.isLocalAppPurchasedState()) {
            return false;
        }

        return !appDelegate.isAppPurchased();
    }

    @Override
    public int getAdCacheTime() {
        return MediationPrefs.instance(context).getInt(MediationConfigConstant.AD_CACHE_TIME, 0);
    }

    @Override
    public String getAdPriority() {
        return MediationPrefs.instance(context).getString(MediationConfigConstant.AD_PRIORITY, "");
    }

    @Override
    public String getAdPriorityNative() {
        return MediationPrefs.instance(context).getString(MediationConfigConstant.AD_PRIORITY_NATIVE, "");
    }

    @Override
    public String getAdMobInterAdUnit(String key, String defaultVal) {
        if (isDebugMode()) {
            return "ca-app-pub-3940256099942544/1033173712";
        }
        return MediationPrefs.instance(context).getString(MediationAdNetwork.ADMOB.getAdName() + "_" + key, defaultVal);
    }

    private boolean isDebugMode() {
        return MediationAdManager.getInstance(context).isDebugMode();
    }

    @Override
    public String getAdMobNativeAdUnit(String key, String defaultVal) {
        if (isDebugMode()) {
            return "ca-app-pub-3940256099942544/2247696110";
        }
        return MediationPrefs.instance(context).getString(MediationAdNetwork.ADMOB.getAdName() + "_" + key, defaultVal);
    }

    @Override
    public String getAdMobOpenAdUnit(String key, String defaultVal) {
        if (isDebugMode()) {
            return "ca-app-pub-3940256099942544/3419835294";
        }
        return MediationPrefs.instance(context).getString(MediationAdNetwork.ADMOB.getAdName() + "_" + key, defaultVal);
    }

    @Override
    public boolean isLiveUnity(String key, boolean defaultValue) {
        if (!isAdAvailable()) {
            return false;
        }
        return MediationPrefs.instance(context).getBoolean(MediationAdNetwork.UNITY.getAdName() + "_" + key, defaultValue);
    }

    @Override
    public boolean isLiveUnity(String key) {
        return isLiveUnity(key, true);
    }

    @Override
    public String getUnityBannerAdUnit(String key, String defaultVal) {
        return MediationPrefs.instance(context).getString(MediationAdNetwork.UNITY.getAdName() + "_" + key, defaultVal);
    }

    @Override
    public String getUnityInterAdUnit(String key, String defaultVal) {
        return MediationPrefs.instance(context).getString(MediationAdNetwork.UNITY.getAdName() + "_" + key, defaultVal);
    }

    @Override
    public String getUnityRewardAdUnit(String key, String defaultVal) {
        return MediationPrefs.instance(context).getString(MediationAdNetwork.UNITY.getAdName() + "_" + key, defaultVal);
    }

    @Override
    public boolean isLiveAppodeal(String key, boolean defaultValue) {
        if (!isAdAvailable() || !MediationGooglePlayInstallerUtils.isInstalledViaGooglePlay(context)) {
            return false;
        }
        return MediationPrefs.instance(context).getBoolean(MediationAdNetwork.APPODEAL.getAdName() + "_" + key, defaultValue);
    }

    @Override
    public boolean isLiveAppodeal(String key) {
        return isLiveAppodeal(key, true);
    }

    @Override
    public String getAppodealBannerAdUnit(String key, String defaultVal) {
        return MediationPrefs.instance(context).getString(MediationAdNetwork.APPODEAL.getAdName() + "_" + key, defaultVal);
    }

    @Override
    public String getAppodealNativeAdUnit(String key, String defaultVal) {
        return MediationPrefs.instance(context).getString(MediationAdNetwork.APPODEAL.getAdName() + "_" + key, defaultVal);
    }

    @Override
    public String getAppodealInterAdUnit(String key, String defaultVal) {
        return MediationPrefs.instance(context).getString(MediationAdNetwork.APPODEAL.getAdName() + "_" + key, defaultVal);
    }

    @Override
    public String getAppodealRewardAdUnit(String key, String defaultVal) {
        return MediationPrefs.instance(context).getString(MediationAdNetwork.APPODEAL.getAdName() + "_" + key, defaultVal);
    }

}
