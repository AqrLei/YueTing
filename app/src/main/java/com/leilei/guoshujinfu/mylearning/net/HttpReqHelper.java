package com.leilei.guoshujinfu.mylearning.net;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.leilei.guoshujinfu.mylearning.net.config.HttpReqConfig;

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
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(HttpReqConfig.HTTP_CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(HttpReqConfig.HTTP_READ_TIME_OUT, TimeUnit.SECONDS)
/*                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type", "appliation/json")
                                .build();
                        return chain.proceed(request);
                    }
                })*/
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(interceptor)
                .build();
                //.addInterceptor()
        mRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(HttpReqConfig.HTTP_BASE_URL)
                .client(client)
                .build();

    }
    public static HttpReqHelper getHttpHelper() {
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

