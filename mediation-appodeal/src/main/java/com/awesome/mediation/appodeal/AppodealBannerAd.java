package com.awesome.mediation.appodeal;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.BannerView;
import com.awesome.mediation.library.MediationAdNetwork;
import com.awesome.mediation.library.base.MediationBannerAd;

public class AppodealBannerAd extends MediationBannerAd {

    @Override
    public void destroy() {
        Appodeal.destroy(Appodeal.BANNER);
    }

    @Override
    public boolean load(Context context) {
        if (!super.load(context)) {
            return false;
        }
        AppodealInitializer.getInstance().initBannerAd(((Activity) context), R.id.appodeal_view_banner_ad, new BannerCallbacks() {
            @Override
            public void onBannerLoaded(int i, boolean b) {
                onAdLoaded();
            }

            @Override
            public void onBannerFailedToLoad() {
                onAdError("Ad load fail");
            }

            @Override
            public void onBannerShown() {
                onAdImpression();
            }

            @Override
            public void onBannerShowFailed() {
                onAdError("Ad show fail");
            }

            @Override
            public void onBannerClicked() {
                onAdClicked();
            }

            @Override
            public void onBannerExpired() {
                onAdError("Ad is expired");
            }
        });
        return true;
    }

    @Override
    public void showView(ViewGroup bannerRootView) {
        super.showView(bannerRootView);
        if (canShowAd(context)) {
            boolean show = Appodeal.show(((Activity) context), Appodeal.BANNER);
            Log.i("superman", "showView: " + getMediationNetwork() + " " + show);
        }
    }

    @Override
    protected View getBannerAdView() {
        BannerView bannerView = new BannerView(context, null);
        bannerView.setId(R.id.appodeal_view_banner_ad);
        return bannerView;
    }

    @Override
    protected MediationAdNetwork getMediationNetwork() {
        return MediationAdNetwork.APPODEAL;
    }

    @Override
    protected void onAdLoaded() {
        super.onAdLoaded();
        if (getMediationAdCallback() != null) {
            getMediationAdCallback().onAdLoaded(getMediationNetwork(), getMediationAdType(), this);
        }
    }

    @Override
    public boolean isAdLoaded() {
        return Appodeal.isLoaded(Appodeal.BANNER);
    }
}
