package com.awesome.mediation.library;

public enum MediationAdNetwork {
    ADMOB("gad"), UNITY("unity"), APPODEAL("appo");

    private final String adName;

    MediationAdNetwork(String adName) {
        this.adName = adName;
    }

    public String getAdName() {
        return adName;
    }

    public static MediationAdNetwork lookup(String adName) {
        for (MediationAdNetwork e : MediationAdNetwork.values()) {
            if (e.adName.equalsIgnoreCase(adName)) {
                return e;
            }
        }
        return null;
    }
}
