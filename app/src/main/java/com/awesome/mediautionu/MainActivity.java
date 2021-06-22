package com.awesome.mediautionu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.awesome.mediation.admob.AdMobInterstitialAd;
import com.awesome.mediation.library.AwesomeMediation;
import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.base.MediationAdCallback;
import com.awesome.mediation.library.base.MediationInterstitialAd;
import com.awesome.mediation.library.base.MediationNetworkLoader;
import com.awesome.mediation.unity.UnityInterstitialAd;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private AwesomeMediation awesomeMediation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AwesomeMediation.Config config = new AwesomeMediation.Config(this);
        HashMap<MediationAdNetwork, MediationNetworkLoader> mediationNetworkConfigMap = new HashMap<>();
        mediationNetworkConfigMap.put(MediationAdNetwork.UNITY, new UnityInterstitialAd());
        mediationNetworkConfigMap.put(MediationAdNetwork.ADMOB, new AdMobInterstitialAd());

        config.setMediationNetworkConfigMap(mediationNetworkConfigMap)
                .setPriority(MediationAdNetwork.APPODEAL, MediationAdNetwork.UNITY, MediationAdNetwork.ADMOB);

        this.awesomeMediation = new AwesomeMediation().setConfig(config);
        this.awesomeMediation.setMediationAdCallback(new MediationAdCallback<MediationInterstitialAd>() {
            @Override
            public void onAdLoaded(MediationInterstitialAd mediationNetworkLoader) {
                super.onAdLoaded(mediationNetworkLoader);
                mediationNetworkLoader.showAd(MainActivity.this);
            }
        });
        this.awesomeMediation.load();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (awesomeMediation != null) {
            awesomeMediation.destroy();
        }
    }
}