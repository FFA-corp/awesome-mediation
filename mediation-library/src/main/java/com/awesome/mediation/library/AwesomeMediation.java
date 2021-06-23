package com.awesome.mediation.library;

import android.content.Context;

import com.awesome.mediation.library.base.MediationAdCallback;
import com.awesome.mediation.library.base.MediationNetworkLoader;
import com.awesome.mediation.library.config.MediationPrefs;
import com.awesome.mediation.library.util.MediationAdLogger;

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
        mediationNetworkLoader.setAdLoaderCallback(new MediationAdCallback() {

            @Override
            public void onAdClicked() {
                MediationAdLogger.logI("onAdClicked");
                if (callback != null) {
                    callback.onAdClicked();
                }
            }

            @Override
            public void onAdImpression() {
                MediationAdLogger.logI("onAdImpression");
                if (callback != null) {
                    callback.onAdImpression();
                }
            }

            @Override
            public void onAdClosed() {
                MediationAdLogger.logI("onAdClosed");
                if (callback != null) {
                    callback.onAdClosed();
                }
            }

            @Override
            public void onAdLoaded(MediationNetworkLoader mediationNetworkLoader) {
                MediationAdLogger.logI("onAdLoaded");
                if (destroyed) {
                    return;
                }

                if (callback != null) {
                    callback.onAdLoaded(mediationNetworkLoader);
                }
                mediationNetworkLoaderQueues.removeFirst();

            }

            @Override
            public void onAdError(String errorMessage) {
                MediationAdLogger.logI("onAdError");
                if (destroyed) {
                    return;
                }
                mediationNetworkLoaderQueues.removeFirst();
                if (mediationNetworkLoaderQueues.isEmpty()) {
                    if (callback != null) {
                        callback.onAdError(errorMessage);
                    }
                    return;
                }
                loadNextMediationAd();
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

    private final LinkedList<MediationNetworkLoader> mediationNetworkLoaderQueues = new LinkedList<>();

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
            this.context = context.getApplicationContext();
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
