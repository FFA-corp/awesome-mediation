package com.awesome.mediation.library;

import android.content.Context;

import com.awesome.mediation.library.base.MediationAdCallback;
import com.awesome.mediation.library.base.MediationNetworkLoader;
import com.awesome.mediation.library.config.MediationAdManager;
import com.awesome.mediation.library.config.MediationPrefs;
import com.awesome.mediation.library.util.MediationAdLogger;
import com.awesome.mediation.library.util.MediationDeviceUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AwesomeMediation {

    private Config config;
    private MediationAdCallback<MediationNetworkLoader> callback;
    private boolean destroyed;

    public AwesomeMediation setConfig(Config config) {
        this.config = config;
        return this;
    }

    public void load() {
        if (!MediationDeviceUtil.isConnected(config.context)) {
            MediationAdLogger.logD("Not connect to internet");
            return;
        }
        if (MediationAdManager.getInstance(config.context).getAppDelegate().isAppPurchased()) {
            MediationAdLogger.logD("App is purchased");
            return;
        }
        List<String> priorityList = MediationPrefs.instance(config.context).getPriorityList(config.getPriority());
        Map<MediationAdNetwork, MediationNetworkLoader> mediationNetworkConfigMap = config.getMediationNetworkConfigMap();
        for (String priority : priorityList) {
            MediationAdNetwork mediationAdNetwork = MediationAdNetwork.lookup(priority);
            if (mediationAdNetwork == null) {
                continue;
            }
            MediationNetworkLoader mediationNetworkLoader = mediationNetworkConfigMap.get(mediationAdNetwork);
            if (mediationNetworkLoader == null) {
                continue;
            }
            addAdNetworkRequestToQueue(mediationNetworkLoader);
        }

        if (mediationNetworkLoaderQueues.isEmpty()) {
            MediationAdLogger.logW("QUEUES is empty");
            return;
        }
        executeQueues();
    }

    public AwesomeMediation setMediationAdCallback(MediationAdCallback callback) {
        this.callback = callback;
        return this;
    }

    private void executeQueues() {
        MediationNetworkLoader mediationNetworkLoader = mediationNetworkLoaderQueues.getFirst();
        mediationNetworkLoader.setAdLoaderCallback(new MediationAdCallback<MediationNetworkLoader>() {
            @Override
            public void onAdClicked(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
                super.onAdClicked(positionName, mediationAdNetwork, adType);
                if (callback != null) {
                    callback.onAdClicked(positionName, mediationAdNetwork, adType);
                }
            }

            @Override
            public void onAdClosed(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
                super.onAdClosed(positionName, mediationAdNetwork, adType);
                if (callback != null) {
                    callback.onAdImpression(positionName, mediationAdNetwork, adType);
                }
            }

            @Override
            public void onAdError(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType, String errorMessage) {
                super.onAdError(positionName, mediationAdNetwork, adType, errorMessage);
                if (destroyed) {
                    return;
                }
                if (!mediationNetworkLoaderQueues.isEmpty()) {
                    mediationNetworkLoaderQueues.removeFirst();
                }
                if (mediationNetworkLoaderQueues.isEmpty()) {
                    if (callback != null) {
                        callback.onAdError(positionName, mediationAdNetwork, adType, errorMessage);
                    }
                    return;
                }
                loadNextMediationAd();
            }

            @Override
            public void onAdImpression(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
                super.onAdImpression(positionName, mediationAdNetwork, adType);
                if (callback != null) {
                    callback.onAdImpression(positionName, mediationAdNetwork, adType);
                }
            }

            @Override
            public void onAdLoaded(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType, MediationNetworkLoader mediationNetworkLoader) {
                super.onAdLoaded(positionName, mediationAdNetwork, adType, mediationNetworkLoader);
                if (destroyed) {
                    return;
                }
                if (callback != null) {
                    callback.onAdLoaded(positionName, mediationAdNetwork, adType, mediationNetworkLoader);
                }
                if (!mediationNetworkLoaderQueues.isEmpty()) {
                    mediationNetworkLoaderQueues.removeFirst();
                }
            }
        });
        mediationNetworkLoader.load(config.context);
    }

    private void loadNextMediationAd() {
        MediationAdLogger.logI("loadNextMediationAd");
        executeQueues();
    }

    public void destroy() {
        destroyed = true;
        if (mediationNetworkLoaderQueues.isEmpty()) {
            return;
        }
        mediationNetworkLoaderQueues.clear();
    }

    private LinkedList<MediationNetworkLoader> mediationNetworkLoaderQueues = new LinkedList<>();

    private void addAdNetworkRequestToQueue(MediationNetworkLoader mediationNetworkLoader) {
        if (mediationNetworkLoaderQueues.contains(mediationNetworkLoader)) {
            return;
        }
        mediationNetworkLoaderQueues.add(mediationNetworkLoader);
    }

    public static class Config {
        private String priority;
        private Map<MediationAdNetwork, MediationNetworkLoader> mediationNetworkConfigMap;
        private final Context context;

        public Config(Context context) {
            this.context = context;
        }

        public Context getContext() {
            return context;
        }

        public Config setPriority(String priority) {
            this.priority = priority;
            return this;
        }

        public Config setPriority(MediationAdNetwork... networkPriorities) {
            StringBuilder stringBuilder = new StringBuilder();
            int size = networkPriorities.length;
            for (int i = 0; i < size; i++) {
                MediationAdNetwork mediationAdNetwork = networkPriorities[i];
                stringBuilder.append(mediationAdNetwork.getAdName());
                if (i != size - 1) {
                    stringBuilder.append(", ");
                }
            }
            this.priority = stringBuilder.toString();
            return this;
        }

        public Config setMediationNetworkConfigMap(Map<MediationAdNetwork, MediationNetworkLoader> mediationNetworkConfigMap) {
            this.mediationNetworkConfigMap = mediationNetworkConfigMap;
            return this;
        }

        public Map<MediationAdNetwork, MediationNetworkLoader> getMediationNetworkConfigMap() {
            if (mediationNetworkConfigMap == null) {
                return new HashMap<>();
            }
            return mediationNetworkConfigMap;
        }

        public String getPriority() {
            return priority;
        }

    }
}
