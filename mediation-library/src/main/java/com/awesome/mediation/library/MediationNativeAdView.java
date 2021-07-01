package com.awesome.mediation.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.awesome.mediation.NativeAdTemplate;
import com.awesome.mediation.library.base.MediationNativeAd;
import com.awesome.mediation.library.config.MediationAdConfig;
import com.awesome.mediation.library.config.MediationPrefs;
import com.awesome.mediation.library.config.MediationRemoteConfig;
import com.awesome.mediation.library.util.MediationAdLogger;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MediationNativeAdView extends LinearLayout {

    private static final String TAG = "MEDIATION-NATIVE";
    private static final String ADMOB_NATIVE_AD_VIEW = "com.google.android.gms.ads.nativead.NativeAdView";
    private static final String ADMOB_NATIVE_AD = "com.google.android.gms.ads.nativead.NativeAd";

    private static final String APPODEAL_NATIVE_AD_VIEW = "com.appodeal.ads.NativeAdView";
    private static final String APPODEAL_NATIVE_AD = "com.appodeal.ads.NativeAd";
    private static final String APPODEAL_NATIVE_ICON_VIEW = "com.appodeal.ads.NativeIconView";
    private static final String APPODEAL_NATIVE_MEDIA_VIEW = "com.appodeal.ads.NativeMediaView";

    private TextView tvNativeTitle;
    private TextView tvNativeBody;
    private TextView btNativeCta;
    private ViewGroup viewIcon;
    private ViewGroup layoutAdChoice;
    private ViewGroup layoutContentAd;
    private ViewGroup layoutRootAd;
    private ViewGroup layoutMediaView;
    private NativeAdTemplate adStyle = NativeAdTemplate.DEFAULT;
    private boolean adAttached;
    private View nativeAdContentLayout;
    private ViewGroup layoutAdContent;
    private String remoteName;

    public MediationNativeAdView(Context context) {
        super(context);
        initView(null);
    }

    public MediationNativeAdView(Context context, String adStyle, String remoteName) {
        super(context);
        this.adStyle = NativeAdTemplate.lookup(adStyle);
        this.remoteName = remoteName;
        initView(null);
    }

    public MediationNativeAdView setAdStyle(NativeAdTemplate adStyle) {
        this.adStyle = adStyle;
        return this;
    }

    public MediationNativeAdView setRemoteName(String remoteName) {
        this.remoteName = remoteName;
        return this;
    }

    public MediationNativeAdView setAdStyle(String adStyleName) {
        this.adStyle = NativeAdTemplate.lookup(adStyleName);
        return this;
    }

    public MediationNativeAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public MediationNativeAdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MediationNativeAdView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        MediationRemoteConfig config = MediationAdConfig.newInstance(getContext()).getConfig();

        Context context = getContext();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MediationNativeAdView, 0, 0);
        if (typedArray.hasValue(R.styleable.MediationNativeAdView_adStyle)) {
            this.adStyle = NativeAdTemplate.lookup(typedArray.getInt(R.styleable.MediationNativeAdView_adStyle, 0));
        }

        String nativeAdTemplate = null;
        if (typedArray.hasValue(R.styleable.MediationNativeAdView_remoteName)) {
            remoteName = typedArray.getString(R.styleable.MediationNativeAdView_remoteName);
            nativeAdTemplate = config.getNativeAdTemplate(remoteName, adStyle.getName());
        }

        if (!TextUtils.isEmpty(nativeAdTemplate)) {
            adStyle = NativeAdTemplate.lookup(nativeAdTemplate);
        }
        inflateView(context);
    }

    private void inflateView(Context context) {
        inflate(context, adStyle.getLayoutRes(context), this);

        this.viewIcon = this.findViewById(R.id.native_ad_ad_icon_layout);
        this.layoutMediaView = this.findViewById(R.id.layout_media_view);
        this.tvNativeTitle = this.findViewById(R.id.native_ad_title);
        this.tvNativeBody = this.findViewById(R.id.native_ad_body);
        this.btNativeCta = this.findViewById(R.id.native_cta);
        this.showLoadingState(tvNativeTitle, tvNativeBody, btNativeCta, viewIcon, layoutMediaView);

        this.layoutAdChoice = this.findViewById(R.id.native_adchoice_view);
        this.layoutContentAd = this.findViewById(R.id.layout_content_ad);
        this.layoutRootAd = this.findViewById(R.id.root_ad_view);

        if (hasMediaView()) {
            int mediaViewHeight = MediationPrefs.instance(context).getMediaViewHeight();
            if (mediaViewHeight <= 0) {
                layoutRootAd.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int measuredWidth = layoutRootAd.getMeasuredWidth();
                        int height = (int) (((float) 9 / 16) * measuredWidth);
                        layoutMediaView.getLayoutParams().height = height;
                        MediationPrefs.instance(context).saveMediaViewHeight(height);
                        layoutRootAd.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            } else {
                layoutMediaView.getLayoutParams().height = mediaViewHeight;
            }
            layoutMediaView.setVisibility(VISIBLE);
        }

        this.nativeAdContentLayout = this.findViewById(R.id.native_ad_content_layout);
        this.layoutAdContent = findViewById(this.getResources().getIdentifier("layout_ad_content", "id",
                context.getPackageName()));
        this.setVisibility(VISIBLE);
    }

    private void showLoadingState(View... views) {
        activeView(views, false);
    }

    private void hideLoadingState(View... views) {
        activeView(views, true);
    }

    private void activeView(View[] views, boolean active) {
        for (View view : views) {
            if (view != null) {
                view.setActivated(active);
            }
        }
    }

    public void setAdBackgroundColor(@ColorRes int colorResId) {
        if (this.nativeAdContentLayout != null) {
            this.nativeAdContentLayout.setBackgroundColor(ContextCompat.getColor(getContext(), colorResId));
        }
    }

    public boolean isAdAttached() {
        return adAttached;
    }

    public void show(MediationNativeAd unifiedNativeAd) {
        if (unifiedNativeAd == null) {
            setVisibility(GONE);
            return;
        }
        MediationAdLogger.logD(TAG, unifiedNativeAd.toString());
        this.adAttached = true;
        this.layoutContentAd.setVisibility(VISIBLE);
        this.viewIcon.removeAllViews();
        this.layoutAdChoice.removeAllViews();
        this.layoutRootAd.removeAllViews();

        this.hideLoadingState(tvNativeTitle, tvNativeBody, btNativeCta, viewIcon, layoutMediaView);

        ViewGroup adMediaView = unifiedNativeAd.getAdMediaView();
        if (hasMediaView()) {
            this.layoutMediaView.addView(adMediaView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            this.layoutMediaView.setVisibility(VISIBLE);
        }
        Context context = getContext();
        FrameLayout nativeAdView = new FrameLayout(context);
        Class<?> adContainerClass = unifiedNativeAd.getAdContainerClass();
        String name = adContainerClass.getName();
        if (ADMOB_NATIVE_AD_VIEW.equalsIgnoreCase(name)) {
            setupAdViewForAdMob(unifiedNativeAd, context);
        } else if (APPODEAL_NATIVE_AD_VIEW.equalsIgnoreCase(name)) {
            setupAdViewForAppodeal(unifiedNativeAd, context);
        }

        this.tvNativeTitle.setText(unifiedNativeAd.getAdTitle());
        this.tvNativeBody.setText(unifiedNativeAd.getAdBody());
        this.btNativeCta.setBackgroundResource(adStyle.getCtaBackground());
        this.btNativeCta.setText(unifiedNativeAd.getAdCallToAction());
        this.btNativeCta.setTextColor(ContextCompat.getColor(context, adStyle.getCtaTextColor()));

        ImageView adIconLogoView = new ImageView(context);
        ViewGroup adIconView = unifiedNativeAd.getAdIconView();
        if (adIconView != null) {
            this.viewIcon.addView(adIconView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        } else {
            this.viewIcon.addView(adIconLogoView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            Drawable adIconDrawable = unifiedNativeAd.getAdIconDrawable();
            if (adIconDrawable != null) {
                adIconLogoView.setImageDrawable(adIconDrawable);
            } else {
                String adIconUrl = unifiedNativeAd.getAdIconUrl();
                if (!TextUtils.isEmpty(adIconUrl)) {
                    Picasso.get().load(adIconUrl).into(adIconLogoView);
                }
            }
        }

        ViewGroup adAdChoiceView = unifiedNativeAd.getAdAdChoiceView();
        if (adAdChoiceView != null) {
            layoutAdChoice.addView(adAdChoiceView);
        }
        try {
            ((ViewGroup) layoutContentAd.getParent()).removeView(layoutContentAd);
        } catch (Exception ignored) {
        }
        nativeAdView.addView(layoutContentAd);
        nativeAdView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.layoutRootAd.addView(nativeAdView);

        this.layoutRootAd.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
    }

    private void setupAdViewForAppodeal(MediationNativeAd unifiedNativeAd, Context context) {
        ClassLoader classLoader = context.getClassLoader();
        try {
            Class<?> nativeAdViewClass = classLoader.loadClass(APPODEAL_NATIVE_AD_VIEW);
            Constructor<?> constructor = nativeAdViewClass.getDeclaredConstructor(Context.class);
            ViewGroup nativeAdViewInstance = (ViewGroup) nativeAdViewClass.cast(constructor.newInstance(context));
            Method setLayoutParams = nativeAdViewClass.getMethod("setLayoutParams", ViewGroup.LayoutParams.class);
            setLayoutParams.invoke(nativeAdViewInstance, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            Method setTitleView = nativeAdViewClass.getMethod("setTitleView", View.class);
            setTitleView.invoke(nativeAdViewInstance, tvNativeTitle);

            Method setDescriptionView = nativeAdViewClass.getMethod("setDescriptionView", View.class);
            setDescriptionView.invoke(nativeAdViewInstance, tvNativeBody);

            Class<?> nativeIconViewClass = classLoader.loadClass(APPODEAL_NATIVE_ICON_VIEW);
            Method setNativeIconView = nativeAdViewClass.getMethod("setNativeIconView", nativeIconViewClass);
            setNativeIconView.invoke(nativeAdViewInstance, nativeIconViewClass.cast(unifiedNativeAd.getAdIconView()));

            Method setCallToActionView = nativeAdViewClass.getMethod("setCallToActionView", View.class);
            setCallToActionView.invoke(nativeAdViewInstance, btNativeCta);

            Class<?> nativeAdClass = classLoader.loadClass(APPODEAL_NATIVE_AD);
            Object nativeAdInstance = nativeAdClass.cast(unifiedNativeAd.getAdLoadedInstance());

            if (hasMediaView()) {
                Method containsVideo = nativeAdClass.getMethod("containsVideo");
                boolean containVideoVal = (boolean) containsVideo.invoke(nativeAdInstance);
                if (containVideoVal) {
                    Class<?> nativeMediaViewClass = classLoader.loadClass(APPODEAL_NATIVE_MEDIA_VIEW);
                    Method setNativeMediaView = nativeAdViewClass.getMethod("setNativeMediaView", nativeMediaViewClass);
                    setNativeMediaView.invoke(nativeAdViewInstance, nativeMediaViewClass.cast(unifiedNativeAd.getAdMediaView()));
                } else {
                    layoutMediaView.setVisibility(GONE);
                }
            }

            Method getProviderView = nativeAdClass.getMethod("getProviderView", Context.class);
            View providerView = (View) getProviderView.invoke(nativeAdInstance, context);
            if (providerView != null) {
                if (providerView.getParent() != null && providerView.getParent() instanceof ViewGroup) {
                    ((ViewGroup) providerView.getParent()).removeView(providerView);
                }
                layoutAdChoice.addView(providerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                Method setProviderView = nativeAdViewClass.getMethod("setProviderView", View.class);
                setProviderView.invoke(nativeAdViewInstance, layoutAdChoice);
            }
            Method registerView = nativeAdViewClass.getMethod("registerView", nativeAdClass);
            registerView.invoke(nativeAdViewInstance, nativeAdInstance);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private boolean hasMediaView() {
        return adStyle.isUseMediaView() && layoutMediaView != null;
    }

    private void setupAdViewForAdMob(MediationNativeAd unifiedNativeAd, Context context) {
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class<?> nativeAdViewClass = classLoader.loadClass(ADMOB_NATIVE_AD_VIEW);
            Constructor<?> constructor = nativeAdViewClass.getDeclaredConstructor(Context.class);
            ViewGroup nativeAdViewInstance = (ViewGroup) nativeAdViewClass.cast(constructor.newInstance(context));

            Method setLayoutParams = nativeAdViewClass.getMethod("setLayoutParams", ViewGroup.LayoutParams.class);
            setLayoutParams.invoke(nativeAdViewInstance, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            Class<?> mediaViewClass = classLoader.loadClass("com.google.android.gms.ads.nativead.MediaView");
            Object mediaViewInstance = mediaViewClass.cast(unifiedNativeAd.getAdMediaView());
            Method setMediaView = nativeAdViewClass.getMethod("setMediaView", mediaViewClass);
            setMediaView.invoke(nativeAdViewInstance, mediaViewInstance);

            Method setHeadlineView = nativeAdViewClass.getMethod("setHeadlineView", View.class);
            setHeadlineView.invoke(nativeAdViewInstance, tvNativeTitle);
            Method setBodyView = nativeAdViewClass.getMethod("setBodyView", View.class);
            setBodyView.invoke(nativeAdViewInstance, tvNativeBody);
            Method setIconView = nativeAdViewClass.getMethod("setIconView", View.class);
            setIconView.invoke(nativeAdViewInstance, viewIcon);
            Method setCallToActionView = nativeAdViewClass.getMethod("setCallToActionView", View.class);
            setCallToActionView.invoke(nativeAdViewInstance, btNativeCta);

            Class<?> nativeAdClass = classLoader.loadClass(ADMOB_NATIVE_AD);
            Method setNativeAd = nativeAdViewClass.getMethod("setNativeAd", nativeAdClass);
            setNativeAd.invoke(nativeAdViewInstance, nativeAdClass.cast(unifiedNativeAd.getAdLoadedInstance()));

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void setTitleTextColor(@ColorRes int colorRes) {
        if (this.tvNativeTitle != null) {
            this.tvNativeTitle.setTextColor(ContextCompat.getColor(getContext(), colorRes));
        }
    }

    public void setDescriptionTextColor(@ColorRes int colorRes) {
        if (this.tvNativeBody != null) {
            this.tvNativeBody.setTextColor(ContextCompat.getColor(getContext(), colorRes));
        }
    }

    public void setLayoutAdContentBackground(@ColorRes int resId) {
        if (this.layoutAdContent != null) {
            this.layoutAdContent.setBackgroundColor(ContextCompat.getColor(getContext(), resId));
        }
    }
}
