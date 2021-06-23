package com.awesome.mediation.admob

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.awesome.mediation.library.MediationInterstitialAdCache
import com.awesome.mediation.library.config.MediationAdManager
import com.awesome.mediation.library.util.MediationAdLogger
import com.awesome.mediation.library.util.MediationDeviceUtil
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import java.util.*

class AppOpenManager(
    private var myApplication: Application,
    private var adUnit: String,
    private var enable: Boolean
) :
    LifecycleObserver, Application.ActivityLifecycleCallbacks {
    companion object {
        private const val LOG_TAG = "AppOpenManager"
    }

    private var appOpenAd: AppOpenAd? = null
    private lateinit var loadCallback: AppOpenAdLoadCallback
    private var currentActivity: Activity? = null
    private var isShowingAd: Boolean = false
    private var loadTime: Long = 0
    private var customCallback: FullScreenContentCallback? = null
    private var classForLockDisplayAd =
        mutableSetOf(AdActivity::class.java.name, RewardedAd::class.java.name)

    init {
        try {
            MediationAdLogger.logD("Init Open Ad with enable = $enable")
            if (enable) {
                active()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            MediationAdLogger.logW("Init Open Ad error ${e.message}")
        }
    }

    @OnLifecycleEvent(ON_START)
    fun onStart() {
        showAdIfAvailable()
        MediationAdLogger.logD(LOG_TAG, "onStart")
    }

    fun isShowingAd(): Boolean {
        return isShowingAd
    }

    fun addScreenClassNameForLockAd(screenClassName: String) {
        classForLockDisplayAd.add(screenClassName)
    }

    fun setCallback(listener: FullScreenContentCallback?) {
        this.customCallback = listener
    }

    fun canShowAd(): Boolean {
        if (currentActivity == null || currentActivity!!.isFinishing) {
            MediationAdLogger.logD(LOG_TAG, "canShowAd: 1")
            return false
        }

        if (!MediationDeviceUtil.isConnected(currentActivity)) {
            MediationAdLogger.logD(LOG_TAG, "No internet")
            return false
        }

        if (MediationAdManager.getInstance(currentActivity).appDelegate.isAppPurchased) {
            return false;
        }

        val currentActivityClassName = currentActivity!!::class.java.name
        MediationAdLogger.logD(LOG_TAG, currentActivityClassName)
        val isAdActivityShown = classForLockDisplayAd.contains(currentActivityClassName)
                || MediationInterstitialAdCache.instance().showing.value == true

        MediationAdLogger.logD(
            LOG_TAG,
            "isAdActivityShown $isAdActivityShown $isShowingAd ${isAdAvailable()}"
        )
        return !isShowingAd && isAdAvailable() && !isAdActivityShown
    }

    fun showAdIfAvailable() {
        if (!canShowAd()) {
            MediationAdLogger.logD(LOG_TAG, "Can not show ad.")
            fetchAd()
            return;
        }
        MediationAdLogger.logD(LOG_TAG, "Will show ad.")
        val fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                customCallback?.onAdDismissedFullScreenContent()
                this@AppOpenManager.appOpenAd = null
                isShowingAd = false
                fetchAd()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                customCallback?.onAdFailedToShowFullScreenContent(adError)
            }

            override fun onAdImpression() {
                super.onAdImpression()
                customCallback?.onAdImpression()
            }

            override fun onAdShowedFullScreenContent() {
                customCallback?.onAdShowedFullScreenContent()
                isShowingAd = true
            }
        }
        appOpenAd?.fullScreenContentCallback = fullScreenContentCallback
        appOpenAd?.show(currentActivity!!)
    }

    fun fetchAd() {
        val appDelegate = MediationAdManager.getInstance(currentActivity).appDelegate
        if (isAdAvailable() || !appDelegate.isPolicyAccepted
            || appDelegate.isAppPurchased
            || !MediationDeviceUtil.isConnected(currentActivity)
        ) {
            MediationAdLogger.logD(LOG_TAG, "Abort fetch ad")
            return
        }
        loadCallback = object : AppOpenAdLoadCallback() {
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                MediationAdLogger.logD(LOG_TAG, "onAppOpenAdFailedToLoad: " + loadAdError.message)
            }

            override fun onAdLoaded(ad: AppOpenAd) {
                super.onAdLoaded(ad)
                MediationAdLogger.logD(LOG_TAG, "onAdLoaded: ")

                this@AppOpenManager.appOpenAd = ad
                this@AppOpenManager.loadTime = (Date()).time
            }

        }

        val request: AdRequest = getAdRequest()
        AppOpenAd.load(
            myApplication, adUnit, request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback
        )
    }


    private fun getAdRequest(): AdRequest {
        return AdMobAdUtil.getAdRequestBuilderWithTestDevice(myApplication).build()
    }

    fun isAdAvailable(): Boolean {
        MediationAdLogger.logD(LOG_TAG, "appOpenAd = " + (appOpenAd == null))
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
    }

    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference = (Date()).time - this.loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return (dateDifference < (numMilliSecondsPerHour * numHours))
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    fun active() {
        try {
            MediationAdLogger.logD(LOG_TAG, "")
            deactive()
            this.myApplication.registerActivityLifecycleCallbacks(this)
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        } catch (e: Exception) {
        }
    }

    fun deactive() {
        try {
            MediationAdLogger.logD(LOG_TAG, "")
            this.myApplication.unregisterActivityLifecycleCallbacks(this)
            ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        } catch (e: Exception) {
        }
    }


}