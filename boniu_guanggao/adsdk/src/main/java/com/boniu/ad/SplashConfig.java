package com.boniu.ad;

import android.content.Context;
import android.util.Log;

import com.boniu.ad.aes.AESUtil;
import com.boniu.ad.bean.AdvetisingInitBean;
import com.boniu.ad.bean.MeterialBean;
import com.boniu.ad.config.ConfigKeys;
import com.boniu.ad.interfaces.MeterialInterfaces;
import com.boniu.ad.request.ApiManager;
import com.boniu.ad.request.AuthApi;
import com.boniu.ad.request.XCallback;
import com.boniu.ad.bean.ResultBean;
import com.boniu.ad.utils.DateUtils;
import com.boniu.ad.utils.SPUtils;
import com.boniu.ad.utils.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.qq.e.comm.managers.GDTADManager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class SplashConfig {
    public static final String TAG = "SplashConfig";
    private static Gson gson = new Gson();

    /**
     *
     * @param context
     * @param packageNmae 包名
     * @param appName appname
     */
    public static void init(final Context context, String packageNmae,String appName){
        SplashSingleton.getInstance().setContext(context);
        SplashSingleton.getInstance().setAppName(appName);
        SplashSingleton.getInstance().setPackageName(packageNmae);

        String times = SPUtils.getInstance(context).getString(ConfigKeys.SPLASH_TIME);
        String typeTime = DateUtils.getTypeTime();
        String splashJson = SPUtils.getInstance(context).getString(ConfigKeys.SPLASH_JSON);
        boolean isHttp = false;
        if (TextUtils.isEmpty(splashJson)){
            isHttp = true;
        }else{
            if (typeTime.equals(times)){
                isHttp = false;
            }else{
                isHttp = true;
            }
        }
        if (isHttp){
            getSplashHttp(packageNmae,context);
        }else{
            AdvetisingInitBean advetisingInitBean = gson.fromJson(splashJson, AdvetisingInitBean.class);
            List<AdvetisingInitBean.SdkAppIdVOListBean> sdkAppIdVOList = advetisingInitBean.getSdkAppIdVOList();
            for (int i = 0; i < sdkAppIdVOList.size(); i++) {
                AdvetisingInitBean.SdkAppIdVOListBean sdkAppIdVOListBean = sdkAppIdVOList.get(i);
                if ("csj".equals(sdkAppIdVOListBean.getAdvertiserNo())){
                    if (sdkAppIdVOListBean.getAppIdList() != null && sdkAppIdVOListBean.getAppIdList().size() > 0){
                        TTAdManagerHolder.init(context,sdkAppIdVOListBean.getAppIdList().get(0));
                    }
                }else if ("ylh".equals(sdkAppIdVOListBean.getAdvertiserNo())){
                    if (sdkAppIdVOListBean.getAppIdList() != null && sdkAppIdVOListBean.getAppIdList().size() > 0){
                        GDTADManager.getInstance().initWith(context, sdkAppIdVOListBean.getAppIdList().get(0));
                    }
                }
            }
            SplashSingleton.getInstance().setAdvetisingInitBean(advetisingInitBean);
            SplashSingleton.getInstance().setInit(true);
            SPUtils.getInstance(context).put(ConfigKeys.SPLASH_TIME,DateUtils.getTypeTime());
        }

    }


    public static void getSplashHttp(String packageNmae, final Context context){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mediaId",packageNmae + "");
        jsonObject.addProperty("platform","ANDROID");
        String encrypt = AESUtil.encrypt(jsonObject.toString(), ConfigKeys.AES_KEY);
        ApiManager.getInstance().create(AuthApi.class).getSplash(
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),encrypt+""))
                .enqueue(new XCallback<ResultBean>() {
                    @Override
                    public void onLoadSuccess(Call<ResultBean> call, ResultBean result) {
                        String decrypt = AESUtil.decrypt(result.result, ConfigKeys.AES_KEY);
                        Log.e(TAG, "onLoadSuccess: " + decrypt );
                        SPUtils.getInstance(context).put(ConfigKeys.SPLASH_JSON,decrypt);
                        AdvetisingInitBean advetisingInitBean = gson.fromJson(decrypt, AdvetisingInitBean.class);
                        List<AdvetisingInitBean.SdkAppIdVOListBean> sdkAppIdVOList = advetisingInitBean.getSdkAppIdVOList();
                        for (int i = 0; i < sdkAppIdVOList.size(); i++) {
                            AdvetisingInitBean.SdkAppIdVOListBean sdkAppIdVOListBean = sdkAppIdVOList.get(i);
                            if ("csj".equals(sdkAppIdVOListBean.getAdvertiserNo())){
                                if (sdkAppIdVOListBean.getAppIdList() != null && sdkAppIdVOListBean.getAppIdList().size() > 0){
                                    TTAdManagerHolder.init(context,sdkAppIdVOListBean.getAppIdList().get(0));
                                }
                            }else if ("ylh".equals(sdkAppIdVOListBean.getAdvertiserNo())){
                                if (sdkAppIdVOListBean.getAppIdList() != null && sdkAppIdVOListBean.getAppIdList().size() > 0){
                                    GDTADManager.getInstance().initWith(context, sdkAppIdVOListBean.getAppIdList().get(0));

                                }
                            }
                        }
                        SplashSingleton.getInstance().setAdvetisingInitBean(advetisingInitBean);
                        SplashSingleton.getInstance().setInit(true);
                        SPUtils.getInstance(context).put(ConfigKeys.SPLASH_TIME,DateUtils.getTypeTime());
                    }
                    @Override
                    public void onLoadError(String errorMsg) {
                        Log.e(TAG, "onLoadError: " + errorMsg );

                    }
                });
    }



    //获取直客广告
    public static void getMaterial(String appid, String posid, final MeterialInterfaces meterialInterfaces){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("appMediaId",appid + "");
        jsonObject.addProperty("adSpaceId",posid + "");
        jsonObject.addProperty("platform","ANDROID");
        String encrypt = AESUtil.encrypt(jsonObject.toString(), ConfigKeys.AES_KEY);
        ApiManager.getInstance().create(AuthApi.class).getMeterial(
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),encrypt+""))
                .enqueue(new XCallback<ResultBean>() {
                    @Override
                    public void onLoadSuccess(Call<ResultBean> call, ResultBean result) {
                        String decrypt = AESUtil.decrypt(result.result, ConfigKeys.AES_KEY);
                        if (TextUtils.isEmpty(decrypt)){
                            meterialInterfaces.onError("数据为空");
                        }else{
                            List<MeterialBean> list = gson.fromJson(decrypt, new TypeToken<List<MeterialBean>>() {
                            }.getType());
                            meterialInterfaces.meterialList(list);
                        }

                    }

                    @Override
                    public void onLoadError(String errorMsg) {
                        Log.e(TAG, "onLoadError1: " + errorMsg );
                        meterialInterfaces.onError(errorMsg);
                    }
                });
    }


    //广告成功失败记录

    /**
     *
     * @param adPlatformId  appid
     * @param adSpaceId  广告位id
     * @param resMsg  错误信息
     * @param resType  错误码
     * @param status  成功失败状态码 0、成功，1、失败，2、未果
     */
    public static void splashSave(String adPlatformId,String adSpaceId,String resMsg,String resType,String status){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("adPlatformId",adPlatformId + "");
        jsonObject.addProperty("adPlatformName",  "");
        jsonObject.addProperty("adSpaceId",adSpaceId + "");
        jsonObject.addProperty("adSpaceName", "");
        jsonObject.addProperty("logTime",System.currentTimeMillis()+"");
        jsonObject.addProperty("mediaId",SplashSingleton.getInstance().packageName + "");
        jsonObject.addProperty("mediaName", "");
        jsonObject.addProperty("resMsg",resMsg + "");
        jsonObject.addProperty("resType",resType + "");
        jsonObject.addProperty("status",status + "");
        jsonObject.addProperty("platform","ANDROID");
        Log.e(TAG, "splashSave: " + jsonObject.toString() );
        String encrypt = AESUtil.encrypt(jsonObject.toString(), ConfigKeys.AES_KEY);
        ApiManager.getInstance().create(AuthApi.class).logSave(
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),encrypt+""))
                .enqueue(new XCallback<ResultBean>() {
                    @Override
                    public void onLoadSuccess(Call<ResultBean> call, ResultBean result) {

                    }

                    @Override
                    public void onLoadError(String errorMsg) {
                    }
                });
    }





}
