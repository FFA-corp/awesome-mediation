package com.awesome.mediation.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
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

public class MediationNativeAdView extends LinearLayout {

    private TextView tvNativeTitle;
    private TextView tvNativeBody;
    private TextView btNativeCta;
    private LinearLayout viewIcon;
    private FrameLayout layoutAdChoice;
    private FrameLayout layoutContentAd;
    private LinearLayout layoutRootAd;
    private RelativeLayout layoutMediaView;
    private Style adStyle = Style.NORMAL;
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
        this.adAttached = true;
        this.layoutContentAd.setVisibility(VISIBLE);
        this.viewIcon.removeAllViews();
        this.layoutAdChoice.removeAllViews();
        this.layoutRootAd.removeAllViews();

        this.tvNativeTitle.setText(unifiedNativeAd.getHeadline());
        this.tvNativeBody.setText(unifiedNativeAd.getBody());
        this.btNativeCta.setText(unifiedNativeAd.getCallToAction());
        this.hideLoadingState(tvNativeTitle, tvNativeBody, btNativeCta, viewIcon, layoutMediaView);
//
//        com.google.android.gms.ads.nativead.NativeAdView unifiedNativeAdView = new com.google.android.gms.ads.nativead.NativeAdView(getContext());
//        unifiedNativeAdView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//
//        if (this.adStyle == Style.MEDIA_VIEW) {
//            MediaView admobMediaView = new MediaView(getContext());
//            this.layoutMediaView.addView(admobMediaView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//            this.layoutMediaView.setVisibility(VISIBLE);
//            unifiedNativeAdView.setMediaView(admobMediaView);
//        }
//
//        ImageView admobIcon = new ImageView(getContext());
//        this.viewIcon.addView(admobIcon, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//        NativeAd.Image icon = unifiedNativeAd.getIcon();
//        if (icon != null && icon.getDrawable() != null) {
//            admobIcon.setImageDrawable(icon.getDrawable());
//        }
//        unifiedNativeAdView.setHeadlineView(tvNativeTitle);
//        unifiedNativeAdView.setCallToActionView(btNativeCta);
//        unifiedNativeAdView.setBodyView(tvNativeBody);
//        unifiedNativeAdView.setIconView(admobIcon);
//        unifiedNativeAdView.setNativeAd(unifiedNativeAd);
//
//        AdChoicesView adChoicesView = new AdChoicesView(layoutContentAd.getContext());
//        if (unifiedNativeAd.getAdChoicesInfo() != null) {
//            Log.i("superman", "show: 1");
//            AdChoicesView choicesView = new AdChoicesView(unifiedNativeAdView.getContext());
//            unifiedNativeAdView.setAdChoicesView(choicesView);
//        }
//
//        Log.i("superman", "show: 2");
//        layoutAdChoice.addView(adChoicesView);
//        unifiedNativeAdView.addView(layoutContentAd);
//        this.layoutRootAd.addView(unifiedNativeAdView);

        // TODO: 23/06/2021 Process multiple network
        this.layoutRootAd.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
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