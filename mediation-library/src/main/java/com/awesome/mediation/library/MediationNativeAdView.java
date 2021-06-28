package com.awesome.mediation.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.awesome.mediation.library.base.MediationNativeAd;
import com.awesome.mediation.library.util.MediationAdLogger;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MediationNativeAdView extends LinearLayout {

    private static final String TAG = "MEDIATION-NATIVE";
    private static final String ADMOB_NATIVE_AD_VIEW = "com.google.android.gms.ads.nativead.NativeAdView";
    private static final String ADMOB_NATIVE_AD = "com.google.android.gms.ads.nativead.NativeAd";

    private TextView tvNativeTitle;
    private TextView tvNativeBody;
    private TextView btNativeCta;
    private LinearLayout viewIcon;
    private FrameLayout layoutAdChoice;
    private FrameLayout layoutContentAd;
    private LinearLayout layoutRootAd;
    private RelativeLayout layoutMediaView;
    private Style adStyle = Style.MEDIA_VIEW;
    private boolean adAttached;
    private View nativeAdContentLayout;
    private ViewGroup layoutAdContent;

    public MediationNativeAdView(Context context) {
        super(context);
        initView(null);
    }

    public MediationNativeAdView(Context context, Style style) {
        super(context);
        this.adStyle = style;
        initView(null);
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

        inflate(getContext(), R.layout.mdl_native_ad_view, this);

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.MediationNativeAdView, 0, 0);

        if (typedArray.hasValue(R.styleable.MediationNativeAdView_ad_style)) {
            int style = typedArray.getInt(R.styleable.MediationNativeAdView_ad_style, 0);
            this.adStyle = Style.fromId(style);
        }

        this.viewIcon = this.findViewById(R.id.native_ad_ad_icon_layout);
        this.layoutMediaView = this.findViewById(R.id.layout_media_view);
        this.layoutMediaView.setVisibility(this.adStyle == Style.MEDIA_VIEW ? View.VISIBLE : GONE);

        this.tvNativeTitle = this.findViewById(R.id.native_ad_title);
        this.tvNativeBody = this.findViewById(R.id.native_ad_body);
        this.btNativeCta = this.findViewById(R.id.native_cta);
        this.showLoadingState(tvNativeTitle, tvNativeBody, btNativeCta, viewIcon, layoutMediaView);

        this.layoutAdChoice = this.findViewById(R.id.native_adchoice_view);
        this.layoutContentAd = this.findViewById(R.id.layout_content_ad);
        this.layoutRootAd = this.findViewById(R.id.root_ad_view);
        this.nativeAdContentLayout = this.findViewById(R.id.native_ad_content_layout);
        this.layoutAdContent = findViewById(this.getResources().getIdentifier("layout_ad_content", "id",
                getContext().getPackageName()));
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
            view.setActivated(active);
        }
    }

    public enum Style {
        NORMAL(0), MEDIA_VIEW(1);
        int id;

        Style(int id) {
            this.id = id;
        }

        static Style fromId(int id) {
            for (Style f : values()) {
                if (f.id == id) return f;
            }
            throw new IllegalArgumentException();
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

        Log.i("superman", "show: " + unifiedNativeAd.getAdTitle());
        Context context = getContext();
        FrameLayout nativeAdView = new FrameLayout(context);
        Class<?> adContainerClass = unifiedNativeAd.getAdContainerClass();
        String name = adContainerClass.getName();
        if (ADMOB_NATIVE_AD_VIEW.equalsIgnoreCase(name)) {
            setupAdViewForAdMob(unifiedNativeAd, context);
        }

        this.tvNativeTitle.setText(unifiedNativeAd.getAdTitle());
        this.tvNativeBody.setText(unifiedNativeAd.getAdBody());
        this.btNativeCta.setBackgroundResource(R.drawable.mdl_bg_cta);
        this.btNativeCta.setText(unifiedNativeAd.getAdCallToAction());

        ViewGroup adMediaView = unifiedNativeAd.getAdMediaView();
        if (this.adStyle == Style.MEDIA_VIEW && adMediaView != null) {
            this.layoutMediaView.addView(adMediaView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            this.layoutMediaView.setVisibility(VISIBLE);
        }
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
        nativeAdView.addView(layoutContentAd);
        this.layoutRootAd.addView(nativeAdView);

        this.layoutRootAd.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
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
            Log.i("superman", "show: " + e.getMessage());
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
