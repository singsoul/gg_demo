package com.jisuanqi.xiaodong;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.boniu.ad.SplashConfig;

public class App extends Application {
    private App app;
    public static final String END_TIME = "END_TIME";

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        SplashConfig.init(app,"com.jisuanqi.xiaodong","计算器");
//        this.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);

    }

    private ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        }
        private int appCount = 0;
        private boolean isForground = true;
        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            appCount++;
            if (!isForground) {
                isForground = true;
                long aLong = SPUtils.getInstance(app).getLong(END_TIME, 0);
                if (aLong != 0 && System.currentTimeMillis() - aLong > 10000) {
                    Intent intent = new Intent(app, SplashActivity.class);
                    intent.putExtra(SplashActivity.IS_TOMAIN, false);
                    startActivity(intent);
                    SPUtils.getInstance(app).put(END_TIME, 0L);
                }
                Log.e("AppLifecycle", "app into forground 前台 ");
            }
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {

        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {

        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
            appCount--;
            if (!isForgroundAppValue()) {
                isForground = false;
                SPUtils.getInstance(app).put(END_TIME, System.currentTimeMillis());
                Log.e("AppLifecycle", "app into background 后台");
            }
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {

        }

        private boolean isForgroundAppValue() {
            return appCount > 0;
        }
    };
}
