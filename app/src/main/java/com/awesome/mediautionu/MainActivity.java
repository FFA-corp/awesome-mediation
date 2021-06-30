package com.awesome.mediautionu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.awesome.mediation.admob.AdMobBannerAd;
import com.awesome.mediation.admob.AdMobInterstitialAd;
import com.awesome.mediation.admob.AdMobNativeAd;
import com.awesome.mediation.admob.AdMobRewardAd;
import com.awesome.mediation.appodeal.AppodealBannerAd;
import com.awesome.mediation.appodeal.AppodealInitializer;
import com.awesome.mediation.appodeal.AppodealInterstitialAd;
import com.awesome.mediation.appodeal.AppodealNativeAd;
import com.awesome.mediation.appodeal.AppodealRewardAd;
import com.awesome.mediation.library.AwesomeMediation;
import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.MediationAdType;
import com.awesome.mediation.library.MediationNativeAdView;
import com.awesome.mediation.library.base.MediationAdCallback;
import com.awesome.mediation.library.base.MediationBannerAd;
import com.awesome.mediation.library.base.MediationInterstitialAd;
import com.awesome.mediation.library.base.MediationNativeAd;
import com.awesome.mediation.library.base.MediationNetworkLoader;
import com.awesome.mediation.library.base.MediationRewardedAd;
import com.awesome.mediation.library.base.RewardAdRewardListener;
import com.awesome.mediation.library.config.MediationAdConfig;
import com.awesome.mediation.library.config.MediationRemoteConfig;
import com.awesome.mediation.unity.UnityBannerAd;
import com.awesome.mediation.unity.UnityInterstitialAd;
import com.awesome.mediation.unity.UnityRewardedAd;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private AwesomeMediation awesomeMediation;
    private MediationNativeAdView nativeAdView;
    private MediationBannerAd mediationBannerAd;
    private MediationNativeAd nativeAd;
    private AwesomeMediation nativeMediation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppodealInitializer.getInstance().initAdForActivity(this, false);

        nativeAdView = findViewById(R.id.native_ad_view);
        MediationRemoteConfig mediationConfig = new MediationAdConfig(this).getConfig();

//        this.loadAdInter(mediationConfig);
        this.loadAdNative(mediationConfig);
