package com.boniu.ad.request;



import com.boniu.ad.bean.ResultBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    //session 是加密后的请求头
//    @POST(ApiHelper.VISITOR_LOGIN)
//    Call<XResult> login(@Body RequestBody params,@Header("session") String session);

    @POST(ApiHelper.ADVERTISEMRNT_LIST)
    Call<ResultBean> getSplash(@Body RequestBody params);


    @POST(ApiHelper.GET_METERIAL)
    Call<ResultBean> getMeterial(@Body RequestBody params);

    @POST(ApiHelper.LOG_SAVE)
    Call<ResultBean> logSave(@Body RequestBody params);
}