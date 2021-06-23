package com.awesome.mediautionu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.awesome.mediation.admob.AdMobInterstitialAd;
import com.awesome.mediation.library.AwesomeMediation;
import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.base.MediationAdCallback;
import com.awesome.mediation.library.base.MediationInterstitialAd;
import com.awesome.mediation.library.base.MediationNetworkLoader;
import com.awesome.mediation.library.config.MediationAdConfig;
import com.awesome.mediation.library.config.MediationRemoteConfig;
import com.awesome.mediation.unity.UnityInterstitialAd;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private AwesomeMediation awesomeMediation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.loadAd();
    }

    private void loadAd() {
        MediationRemoteConfig mediationConfig = new MediationAdConfig(this).getConfig();

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
                adMobInterstitialAd.showAd(MainActivity.this);
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