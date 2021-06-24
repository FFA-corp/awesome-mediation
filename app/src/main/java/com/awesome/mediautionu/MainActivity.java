package com.awesome.mediautionu;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.awesome.mediation.admob.AdMobInterstitialAd;
import com.awesome.mediation.admob.AdMobNativeAd;
import com.awesome.mediation.admob.AdMobRewardAd;
import com.awesome.mediation.library.AwesomeMediation;
import com.awesome.mediation.library.MediationAdNetwork;
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

//        this.loadAdInter(mediationConfig);
//        this.loadAdNative(mediationConfig);

        this.loadReward(mediationConfig);
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
            public void onAdLoaded(AdMobRewardAd admobRewardAd) {
                super.onAdLoaded(admobRewardAd);
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
            public void onAdLoaded(MediationNativeAd mediationNativeAd) {
                super.onAdLoaded(mediationNativeAd);
                mediationNativeAd.showAd(nativeAdView);
            }
        });
        awesomeMediation.load();
    }

    private void loadAdInter(MediationRemoteConfig mediationConfig) {
        AwesomeMediation.Config config = new AwesomeMediation.Config(this);
        HashMap<MediationAdNetwork, MediationNetworkLoader> mediationNetworkConfigMap = new HashMap<>();
        UnityInterstitialAd value = new UnityInterstitialAd();
        UnityInterstitialAd unityInterstitialAd = new UnityInterstitialAd();
        unityInterstitialAd.setAdUnitId(mediationConfig.getAdMobInterAdUnit("it_test", "ca-app-pub-3940256099942544/1033173712"));
        unityInterstitialAd.setAdPositionName("it_test");
        mediationNetworkConfigMap.put(MediationAdNetwork.UNITY, value);

        AdMobInterstitialAd adMobInterstitialAd = new AdMobInterstitialAd();
        adMobInterstitialAd.setAdUnitId(mediationConfig.getAdMobInterAdUnit("it_test", "ca-app-pub-3940256099942544/1033173712"));
        adMobInterstitialAd.setAdPositionName("it_test");
        mediationNetworkConfigMap.put(MediationAdNetwork.ADMOB, adMobInterstitialAd);

        config.setMediationNetworkConfigMap(mediationNetworkConfigMap)
                .setPriority(MediationAdNetwork.ADMOB, MediationAdNetwork.APPODEAL, MediationAdNetwork.UNITY);

        this.awesomeMediation = new AwesomeMediation().setConfig(config);
        this.awesomeMediation.setMediationAdCallback(new MediationAdCallback<MediationInterstitialAd>() {
            @Override
            public void onAdLoaded(MediationInterstitialAd mediationNetworkLoader) {
                super.onAdLoaded(mediationNetworkLoader);
                mediationNetworkLoader.showAd(getApplicationContext());
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