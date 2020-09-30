package com.boniu.ad;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.boniu.ad.bean.AdvetisingInitBean;
import com.boniu.ad.bean.MeterialBean;
import com.boniu.ad.config.ConfigKeys;
import com.boniu.ad.download.UpdateService;
import com.boniu.ad.interfaces.MeterialInterfaces;
import com.boniu.ad.utils.TextUtils;
import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by woookliu on 21/09/2017.  implements SplashADListener
 */

public class SplashAdManager {

    public static final String TAG = SplashAdManager.class.getSimpleName();


    public String CSJ_APPID = "";
    public String CSJ_CODEID = "";

    public String GDT_APP_ID = "";
    public String GDT_POS_ID = "";

    public String ZHIKE_POS_ID = "";//直客广告位id
    public String ZHIKE_APP_ID = "";//直客appid


    public static final String SHOW_SPLASHAD = "SHOW_SPLASHAD";//显示广告类型  true gdt  false 穿山甲

    public static final String ADNAME_GDT = "ADNAME_GDT";// 广点通
    public static final String ADNAME_BU = "ADNAME_BU";// 穿山甲
    public static final String ADNAME_ZHIKE = "ADNAME_ZHIKE";//直客

    @Nullable
    private IStartNext startNext = null;

    private TTAdNative mTTAdNative;
    private SplashAD gdtSplashAD;

    private SharedPreferences sharedPreferences;

    private List<String> adLists = new ArrayList<>();
    private Activity activity;
    private ViewGroup container;

    private String errorMsg;
    public SplashAdManager() {

    }

    /**
     *
     * @param activity
     * @param container  广告显示的布局
     * @param startNext
     */
    public void ShowSplashAd(Activity activity, ViewGroup container, IStartNext startNext) {
        this.startNext = startNext;
        this.activity = activity;
        this.container = container;
        this.sharedPreferences = activity.getSharedPreferences("privacy", MODE_PRIVATE);
        List<AdvetisingInitBean.SdkAdverVOListBean.ListBean> splashList = SplashSingleton.getInstance().getSplashList(0);
        if (splashList == null){
            startNext.onerror("暂无开屏广告");
            return;
        }
        this.adLists.clear();
        for (int i = 0; i < splashList.size(); i++) {
            AdvetisingInitBean.SdkAdverVOListBean.ListBean listBean = splashList.get(i);
            if (listBean.isIfDirectCus()){
                this.adLists.add(ADNAME_ZHIKE);
                ZHIKE_APP_ID = listBean.getAppId();
                ZHIKE_POS_ID = listBean.getAdvertisingSpaceId();
                continue;
            }
            if ("csj".equals(listBean.getAdvertiserNo())){
                this.adLists.add(ADNAME_BU);
                CSJ_APPID = listBean.getAppId();
                CSJ_CODEID = listBean.getAdvertisingSpaceId();
            }
            if ("ylh".equals(listBean.getAdvertiserNo())){
                this.adLists.add(ADNAME_GDT);
                GDT_APP_ID = listBean.getAppId();
                GDT_POS_ID = listBean.getAdvertisingSpaceId();
            }

        }


        loadSplashAd();

    }

    // 展示广告
    private void loadSplashAd() {

        String lastAdName = sharedPreferences.getString(SHOW_SPLASHAD, ADNAME_BU);
        Log.e(TAG, "loadSplashAd: " + lastAdName );
        // 数组为空 广告循环结束
        if (adLists == null || adLists.size() == 0) {
            // 关闭
            startNext.onerror(TextUtils.isEmpty(errorMsg)?"暂无广告":errorMsg);
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
                loadGDTSplashAD();
                break;
            case ADNAME_BU:
                loadBUSplashAd();
                break;
            case ADNAME_ZHIKE:
                //直客
                loadZhiKeSplashAd();
                break;
            default:
                startNext.startNext();
                break;
        }
    }


    private void saveSplashAdToSP(String lastAdName) {
        Log.e(TAG, "saveSplashAdToSP: " + lastAdName );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //步骤3：将获取过来的值放入文件
        editor.putString(SHOW_SPLASHAD, lastAdName);
        //步骤4：提交
        editor.commit();
    }

