package com.awesome.mediation.library.config;

import android.content.Context;
import android.text.TextUtils;

import com.awesome.mediation.library.util.MediationAdLogger;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MediationPrefs extends MediationBasePrefData implements MediationConfigConstant {
    private static WeakReference<MediationPrefs> instance;

    private MediationPrefs(Context context) {
        super(context, "mediation-pref");
    }

    public static MediationPrefs instance(Context context) {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new MediationPrefs(context));
        }
        return instance.get();
    }

    public List<String> getPriorityList(String key, String defaultVal) {
        String useSubStyles = getString(key, defaultVal);
        return getPrioritiesByString(useSubStyles);
    }

    public List<String> getPrioritiesByString(String useSubStyles) {
        MediationAdLogger.logD(useSubStyles);
        if (TextUtils.isEmpty(useSubStyles)) {
            ArrayList<String> subStyleDefault = new ArrayList<>();
            try {
                subStyleDefault = MediationAdManager.getInstance(context).getAdNetworkDefaultStringList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return subStyleDefault;
        }
        useSubStyles = useSubStyles.trim();
        if (useSubStyles.contains(",")) {
            List<String> results = new ArrayList<>();
            String[] split = useSubStyles.split(",");
            for (String style : split) {
                results.add(style.trim());
            }
            return results;
        }
        return Collections.singletonList(useSubStyles);
    }

    public List<String> getPriorityList(String defaultVal) {
        return getPriorityList(AD_PRIORITY, defaultVal);
    }

    public List<String> getNativePriorityList(String defaultVal) {
        return getPriorityList(AD_PRIORITY_NATIVE, defaultVal);
    }

    public void saveLastInterAdRequestTime() {
        putLong(LAST_REQUEST_IT_AD_TIME, System.currentTimeMillis());
    }

    public long getLastInterAdRequestTime() {
        return getLong(LAST_REQUEST_IT_AD_TIME, 0);
    }

    public long getTimeItDelay() {
        return getLong(TIME_IT_DELAY, MediationAdManager.getInstance(context).getInterstitialAdTimeDelay());
    }

    public boolean canRequestInterAd() {
        long delayTime = getTimeItDelay();
        if (delayTime <= 0) {
            return true;
        }

        long lastInterAdRequestTime = getLastInterAdRequestTime();
        if (lastInterAdRequestTime <= 0) {
            return true;
        }

        long timeUsed = System.currentTimeMillis() - lastInterAdRequestTime;
        return timeUsed > delayTime * 1000;
    }

    public void saveMediaViewHeight(int height) {
        putInt(MEDIA_VIEW_HEIGHT, height);
    }

    public int getMediaViewHeight() {
        return getInt(MEDIA_VIEW_HEIGHT, 0);
    }
}
