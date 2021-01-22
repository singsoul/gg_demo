package com.boniu.ad.request;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class ApiManager {
    private static final int READ_TIME_OUT = 30;

    private static final int CONNECT_TIME_OUT = 30;
    private Retrofit mRetrofit;


    //构造方法私有化，目的是为了只创建一个实例，并且在这里设置打印log和
    private ApiManager() {
        build(ApiHelper.BASE_URI);
    }

    private void build(String url) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(new HttpInterceptor())
                .build();
        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(AResultConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
    }

    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return mRetrofit.create(service);
    }

    /**
     * 单例对象持有者
     */
    private static class SingletonHolder {
        private static final ApiManager INSTANCE = new ApiManager();
    }

    public static ApiManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private class HttpInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            Request request = builder.addHeader("Content-type", "application/json; charset=utf-8").build();
            return chain.proceed(request);
        }
    }
}
