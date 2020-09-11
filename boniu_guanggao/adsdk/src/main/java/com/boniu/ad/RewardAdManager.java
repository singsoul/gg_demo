package com.boniu.ad;

import android.app.Activity;
import android.os.CountDownTimer;
import android.util.Log;

import com.boniu.ad.bean.AdvetisingInitBean;
import com.boniu.ad.bean.MeterialBean;
import com.boniu.ad.config.ConfigKeys;
import com.boniu.ad.interfaces.MeterialInterfaces;
import com.boniu.ad.utils.TextUtils;
import com.boniu.ad.view.VideoDialog;
import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;


import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    public static final String ADNAME_ZHIKE = "ADNAME_ZHIKE";//直客
    private String gdt_pos_id = "";
    private String gdt_app_id = "";

    private String csj_app_id = "";
    private String csj_pos_id = "";

    private String ZHIKE_POS_ID = "";//直客广告位id
    private String ZHIKE_APP_ID = "";//直客appid
    private SharedPreferences sharedPreferences;
    private List<String> adLists = new ArrayList<>();

    private Activity activity;

    private RewardAdManagerListener AdListener = null;

    private RewardVideoAD gdtRewardVideoAD;
    private TTAdNative mTTAdNative;
    private TTRewardVideoAd buRewardVideoAd;
    private String errorMsg = "";

    public interface RewardAdManagerListener {
        void rewardVideAdComplete();
        void rewardVideAdClose();

        void rewardError(String msg);
    }


    public void ShowSplashAd(Activity activity, RewardAdManagerListener listener) {
        this.AdListener = listener;
        this.activity = activity;
        List<AdvetisingInitBean.SdkAdverVOListBean.ListBean> videoList = SplashSingleton.getInstance().getSplashList(1);
        if (videoList == null || videoList.size() == 0){
            AdListener.rewardError(TextUtils.isEmpty(errorMsg)?"暂无激励视频":errorMsg);
            return;
        }
        this.adLists.clear();
        for (int i = 0; i < videoList.size(); i++) {
            AdvetisingInitBean.SdkAdverVOListBean.ListBean listBean = videoList.get(i);
            if (listBean.isIfDirectCus()){
                this.adLists.add(ADNAME_ZHIKE);
                ZHIKE_APP_ID = listBean.getAppId();
                ZHIKE_POS_ID = listBean.getAdvertisingSpaceId();
                continue;
            }
            if ("csj".equals(listBean.getAdvertiserNo())){
                this.adLists.add(ADNAME_BU);
                csj_pos_id = listBean.getAdvertisingSpaceId();
            }
            if ("ylh".equals(listBean.getAdvertiserNo())){
                this.adLists.add(ADNAME_GDT);
                gdt_pos_id = listBean.getAdvertisingSpaceId();

            }
        }

        this.sharedPreferences = activity.getSharedPreferences("privacy", MODE_PRIVATE);

        showRewardad();

    }

    private void saveSplashAdToSP(String lastAdName) {
        Log.e(TAG, "saveSplashAdToSP: " + lastAdName );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //步骤3：将获取过来的值放入文件
        editor.putString(SHOW_REWARDAD, lastAdName);
        //步骤4：提交
        editor.commit();
    }

    private void showRewardad() {
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
                showGDTRewardVideoAd();
                break;
            case ADNAME_BU:
                showBURewardVideoAd();
                break;
            case ADNAME_ZHIKE:
                //直客
                showZhikeVideo();
                break;
            default:
                break;
        }
    }

    /**
     * 显示广点通广告
     */
    private void showGDTRewardVideoAd() {
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
                SplashConfig.splashSave(gdt_app_id,gdt_pos_id,"",  "","0");

                AdListener.rewardVideAdComplete();
            }

            @Override
            public void onADClose() {
                /// 关闭
                AdListener.rewardVideAdClose();
            }

            @Override
            public void onError(AdError adError) {
                errorMsg = adError.getErrorMsg();
                SplashConfig.splashSave(gdt_app_id,gdt_pos_id,errorMsg,  adError.getErrorCode()+"","1");

                showRewardad();
            }
        };
        gdtRewardVideoAD = new RewardVideoAD(activity,gdt_pos_id,listener);
        gdtRewardVideoAD.loadAD();
    }

    /**
     * 显示穿山甲广告
     */
    private void showBURewardVideoAd() {
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
                errorMsg = s;
                SplashConfig.splashSave(csj_app_id,csj_pos_id,errorMsg,  i+"","1");

                showRewardad();
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
                        AdListener.rewardVideAdClose();
                    }

                    @Override
                    public void onVideoComplete() {
                        SplashConfig.splashSave(gdt_app_id,gdt_pos_id,"",  "","0");

                        AdListener.rewardVideAdComplete();
                    }

                    @Override
                    public void onVideoError() {
                        // 错误
                        SplashConfig.splashSave(csj_app_id,csj_pos_id,"穿山甲加载失败",  ConfigKeys.CSJ_ERROR+"","1");

                        showRewardad();
                    }

                    @Override
                    public void onRewardVerify(boolean b, int i, String s) {
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

    VideoDialog videoDialog;
    private void showZhikeVideo(){
        SplashConfig.getMaterial(ZHIKE_APP_ID,ZHIKE_POS_ID,meterialInterfaces);
    }

    private MeterialInterfaces meterialInterfaces = new MeterialInterfaces() {
        @Override
        public void meterialList(List<MeterialBean> list) {
            if (list == null  || list.size() <= 0){
                errorMsg = "直客激励视频为空";
                SplashConfig.splashSave(ZHIKE_APP_ID,ZHIKE_POS_ID,errorMsg,  ConfigKeys.EMPTY_SPLASH+"","1");

                showRewardad();
            }else{
                MeterialBean meterialBean = list.get(0);
                videoDialog = new VideoDialog(activity,meterialBean,AdListener);
                videoDialog.setCanceledOnTouchOutside(false);
                videoDialog.setCancelable(false);
                videoDialog.show();
            }

        }

        @Override
        public void onError(String msg) {
            errorMsg = msg;
            SplashConfig.splashSave(ZHIKE_APP_ID,ZHIKE_POS_ID,msg,  ConfigKeys.ZHIKE_ERROR+"","1");

            AdListener.rewardError(msg);
        }
    };



}
