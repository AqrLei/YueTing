package com.aqrlei.graduation.truckrental.baselib.util.net;

import com.aqrlei.graduation.truckrental.baselib.util.net.config.HttpReqConfig;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:HTTP请求类
 * @Date: 2017/8/4
 */

public class HttpReqHelper {
    private Retrofit mRetrofit;

    private HttpReqHelper(){
        /*
        * 日志打印
        * 筛选BODY的信息
        * */
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                /*连接超时时间*/
                .connectTimeout(HttpReqConfig.HTTP_CONNECT_TIME_OUT, TimeUnit.SECONDS)
                /*读取超时时间*/
                .readTimeout(HttpReqConfig.HTTP_READ_TIME_OUT, TimeUnit.SECONDS)
                /*facebook调试*/
                .addNetworkInterceptor(new StethoInterceptor())
                /*日志*/
                .addInterceptor(interceptor)
                .build();
        mRetrofit = new Retrofit.Builder()
                /*Observable适配器*/
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                /*json转化器*/
                .addConverterFactory(GsonConverterFactory.create())
                /*服务器地址URL
                * "http://daily.api.guoshujinfu.com/"
                * */
                .baseUrl(HttpReqConfig.HTTP_BASE_URL)
                /*OkHttpClient*/
                .client(client)
                .build();

    }
    public static HttpReqHelper getHttpHelper() {
        /*返回一个实例*/
        return  HttpReqHolder.mHelper;
    }
    public   Retrofit getRetrofit(){
        return  mRetrofit;
    }
    public <T> T creatService(Class<T> service) {
        if(mRetrofit == null) {
            throw new RuntimeException("ddd");
        }
        return mRetrofit.create(service);
    }
    public static class HttpReqHolder {
        private static final HttpReqHelper mHelper = new HttpReqHelper();
    }

}

