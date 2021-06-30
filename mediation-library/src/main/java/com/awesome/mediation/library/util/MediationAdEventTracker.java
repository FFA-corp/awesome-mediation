package com.awesome.mediation.library.util;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Size;

import com.awesome.mediation.library.config.MediationAdManager;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class MediationAdEventTracker {
    private static final String TYPE = "type";
    private static final String CHANNEL = "channel";
    private static final String NAME = "name";
    private static final String EVENT = "event";
    private static final String ID = "id";
    private static final String MESSAGE = "message";
    private static volatile WeakReference<MediationAdEventTracker> mediationAdEventTracker;
    private final FirebaseAnalytics firebaseAnalytics;
    private final Context context;

    private MediationAdEventTracker(Context context) {
        this.firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        this.context = context;
    }

    public static MediationAdEventTracker instance(Context context) {
        if (mediationAdEventTracker == null || mediationAdEventTracker.get() == null) {
            mediationAdEventTracker = new WeakReference<>(new MediationAdEventTracker(context));
        }

        return mediationAdEventTracker.get();
    }

    public void log(@NonNull @Size(min = 1L, max = 32L) String eventName) {
        this.log(eventName, "");
    }

    public void log(@NonNull @Size(min = 1L, max = 32L) String eventName, @NonNull String message) {
        Bundle bundle = new Bundle();
        String msg = eventName;
        if (!TextUtils.isEmpty(message)) {
            msg = eventName + " | " + message;
            bundle.putString(MESSAGE, message);
        }

        MediationAdLogger.logI(msg);
        this.log(eventName, bundle);
    }

    public void log(@NonNull String type, @NonNull String channel, @NonNull String name, @NonNull String event, @NonNull String id) {
        this.log(type, channel, name, event, id, "");
    }

    public void log(@NonNull String type, @NonNull String channel, @NonNull String name, @NonNull String event, @NonNull String id, @NonNull String message) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(TYPE, type);
            bundle.putString(CHANNEL, channel);
            bundle.putString(NAME, name);
            bundle.putString(EVENT, event);
            bundle.putString(ID, id);
            bundle.putString(MESSAGE, message);
            this.log("ads", bundle);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

    }

    public void log(@NonNull @Size(min = 1L, max = 32L) String eventName, @NonNull Bundle bundle) {
        if (MediationAdManager.getInstance(context).isDebugMode()) {
            return;
        }
        try {
            long time = System.currentTimeMillis();
            bundle.putLong("time_long", time);
            bundle.putString("time_string", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())).format(new Date(time)));
            this.firebaseAnalytics.logEvent(eventName, bundle);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }
}