//        this.loadBanner(mediationConfig);
//        this.loadReward(mediationConfig);
    }

    private void loadBanner(MediationRemoteConfig mediationConfig) {
        AwesomeMediation.Config config = new AwesomeMediation.Config(this);
        HashMap<MediationAdNetwork, MediationNetworkLoader> mediationNetworkConfigMap = new HashMap<>();

        UnityBannerAd unityBannerAd = new UnityBannerAd();
        unityBannerAd.setAdUnitId(mediationConfig.getUnityBannerAdUnit("bn_test", "Banner_Android"));
        unityBannerAd.setAdPositionName("bn_test");
        mediationNetworkConfigMap.put(MediationAdNetwork.UNITY, unityBannerAd);

        AdMobBannerAd adMobBannerAd = new AdMobBannerAd();
        adMobBannerAd.setAdUnitId(mediationConfig.getUnityBannerAdUnit("bn_test", "ca-app-pub-3940256099942544/6300978111"));
        adMobBannerAd.setAdPositionName("bn_test");
        mediationNetworkConfigMap.put(MediationAdNetwork.ADMOB, adMobBannerAd);

        AppodealBannerAd appodealBannerAd = new AppodealBannerAd();
        appodealBannerAd.setAdPositionName("bn_test");
        mediationNetworkConfigMap.put(MediationAdNetwork.APPODEAL, appodealBannerAd);

        config.setMediationNetworkConfigMap(mediationNetworkConfigMap)
                .setPriority(MediationAdNetwork.APPODEAL, MediationAdNetwork.ADMOB, MediationAdNetwork.UNITY);

        ViewGroup viewBanner = findViewById(R.id.view_banner);
        AwesomeMediation awesomeMediation = new AwesomeMediation().setConfig(config);
        awesomeMediation.setMediationAdCallback(new MediationAdCallback<MediationBannerAd>() {
            @Override
            public void onAdLoaded(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType, MediationBannerAd mediationBannerAd) {
                super.onAdLoaded(positionName, mediationAdNetwork, adType, mediationBannerAd);
                MainActivity.this.mediationBannerAd = mediationBannerAd;
                mediationBannerAd.showView(viewBanner);
            }
        });
        awesomeMediation.load();
    }

    private void loadReward(MediationRemoteConfig mediationConfig) {
        AwesomeMediation.Config config = new AwesomeMediation.Config(this);
        HashMap<MediationAdNetwork, MediationNetworkLoader> mediationNetworkConfigMap = new HashMap<>();
        AdMobRewardAd adMobRewardAd = new AdMobRewardAd();
        adMobRewardAd.setAdUnitId(mediationConfig.getAdMobRewardAdUnit("rw_test", "ca-app-pub-3940256099942544/5224354917"));
        adMobRewardAd.setAdPositionName("rw_test");
        mediationNetworkConfigMap.put(MediationAdNetwork.ADMOB, adMobRewardAd);

        UnityRewardedAd unityRewardedAd = new UnityRewardedAd();
        unityRewardedAd.setAdUnitId(mediationConfig.getUnityRewardAdUnit("rw_test", "Rewarded_Android"));
        unityRewardedAd.setAdPositionName("rw_test");
        mediationNetworkConfigMap.put(MediationAdNetwork.UNITY, unityRewardedAd);

        AppodealRewardAd appodealRewardAd = new AppodealRewardAd();
        appodealRewardAd.setAdPositionName("rw_test");
        mediationNetworkConfigMap.put(MediationAdNetwork.APPODEAL, appodealRewardAd);

        config.setMediationNetworkConfigMap(mediationNetworkConfigMap)
                .setPriority(MediationAdNetwork.APPODEAL, MediationAdNetwork.UNITY, MediationAdNetwork.ADMOB);

        AwesomeMediation awesomeMediation = new AwesomeMediation().setConfig(config);
        awesomeMediation.setMediationAdCallback(new MediationAdCallback<MediationRewardedAd>() {
            @Override
            public void onAdLoaded(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType, MediationRewardedAd mediationNetworkLoader) {
                super.onAdLoaded(positionName, mediationAdNetwork, adType, mediationNetworkLoader);
                mediationNetworkLoader.showAd(MainActivity.this, new RewardAdRewardListener() {
                    @Override
                    public void onUserEarnedReward() {
                        Toast.makeText(MainActivity.this, "onUserEarnedReward", Toast.LENGTH_SHORT).show();
                    }
                });
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

        AppodealNativeAd appodealNative = new AppodealNativeAd();
        appodealNative.setAdPositionName("nt_test");
        mediationNetworkConfigMap.put(MediationAdNetwork.APPODEAL, appodealNative);

        config.setMediationNetworkConfigMap(mediationNetworkConfigMap)
                .setPriority(MediationAdNetwork.APPODEAL, MediationAdNetwork.ADMOB, MediationAdNetwork.UNITY);

        nativeMediation = new AwesomeMediation().setConfig(config);
        nativeMediation.setMediationAdCallback(new MediationAdCallback<MediationNativeAd>() {
            @Override
            public void onAdLoaded(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType, MediationNativeAd nativeAd) {
                super.onAdLoaded(positionName, mediationAdNetwork, adType, nativeAd);
                nativeAd.showAd(nativeAdView);
                MainActivity.this.nativeAd = nativeAd;
            }

            @Override
            public void onAllAdError(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
                super.onAllAdError(positionName, mediationAdNetwork, adType);
                nativeAdView.setVisibility(View.GONE);
            }
        });
        nativeMediation.load();
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

        AppodealInterstitialAd appodealInterstitialAd = new AppodealInterstitialAd();
        appodealInterstitialAd.setAdPositionName("it_test");
        mediationNetworkConfigMap.put(MediationAdNetwork.APPODEAL, appodealInterstitialAd);

        config.setMediationNetworkConfigMap(mediationNetworkConfigMap)
                .setPriority(MediationAdNetwork.APPODEAL, MediationAdNetwork.UNITY, MediationAdNetwork.ADMOB);

        this.awesomeMediation = new AwesomeMediation().setConfig(config);
        this.awesomeMediation.setMediationAdCallback(new MediationAdCallback<MediationInterstitialAd<Activity>>() {
            @Override
            public void onAdLoaded(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType, MediationInterstitialAd<Activity> mediationNetworkLoader) {
                super.onAdLoaded(positionName, mediationAdNetwork, adType, mediationNetworkLoader);
                mediationNetworkLoader.showAd(MainActivity.this);
            }

            @Override
            public void onAdError(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType, String errorMessage) {
                super.onAdError(positionName, mediationAdNetwork, adType, errorMessage);
                Toast.makeText(MainActivity.this, mediationAdNetwork.getAdName() + " " + errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAllAdError(String positionName, MediationAdNetwork mediationAdNetwork, MediationAdType adType) {
                super.onAllAdError(positionName, mediationAdNetwork, adType);
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

        if (this.mediationBannerAd != null) {
            this.mediationBannerAd.destroy();
        }
        if (nativeMediation != null) {
            nativeMediation.destroy();
        }

        if (nativeAd != null) {
            nativeAd.destroy();
        }
    }
}