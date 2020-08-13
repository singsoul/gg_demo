package com.boniu.advertissdk;

import android.app.Activity;
import android.util.Log;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;


import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RewardAdManager {
    public static final String TAG = RewardAdManager.class.getSimpleName();
    public static final String SHOW_REWARDAD = "SHOW_REWARDAD";//显示广告类型  true gdt  false 穿山甲
    public static final String ADNAME_GDT = "ADNAME_GDT";// 广点通
    public static final String ADNAME_BU = "ADNAME_BU";// 穿山甲

    private String gdt_app_id = "1110326112";
    private String gdt_pos_id = "2001114717254536";


    private String csj_pos_id = "945187758";

    private SharedPreferences sharedPreferences;
    private List<String> adLists = new ArrayList<>();



    private RewardAdManagerListener AdListener = null;

    private RewardVideoAD gdtRewardVideoAD;
    private TTAdNative mTTAdNative;
    private TTRewardVideoAd buRewardVideoAd;


    public interface RewardAdManagerListener {
        void rewardVideAdComplete();
        void rewardVideAdClose();
    }


    public void ShowSplashAd(Activity activity, RewardAdManagerListener listener) {

        /// 每次加载广告 重置数组
        this.adLists.clear();
        this.adLists.add(ADNAME_BU);
        this.adLists.add(ADNAME_GDT);

        this.sharedPreferences = activity.getSharedPreferences("privacy", MODE_PRIVATE);
        this.AdListener = listener;
        showRewardad(activity);

    }

    private void saveSplashAdToSP(String lastAdName) {
        Log.e(TAG, "saveSplashAdToSP: " + lastAdName );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //步骤3：将获取过来的值放入文件
        editor.putString(SHOW_REWARDAD, lastAdName);
        //步骤4：提交
        editor.commit();
    }

    private void showRewardad(Activity activity) {
        String lastAdName = sharedPreferences.getString(SHOW_REWARDAD, ADNAME_BU);
        Log.e(TAG, "showRewardad: " + lastAdName );
        // 数组为空 广告循环结束
        if (adLists == null || adLists.size() == 0) {
            AdListener.rewardVideAdClose();
            return;
        }

        int index = adLists.indexOf(lastAdName);
        index = index < 0 ? 0 : index;

        // 保存下次要展示的
        int newIndex = index + 1;
        if (newIndex >= adLists.size()) {
            newIndex = 0;
        }
        saveSplashAdToSP(adLists.get(newIndex));

        // 移除这次展示的
        adLists.remove(index);

        // 显示广告
        switch (lastAdName) {
            case ADNAME_GDT:
                showGDTRewardVideoAd(activity);
                break;
            default:
                showBURewardVideoAd(activity);
                break;
        }
    }

    /**
     * 显示广点通广告
     */
    private void showGDTRewardVideoAd(final Activity activity) {
        RewardVideoADListener listener = new RewardVideoADListener() {

            @Override
            public void onADLoad() {
                gdtRewardVideoAD.showAD();
            }

            @Override
            public void onVideoCached() {

            }

            @Override
            public void onADShow() {

            }

            @Override
            public void onADExpose() {

            }

            @Override
            public void onReward() {

            }

            @Override
            public void onADClick() {

            }

            @Override
            public void onVideoComplete() {
                // 播放完成
                AdListener.rewardVideAdComplete();
            }

            @Override
            public void onADClose() {
                /// 关闭
//                AdListener.rewardVideAdClose();
            }

            @Override
            public void onError(AdError adError) {
                showRewardad(activity);
            }
        };
        gdtRewardVideoAD = new RewardVideoAD(activity, gdt_app_id, gdt_pos_id, listener);
        gdtRewardVideoAD.loadAD();
    }

    /**
     * 显示穿山甲广告
     */
    private void showBURewardVideoAd(final Activity activity) {
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(csj_pos_id)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setOrientation(TTAdConstant.VERTICAL)
                .build();
        mTTAdNative = TTAdManagerHolder.get().createAdNative(activity);
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int i, String s) {
                showRewardad(activity);
            }

            @Override
            public void onRewardVideoAdLoad(final TTRewardVideoAd ttRewardVideoAd) {
                buRewardVideoAd = ttRewardVideoAd;
                ttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {

                    }

                    @Override
                    public void onDownloadActive(long l, long l1, String s, String s1) {

                    }

                    @Override
                    public void onDownloadPaused(long l, long l1, String s, String s1) {

                    }

                    @Override
                    public void onDownloadFailed(long l, long l1, String s, String s1) {

                    }

                    @Override
                    public void onDownloadFinished(long l, String s, String s1) {
                        ttRewardVideoAd.showRewardVideoAd(activity);
                    }

                    @Override
                    public void onInstalled(String s, String s1) {

                    }
                });

                ttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {
                    @Override
                    public void onAdShow() {

                    }

                    @Override
                    public void onAdVideoBarClick() {

                    }

                    @Override
                    public void onAdClose() {
//                        AdListener.rewardVideAdClose();
                    }

                    @Override
                    public void onVideoComplete() {
                        AdListener.rewardVideAdComplete();
                    }

                    @Override
                    public void onVideoError() {
                        // 错误
                        showRewardad(activity);
                    }

                    @Override
                    public void onRewardVerify(boolean b, int i, String s) {
                        Log.e("穿山甲RewardVerify", "b = " + b + " i = " + i +  " s = " + s);
                    }

                    @Override
                    public void onSkippedVideo() {

                    }
                });
            }

            @Override
            public void onRewardVideoCached() {
                buRewardVideoAd.showRewardVideoAd(activity);
            }
        });
    }


}
