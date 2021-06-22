package com.awesome.mediation.library.config;

import android.content.Context;

import com.awesome.mediation.library.AwesomeMediation;
import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.MediationAppDelegate;
import com.awesome.mediation.library.util.MediationFirebaseConfigFetcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MediationAdManager {
    private static MediationAdManager instance;
    private boolean debugMode;
    private boolean enableEventTrack = false;
    private MediationAppDelegate appDelegate;
    private List<MediationAdNetwork> adNetworkDefaults;

    MediationAdManager(Context context) {
        initConfigs(context);
    }

    private void initConfigs(Context context) {
        this.reloadRemoteConfigs();
        MediationPrefs.instance(context);
    }

    public void reloadRemoteConfigs() {
        MediationFirebaseConfigFetcher.instance().fetch();
    }

    public MediationAdManager(Context context, boolean debugMode) {
        this.debugMode = debugMode;
        initConfigs(context);
    }

    public static void init(Context context) {
        instance = new MediationAdManager(context);
    }

    public static void init(Context context, boolean debugMode) {
        instance = new MediationAdManager(context, debugMode);
    }

    public static MediationAdManager getInstance(Context context) {
        if (instance == null) {
            instance = new MediationAdManager(context);
        }
        return instance;
    }

    public MediationAdManager setEnableEventTrack(boolean enableEventTrack) {
        this.enableEventTrack = enableEventTrack;
        return this;
    }

    public boolean isEnableEventTrack() {
        return enableEventTrack;
    }

    public void setAppDelegate(MediationAppDelegate appDelegate) {
        this.appDelegate = appDelegate;
    }

    public MediationAppDelegate getAppDelegate() {
        if (appDelegate == null) {
            return new MediationAppDelegate() {
                @Override
                public boolean isPolicyAccepted() {
                    return false;
                }

                @Override
                public boolean isAppPurchased() {
                    return false;
                }

                @Override
                public boolean isLocalAppPurchasedState() {
                    return false;
                }
            };
        }
        return appDelegate;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public List<MediationAdNetwork> getAdNetworkDefaults() {
        if (this.adNetworkDefaults == null) {
            return Collections.emptyList();
        }
        return this.adNetworkDefaults;
    }

    public MediationAdManager setAdNetworkDefault(MediationAdNetwork... adNetworkDefaults) {
        this.adNetworkDefaults = Arrays.asList(adNetworkDefaults);
        return null;
    }

    public ArrayList<String> getAdNetworkDefaultStringList() {
        List<MediationAdNetwork> adNetworkDefault = getAdNetworkDefaults();
        ArrayList<String> strings = new ArrayList<>();
        if (adNetworkDefault != null) {
            for (MediationAdNetwork mediationAd : adNetworkDefault) {
                strings.add(mediationAd.getAdName());
            }
        }
        return strings;
    }
}
