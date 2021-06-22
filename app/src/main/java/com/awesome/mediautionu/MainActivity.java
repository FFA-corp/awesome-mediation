package com.awesome.mediautionu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.awesome.mediation.admob.AdMobInterstitialAd;
import com.awesome.mediation.library.AwesomeMediation;
import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.base.MediationNetworkLoader;
import com.awesome.mediation.unity.UnityInterstitialAd;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AwesomeMediation.Config config = new AwesomeMediation.Config(this);
        HashMap<MediationAdNetwork, MediationNetworkLoader> mediationNetworkConfigMap = new HashMap<>();
        mediationNetworkConfigMap.put(MediationAdNetwork.ADMOB, new AdMobInterstitialAd());
        mediationNetworkConfigMap.put(MediationAdNetwork.UNITY, new UnityInterstitialAd());

        config.setMediationNetworkConfigMap(mediationNetworkConfigMap)
                .setPriority(MediationAdNetwork.APPODEAL, MediationAdNetwork.ADMOB, MediationAdNetwork.UNITY);

        AwesomeMediation awesomeMediation = new AwesomeMediation().setConfig(config);
        awesomeMediation.load();
    }
}