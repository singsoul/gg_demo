package com.boniu.ad.request;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


public class ApiHelper {
    private static final String TAG = ApiHelper.class.getSimpleName();

    public static final String BASE_URI = "https://ad.rhinox.cn/";
    public static final String ADVERTISEMRNT_LIST = BASE_URI + "client/sdk/getData";
    public static final String GET_METERIAL = BASE_URI + "client/sdk/getMaterial";

    public static final String LOG_SAVE = BASE_URI + "client/log/save";

    public static String getVersionName(Context context) {
        String version = "0.0.00";
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo == null ? version : packageInfo.versionName;
    }

    private static PackageInfo getPackageInfo(Context context) {
        String packageName = context.getPackageName();
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

}
