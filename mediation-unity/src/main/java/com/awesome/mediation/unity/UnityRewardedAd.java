package com.awesome.mediation.unity;

import android.content.Context;

import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.base.MediationAdCallback;
import com.awesome.mediation.library.base.MediationNetworkLoader;
import com.awesome.mediation.library.base.MediationRewardedAd;
import com.awesome.mediation.library.base.RewardAdRewardListener;

public class UnityRewardedAd extends MediationRewardedAd {

    @Override
    protected void showAd(Context context, RewardAdRewardListener rewardAdRewardListener) {

    }

    @Override
    public boolean load(Context context) {
        return super.load(context);
    }

    @Override
    protected MediationAdNetwork getMediationNetwork() {
        return MediationAdNetwork.UNITY;
    }

    @Override
    protected void onAdLoaded() {
        super.onAdLoaded();
        MediationAdCallback<MediationNetworkLoader> mediationAdCallback = getMediationAdCallback();
        if (mediationAdCallback != null) {
            mediationAdCallback.onAdLoaded(getMediationNetwork(), getMediationAdType(), this);
        }
    }
}
