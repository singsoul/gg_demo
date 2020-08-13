package com.boniu.ggdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.boniu.advertissdk.RewardAdManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RewardAdManager().ShowSplashAd(MainActivity.this,new RewardAdManager.RewardAdManagerListener() {
                    @Override
                    public void rewardVideAdComplete() {
                        //成功
                    }

                    @Override
                    public void rewardVideAdClose() {
                        //失败
                    }
                });
            }
        });
    }
}
