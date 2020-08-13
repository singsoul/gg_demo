package com.boniu.advertissdk;

import android.content.Context;

import com.qq.e.comm.managers.GDTADManager;

public class SplashConfig {
    public static void init(Context context){
        //穿山甲SDK初始化
        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
        TTAdManagerHolder.init(context);

        GDTADManager.getInstance().initWith(context, SplashAdManager.GDT_APP_ID);
    }
}
