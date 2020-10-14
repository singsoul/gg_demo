package com.treefinance.gongfudai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.boniu.ad.RewardAdManager;
import com.boniu.ad.download.UpdateService;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        findViewById(R.id.tv_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RewardAdManager().ShowSplashAd(MainActivity.this,new RewardAdManager.RewardAdManagerListener() {
                    @Override
                    public void rewardVideAdComplete() {
                        //成功
                        Log.e(TAG, "rewardVideAdComplete: " );
                    }

                    @Override
                    public void rewardVideAdClose() {

                        Log.e(TAG, "rewardVideAdClose: "  );
                        //关闭
                    }

                    @Override
                    public void rewardError(String msg) {
                        //错误
                        Log.e(TAG, "rewardError: " + msg);
                    }
                });
            }
        });



        findViewById(R.id.tv_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UpdateService.class);
                intent.putExtra(UpdateService.APP_URL,"https://imtt.dd.qq.com/16891/apk/2F7B82DC8EBE1D0B4A1C4ECD6311D6BA.apk?fsname=com.dewmobile.kuaiya_5.9.3 (CN)_341.apk&csr=1bbd");
                intent.putExtra(UpdateService.APP_NAME,"下载apk");
                intent.putExtra(UpdateService.APP_LOGO,"http://boniuearth.oss-cn-hangzhou.aliyuncs.com/c864d100eaa147579c318dd630eec42d.jpg");
                startService(intent);
//                Uri uri = Uri.parse("http://www.baidu.com");
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
            }
        });
    }




}
