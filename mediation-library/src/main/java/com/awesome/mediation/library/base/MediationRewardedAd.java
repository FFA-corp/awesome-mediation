package com.awesome.mediation.library.base;

import android.content.Context;

import com.awesome.mediation.library.MediationAdType;

public abstract class MediationRewardedAd extends MediationNetworkLoader {

    protected abstract void showAd(Context context, RewardAdRewardListener rewardAdRewardListener);

    @Override
    protected MediationAdType getMediationAdType() {
        return MediationAdType.REWARD;
    }
}
