package com.awesome.mediation;

import android.content.Context;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

import com.awesome.mediation.nativetemplate.R;

public enum NativeAdTemplate {
    DEFAULT("default", R.drawable.mdl_bg_cta_default, R.color.mdl_white, true),

    MEDIA_1("media_1", R.drawable.mdl_selector_cta_green, R.color.cta_text_dark, true),

    MEDIA_2("media_2", R.drawable.mdl_selector_cta_blue_gradient, R.color.mdl_white, true),

    MEDIA_3("media_3", R.drawable.mdl_selector_cta_blue_gradient_2, R.color.mdl_white, true),

    MEDIA_4("media_4", R.drawable.mdl_selector_cta_blue_gradient, R.color.mdl_white, true),

    MEDIA_5("media_5", R.drawable.mdl_selector_cta_blue_gradient, R.color.mdl_white, true),

    MEDIA_6("media_6", R.drawable.mdl_selector_cta_blue_gradient, R.color.mdl_white, true),

    NO_MEDIA_1("no_media_1", R.drawable.mdl_selector_cta_blue_gradient_2, R.color.mdl_white, false),

    NO_MEDIA_2("no_media_2", R.drawable.mdl_selector_cta_blue_gradient_2, R.color.mdl_white, false),

    NO_MEDIA_3("no_media_3", R.drawable.mdl_selector_cta_green, R.color.cta_text_dark, false);

    private final String name;
    private final int ctaBackground;
    private final int ctaTextColor;
    private final boolean useMediaView;

    NativeAdTemplate(String name, @DrawableRes int ctaBackground, @ColorRes int ctaTextColor, boolean useMediaView) {
        this.name = name;
        this.ctaBackground = ctaBackground;
        this.ctaTextColor = ctaTextColor;
        this.useMediaView = useMediaView;
    }

    public int getCtaBackground() {
        return ctaBackground;
    }

    @ColorRes
    public int getCtaTextColor() {
        return ctaTextColor;
    }

    public String getName() {
        return name;
    }

    public boolean isUseMediaView() {
        return useMediaView;
    }

    public int getLayoutRes(Context context) {
        return context.getResources().getIdentifier("mdl_template_" + name, "layout", context.getPackageName());
    }

    public static NativeAdTemplate lookup(String name) {
        for (NativeAdTemplate e : NativeAdTemplate.values()) {
            if (e.name.equalsIgnoreCase(name)) {
                return e;
            }
        }
        return DEFAULT;
    }

    public static NativeAdTemplate lookup(int index) {
        NativeAdTemplate[] values = NativeAdTemplate.values();
        if (index < 0 || index > values.length) {
            return values[0];
        }
        return values[index];
    }
}
