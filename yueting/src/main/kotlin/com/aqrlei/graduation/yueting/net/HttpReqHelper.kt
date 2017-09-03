package com.aqrlei.graduation.yueting.net

import com.aqrlei.graduation.yueting.net.config.HttpReqConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:HTTP请求类
 * @Date: 2017/8/4
 */

class HttpReqHelper private constructor() {
    private val mRetrofit: Retrofit?

    init {
        /*
        * 日志打印
        * 筛选BODY的信息
        * */
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
                /*连接超时时间*/
                .connectTimeout(HttpReqConfig.HTTP_CONNECT_TIME_OUT, TimeUnit.SECONDS)
                /*读取超时时间*/
                .readTimeout(HttpReqConfig.HTTP_READ_TIME_OUT, TimeUnit.SECONDS)
                /*facebook调试*/
                .addNetworkInterceptor(StethoInterceptor())
                /*日志*/
                .addInterceptor(interceptor)
                .build()
        mRetrofit = Retrofit.Builder()
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
                .build()

    }

    fun <T> creatService(service: Class<T>): T {
        if (mRetrofit == null) {
            throw RuntimeException("ddd")
        }
        return mRetrofit.create(service)
    }

    private object HttpReqHolder {
         val mHelper = HttpReqHelper()
    }

    companion object {
        /*返回一个实例*/ val httpHelper: HttpReqHelper
            get() = HttpReqHolder.mHelper
    }

}

