package com.boniu.advertissdk;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

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


    public static final String APP_NAME = "超级表情包";

    //开屏广告
//    public static final String CSJ_APPID = "5040073";
    public static final String CSJ_APPID = "5095422";//测试


    public static final String CSJ_CODEID = "840073445";//840073445


    public static final String GDT_APP_ID = "1109940216";
    public static final String GDT_POS_ID = "1010480759612140";


    public static final String SHOW_SPLASHAD = "SHOW_SPLASHAD";//显示广告类型  true gdt  false 穿山甲

    public static final String ADNAME_GDT = "ADNAME_GDT";// 广点通
    public static final String ADNAME_BU = "ADNAME_BU";// 穿山甲

    @Nullable
    private IStartNext startNext = null;

    private TTAdNative mTTAdNative;
    private SplashAD gdtSplashAD;

    private SharedPreferences sharedPreferences;

    private List<String> adLists = new ArrayList<>();

    private boolean isHuawei = false;

    public SplashAdManager() {

    }

    public void ShowSplashAd(Activity activity, ViewGroup container, IStartNext startNext) {

        this.sharedPreferences = activity.getSharedPreferences("privacy", MODE_PRIVATE);
        /// 每次从activity加载广告 重置数组
        this.adLists.clear();
        this.adLists.add(ADNAME_BU);
        this.adLists.add(ADNAME_GDT);

        this.startNext = startNext;

        if (isHuawei) {
            loadGDTSplashAD(activity, container);
            return;
        }
        loadSplashAd(activity, container);
    }

    // 展示广告
    private void loadSplashAd(Activity activity, ViewGroup container) {

        String lastAdName = sharedPreferences.getString(SHOW_SPLASHAD, ADNAME_BU);
        Log.e(TAG, "loadSplashAd: " + lastAdName );
        // 数组为空 广告循环结束
        if (adLists == null || adLists.size() == 0) {
            // 关闭
            next();
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
                loadGDTSplashAD(activity, container);
                break;
            default:
                loadBUSplashAd(activity, container);
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
     * @param activity
     * @param container
     */
    private void loadBUSplashAd(final Activity activity, final ViewGroup container) {
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
                loadSplashAd(activity, container);
            }

            @Override
            public void onTimeout() {
                // 加载超时， 调用其他广告
                loadSplashAd(activity, container);
            }

            @Override
            public void onSplashAdLoad(TTSplashAd ttSplashAd) {
                if (ttSplashAd == null || ttSplashAd.getSplashView() == null) {
                    next();
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
                        // 调过
                        next();
                    }

                    @Override
                    public void onAdTimeOver() {
                        // 关闭
                        next();
                    }
                });
            }
        });
    }

    /**
     * 广点通
     *
     * @param activity
     * @param container
     */
    private void loadGDTSplashAD(final Activity activity, final ViewGroup container) {
        gdtSplashAD = new SplashAD(activity, GDT_POS_ID, new SplashADListener() {
            @Override
            public void onADDismissed() {
                next();

            }

            @Override
            public void onNoAD(AdError adError) {
                Log.e(TAG, "onNoAD: " + adError.getErrorMsg() + ":" + adError.getErrorCode() );
                // 华为直接进入
                if (isHuawei) {
                    next();
                    return;
                }
                // 加载失败， 调用其他广告
                loadSplashAd(activity, container);
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



    // 关闭activity
    public void next() {
        if (startNext != null) {
            startNext.startNext();
        }
    }
}
