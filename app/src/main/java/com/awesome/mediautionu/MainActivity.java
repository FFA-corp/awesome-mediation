package com.awesome.mediautionu;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.awesome.mediation.admob.AdMobInterstitialAd;
import com.awesome.mediation.admob.AdMobNativeAd;
import com.awesome.mediation.admob.AdMobRewardAd;
import com.awesome.mediation.library.AwesomeMediation;
import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.MediationAdType;
import com.awesome.mediation.library.MediationNativeAdView;
import com.awesome.mediation.library.base.MediationAdCallback;
import com.awesome.mediation.library.base.MediationInterstitialAd;
import com.awesome.mediation.library.base.MediationNativeAd;
import com.awesome.mediation.library.base.MediationNetworkLoader;
import com.awesome.mediation.library.base.RewardAdRewardListener;
import com.awesome.mediation.library.config.MediationAdConfig;
import com.awesome.mediation.library.config.MediationRemoteConfig;
import com.awesome.mediation.unity.UnityInterstitialAd;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private AwesomeMediation awesomeMediation;
    private MediationNativeAdView nativeAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nativeAdView = findViewById(R.id.native_ad_view);
        MediationRemoteConfig mediationConfig = new MediationAdConfig(this).getConfig();

        this.loadAdInter(mediationConfig);
        this.loadAdNative(mediationConfig);
//        this.loadReward(mediationConfig);

    }

    private void loadReward(MediationRemoteConfig mediationConfig) {
        AwesomeMediation.Config config = new AwesomeMediation.Config(this);
        HashMap<MediationAdNetwork, MediationNetworkLoader> mediationNetworkConfigMap = new HashMap<>();
        AdMobRewardAd adMobRewardAd = new AdMobRewardAd();
        adMobRewardAd.setAdUnitId(mediationConfig.getAdMobRewardAdUnit("rw_test", "ca-app-pub-3940256099942544/5224354917"));
        adMobRewardAd.setAdPositionName("rw_test");
        mediationNetworkConfigMap.put(MediationAdNetwork.ADMOB, adMobRewardAd);
        config.setMediationNetworkConfigMap(mediationNetworkConfigMap)
                .setPriority(MediationAdNetwork.ADMOB, MediationAdNetwork.APPODEAL, MediationAdNetwork.UNITY);

        AwesomeMediation awesomeMediation = new AwesomeMediation().setConfig(config);
        awesomeMediation.setMediationAdCallback(new MediationAdCallback<AdMobRewardAd>() {
            @Override
            public void onAdLoaded(MediationAdNetwork mediationAdNetwork, MediationAdType adType, AdMobRewardAd mediationNetworkLoader) {
                super.onAdLoaded(mediationAdNetwork, adType, mediationNetworkLoader);
                adMobRewardAd.showAd(MainActivity.this, () -> Log.i("superman", "onUserEarnedReward: "));
            }
        });
        awesomeMediation.load();
    }

    private void loadAdNative(MediationRemoteConfig mediationConfig) {
        AwesomeMediation.Config config = new AwesomeMediation.Config(this);
        HashMap<MediationAdNetwork, MediationNetworkLoader> mediationNetworkConfigMap = new HashMap<>();
        AdMobNativeAd adMobNativeAd = new AdMobNativeAd();
        adMobNativeAd.setAdUnitId(mediationConfig.getAdMobNativeAdUnit("nt_test", "ca-app-pub-3940256099942544/1044960115"));
        adMobNativeAd.setAdPositionName("nt_test");
        mediationNetworkConfigMap.put(MediationAdNetwork.ADMOB, adMobNativeAd);
        config.setMediationNetworkConfigMap(mediationNetworkConfigMap)
                .setPriority(MediationAdNetwork.ADMOB, MediationAdNetwork.APPODEAL, MediationAdNetwork.UNITY);

        AwesomeMediation awesomeMediation = new AwesomeMediation().setConfig(config);
        awesomeMediation.setMediationAdCallback(new MediationAdCallback<MediationNativeAd>() {
            @Override
            public void onAdLoaded(MediationAdNetwork mediationAdNetwork, MediationAdType adType, MediationNativeAd mediationNetworkLoader) {
                super.onAdLoaded(mediationAdNetwork, adType, mediationNetworkLoader);
                mediationNetworkLoader.showAd(nativeAdView);
            }
        });
        awesomeMediation.load();
    }

    private void loadAdInter(MediationRemoteConfig mediationConfig) {
        AwesomeMediation.Config config = new AwesomeMediation.Config(this);
        HashMap<MediationAdNetwork, MediationNetworkLoader> mediationNetworkConfigMap = new HashMap<>();
        UnityInterstitialAd unityInterstitialAd = new UnityInterstitialAd();
        unityInterstitialAd.setAdUnitId(mediationConfig.getUnityInterAdUnit("it_test", "Interstitial_Android"));
        unityInterstitialAd.setAdPositionName("it_test");
        mediationNetworkConfigMap.put(MediationAdNetwork.UNITY, unityInterstitialAd);

        AdMobInterstitialAd adMobInterstitialAd = new AdMobInterstitialAd();
        adMobInterstitialAd.setAdUnitId(mediationConfig.getAdMobInterAdUnit("it_test", "ca-app-pub-3940256099942544/1033173712"));
        adMobInterstitialAd.setAdPositionName("it_test");
        mediationNetworkConfigMap.put(MediationAdNetwork.ADMOB, adMobInterstitialAd);

        config.setMediationNetworkConfigMap(mediationNetworkConfigMap)
                .setPriority(MediationAdNetwork.UNITY, MediationAdNetwork.ADMOB, MediationAdNetwork.APPODEAL);

        this.awesomeMediation = new AwesomeMediation().setConfig(config);
        this.awesomeMediation.setMediationAdCallback(new MediationAdCallback<MediationInterstitialAd<Activity>>() {
            @Override
            public void onAdLoaded(MediationAdNetwork mediationAdNetwork, MediationAdType adType, MediationInterstitialAd<Activity> mediationNetworkLoader) {
                super.onAdLoaded(mediationAdNetwork, adType, mediationNetworkLoader);
                mediationNetworkLoader.showAd(MainActivity.this);
            }
        });
        this.awesomeMediation.load();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (this.awesomeMediation != null) {
            this.awesomeMediation.destroy();
        }
    }
}