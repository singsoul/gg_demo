package com.boniu.ad.request;


import com.boniu.ad.bean.ResultBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class XCallback<T extends ResultBean> implements Callback<T> {

    private static final String TAG = XCallback.class.getSimpleName();

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        try {
            T result = response.body();
            if (result == null) {
                onLoadError(result.errorMsg);
                return;
            }
            if (result.isSuccess()) { // 和服务器交互成功
                if ("0".equals(result.returnCode)){
                    onLoadSuccess(call,result);
                }else{
                    onLoadError(result.errorMsg);
                }
            } else { // 和服务器交互失败
                onLoadError(result.errorMsg);
            }
        } catch (Exception e) {
            onLoadError("服务器异常");
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onLoadError(t.getMessage());
    }

    public abstract void onLoadSuccess(Call<T> call, T result);
    public abstract void onLoadError(String errorMsg);
}
