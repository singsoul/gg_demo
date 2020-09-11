package com.boniu.ad;

import android.app.Application;
import android.content.Context;

import com.boniu.ad.bean.AdvetisingInitBean;
import com.boniu.ad.config.ConfigKeys;
import com.boniu.ad.utils.SPUtils;
import com.boniu.ad.utils.TextUtils;
import com.google.gson.Gson;
import com.qq.e.comm.managers.GDTADManager;

import java.util.ArrayList;
import java.util.List;

public class SplashSingleton {
    //类加载时就初始化
    private static final SplashSingleton instance = new SplashSingleton();

    private List<AdvetisingInitBean.SdkAdverVOListBean> list;

    private AdvetisingInitBean advetisingInitBean;
    public String packageName;
    private String appName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    private Context context;
    private Gson gson = new Gson();




    private boolean isInit = false;


    private SplashSingleton(){}

    public static SplashSingleton getInstance(){
        return instance;
    }




    //获取各种类型广告列表
    public List<AdvetisingInitBean.SdkAdverVOListBean.ListBean> getSplashList(int type){

        if (advetisingInitBean == null){
            String splashJson = SPUtils.getInstance(context).getString(ConfigKeys.SPLASH_JSON);
            if (TextUtils.isEmpty(splashJson)){
                return  null;
            }
            advetisingInitBean = gson.fromJson(splashJson, AdvetisingInitBean.class);
            list = advetisingInitBean.getSdkAdverVOList();
        }else{
            list = advetisingInitBean.getSdkAdverVOList();
        }

        if (!isInit){
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
            isInit = true;
        }
        if (list != null){
            for (int i = 0; i < list.size(); i++) {
                AdvetisingInitBean.SdkAdverVOListBean advertisingBean = list.get(i);
                if (type == advertisingBean.getAdvertisingSpaceType()){
                    return advertisingBean.getList();
                }
            }
        }

        return null;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public AdvetisingInitBean getAdvetisingInitBean() {
        return advetisingInitBean;
    }

    public void setAdvetisingInitBean(AdvetisingInitBean advetisingInitBean) {
        this.advetisingInitBean = advetisingInitBean;
    }

}