    /**
     * 穿山甲
     *
     */
    private void loadBUSplashAd() {
        Log.e(TAG, "loadBUSplashAd: " + CSJ_CODEID );
        mTTAdNative = TTAdSdk.getAdManager().createAdNative(activity);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(CSJ_CODEID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .build();
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            public void onError(int i, String s) {
                // 加载失败， 调用其他广告
                errorMsg = s;
                SplashConfig.splashSave(CSJ_APPID,CSJ_CODEID,s,i+ "","1");

                loadSplashAd();
            }

            @Override
            public void onTimeout() {
                // 加载超时， 调用其他广告
                errorMsg = "穿山甲加载超时";
                SplashConfig.splashSave(CSJ_APPID,CSJ_CODEID,errorMsg, ConfigKeys.TIME_OUT + "","1");

                loadSplashAd();
            }

            @Override
            public void onSplashAdLoad(TTSplashAd ttSplashAd) {
                if (ttSplashAd == null || ttSplashAd.getSplashView() == null) {
                    errorMsg = "暂无穿山甲广告";
                    SplashConfig.splashSave(CSJ_APPID,CSJ_CODEID,errorMsg, ConfigKeys.EMPTY_SPLASH + "","1");

                    loadSplashAd();
                }
                View view = ttSplashAd.getSplashView();
                container.removeAllViews();
                container.addView(view);

                ttSplashAd.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int i) {

                    }

                    @Override
                    public void onAdShow(View view, int i) {

                    }

                    @Override
                    public void onAdSkip() {
                        startNext.startNext();
                        SplashConfig.splashSave(CSJ_APPID,CSJ_CODEID,"",  "","0");

                        //跳过
                        Log.e(TAG, "onAdSkip: " );
                    }

                    @Override
                    public void onAdTimeOver() {
                        SplashConfig.splashSave(CSJ_APPID,CSJ_CODEID,"",  "","0");

                        Log.e(TAG, "onAdTimeOver: "  );
                        // 倒计时结束
                        startNext.startNext();
                    }
                });
            }
        });
    }

    /**
     * 广点通
     *
     */
    private void loadGDTSplashAD() {
        Log.e(TAG, "loadGDTSplashAD: " + GDT_POS_ID );
        gdtSplashAD = new SplashAD(activity, GDT_POS_ID, new SplashADListener() {
            @Override
            public void onADDismissed() {
                startNext.startNext();
                SplashConfig.splashSave(CSJ_APPID,CSJ_CODEID,"","","0");

            }

            @Override
            public void onNoAD(AdError adError) {
                errorMsg = adError.getErrorMsg();
                SplashConfig.splashSave(CSJ_APPID,CSJ_CODEID,adError.getErrorMsg(),adError.getErrorCode()+"","1");
                loadSplashAd();
            }

            @Override
            public void onADPresent() {

            }

            @Override
            public void onADClicked() {

            }

            @Override
            public void onADTick(long l) {

            }

            @Override
            public void onADExposure() {

            }

            @Override
            public void onADLoaded(long l) {

            }
        },0);

        gdtSplashAD.fetchAndShowIn(container);
    }


    private void loadZhiKeSplashAd(){
        SplashConfig.getMaterial(ZHIKE_APP_ID,ZHIKE_POS_ID,meterialInterfaces);


    }

    private MeterialInterfaces meterialInterfaces = new MeterialInterfaces() {
        @Override
        public void meterialList(List<MeterialBean> list) {
            if (list != null && list.size() > 0){
                final MeterialBean meterialBean = list.get(0);
                View skipLayout = LayoutInflater.from(activity).inflate(R.layout.framelayout_meterial_open, null);
                container.addView(skipLayout);
                ImageView imgBg =  skipLayout.findViewById(R.id.img_bg);
                final TextView tvTiaoguo = skipLayout.findViewById(R.id.tv_tiaoguo);
                Glide.with(activity).load(meterialBean.getLineMate().get(0)).into(imgBg);
                CountDownTimer timer = new CountDownTimer(3000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        tvTiaoguo.setText("跳过" + millisUntilFinished / 1000);
                    }
                    public void onFinish() {
                        SplashConfig.splashSave(CSJ_APPID,CSJ_CODEID,"",  "","0");

                        startNext.startNext();
                    }
                };
                //调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
                timer.start();

                imgBg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (meterialBean.getOpenMode() == 2) {
                            Intent intent = new Intent(activity, UpdateService.class);
                            intent.putExtra(UpdateService.APP_URL,meterialBean.getJumpUrl());
                            intent.putExtra(UpdateService.APP_NAME,meterialBean.getVname());
                            intent.putExtra(UpdateService.APP_LOGO,meterialBean.getLogoUrl()+"");
                            activity.startService(intent);
                        }else{
                            String url = "";
                            if (meterialBean.getJumpUrl().contains("http")){
                                url = meterialBean.getJumpUrl()+"";
                            }else{
                                url = "http://" + meterialBean.getJumpUrl();
                            }
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            activity.startActivity(intent);
                        }

                    }
                });
                tvTiaoguo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startNext.startNext();
                    }
                });
            }else{
                errorMsg = "暂无直客广告";
                SplashConfig.splashSave(CSJ_APPID,CSJ_CODEID,errorMsg,  ConfigKeys.EMPTY_SPLASH+"","1");

                loadSplashAd();
            }

        }

        @Override
        public void onError(String msg) {
            errorMsg = msg;
            SplashConfig.splashSave(CSJ_APPID,CSJ_CODEID,errorMsg,  ConfigKeys.ZHIKE_ERROR+"","1");

            loadSplashAd();
        }
    };

    public interface IStartNext {

        void startNext();

        void onerror(String msg);
    }

}
