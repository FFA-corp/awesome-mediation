package com.awesome.mediation.library;

public enum MediationAdType {
    INTERSTITIAL("it"), NATIVE("nt"), BANNER("bn"), REWARD("rw");

    private final String shortName;

    MediationAdType(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }
}
