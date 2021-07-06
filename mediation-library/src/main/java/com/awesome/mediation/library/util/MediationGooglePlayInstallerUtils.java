package com.awesome.mediation.library.util;

import android.content.Context;
import android.content.pm.InstallSourceInfo;
import android.content.pm.PackageManager;
import android.os.Build;

public class MediationGooglePlayInstallerUtils {

    private static final String GOOGLE_PLAY = "com.android.vending";

    public static boolean isInstalledViaGooglePlay(Context ctx) {
        return isInstalledVia(ctx, GOOGLE_PLAY);
    }

    public static boolean isInstalledVia(Context ctx, String required) {
        String installer = getInstallerPackageName(ctx);
        return required.equals(installer);
    }

    private static String getInstallerPackageName(Context ctx) {
        try {
            String packageName = ctx.getPackageName();
            PackageManager pm = ctx.getPackageManager();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                InstallSourceInfo info = pm.getInstallSourceInfo(packageName);
                if (info != null) {
                    return info.getInstallingPackageName();
                }
            }
            return pm.getInstallerPackageName(packageName);
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return "";
    }

}