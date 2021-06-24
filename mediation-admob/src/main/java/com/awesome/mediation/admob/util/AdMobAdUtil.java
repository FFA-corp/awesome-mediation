package com.awesome.mediation.admob.util;

import android.content.Context;
import android.provider.Settings;

import com.google.android.gms.ads.AdRequest;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AdMobAdUtil {
    public static AdRequest.Builder getAdRequestBuilderWithTestDevice(Context context) {
        return new AdRequest.Builder();
    }

    public static List<String> getDeviceTestId(Context context) {
        try {
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            return Collections.singletonList(EncryptionUtils.encodeMd5(androidId).toUpperCase(Locale.US));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}