package com.treefinance.gongfudai;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.boniu.ad.SplashAdManager;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private boolean isTomain = true;
    public static final String IS_TOMAIN = "IS_TOMAIN";
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        isTomain = getIntent().getBooleanExtra(IS_TOMAIN,true);
        frameLayout = ((FrameLayout) findViewById(R.id.framelayout));
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
        }else{
            new SplashAdManager().ShowSplashAd(this,frameLayout,iStartNext,"9edf7f0485b94fbea8bfb01bafeaaf27");
        }
    }

    private SplashAdManager.IStartNext iStartNext = new SplashAdManager.IStartNext() {
        @Override
        public void startNext() {
            gotoNext();
        }

        @Override
        public void onerror(String msg) {
            Log.e("MainActivity", "onerror: " + msg );
            gotoNext();
        }

    };



    /**
     * ----------非常重要----------
     * <p>
     * Android6.0以上的权限适配简单示例：
     * <p>
     * 如果targetSDKVersion >= 23，那么建议动态申请相关权限，再调用广点通SDK
     * <p>
     * SDK不强制校验下列权限（即:无下面权限sdk也可正常工作），但建议开发者申请下面权限，尤其是READ_PHONE_STATE权限
     * <p>
     * READ_PHONE_STATE权限用于允许SDK获取用户标识,
     * 针对单媒体的用户，允许获取权限的，投放定向广告；不允许获取权限的用户，投放通投广告，媒体可以选择是否把用户标识数据提供给优量汇，并承担相应广告填充和eCPM单价下降损失的结果。
     * <p>
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
     * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        // 如果需要的权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() != 0) {
            // 否则，建议请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        } else {
            new SplashAdManager().ShowSplashAd(this,frameLayout,iStartNext,"9edf7f0485b94fbea8bfb01bafeaaf27");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024) {
            gotoNext();
        }
    }

    private void gotoNext() {
        if (isTomain){
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        }
        finish();

    }
}
