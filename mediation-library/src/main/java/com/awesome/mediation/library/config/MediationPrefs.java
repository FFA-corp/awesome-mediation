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

}
