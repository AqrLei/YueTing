package com.leilei.guoshujinfu.mylearning.presenter

import android.util.Log
import com.leilei.guoshujinfu.mylearning.activity.PictureActivity
import com.leilei.guoshujinfu.mylearning.model.req.PictureReqBean
import com.leilei.guoshujinfu.mylearning.model.resp.BannerBean
import com.leilei.guoshujinfu.mylearning.model.resp.BaseRespBean
import com.leilei.guoshujinfu.mylearning.model.resp.PictureRespBean
import com.leilei.guoshujinfu.mylearning.net.HttpReqHelper
import com.leilei.guoshujinfu.mylearning.net.service.PictureInfoService
import com.leilei.guoshujinfu.mylearning.util.BaseActivityPresenter

import retrofit2.Response
import rx.Observer
import rx.schedulers.Schedulers
//import java.util.*

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/7
 */

class PicturePresenter(mvpActivity: PictureActivity) : BaseActivityPresenter<PictureActivity>(mvpActivity) {
    /*fun getImg(type: String) {
        *//*创建请求参数*//*
        val pictureReqBean = PictureReqBean()
        pictureReqBean.type = type
        *//*Subscription subscription(RxJava, RxAndroid)*//*
        val subscription = HttpReqHelper.getHttpHelper()
                *//*根据service接口*//*
                .creatService(PictureInfoService::class.java)
                *//*执行的方法*//*
                .getPicture(pictureReqBean)
                *//*调用之后回到Android的UI线程（RxAndroid)*//*
                .observeOn(AndroidSchedulers.mainThread())
                *//*调用之前启动新线程*//*
                .subscribeOn(Schedulers.io())
                *//*订阅（执行回调方法）*//*
                .subscribe(object : Observer<Response<BaseRespBean<BannerBean>>> {
                    override fun onCompleted() {
                        Log.d("Amoryan", "onCompleted")
                    }

                    override fun onError(e: Throwable) {
                        Log.d("Amoryan", "onError")
                        e.printStackTrace()
                    }

                    override fun onNext(baseRespBeanResponse: Response<BaseRespBean<BannerBean>>?) {
                        Log.d("Amoryan", "onNext")
                        if (baseRespBeanResponse != null) {
                            val data = ArrayList<PictureRespBean>()
                            data.addAll(baseRespBeanResponse.body().data.bannerConfig)
                            if (data.size > 0) {

                                *//*  mPictureRespBeanList.addAll(data);
                                initViews();*//*
                                *//*
                                * 将获取到的数据用键值对的形式存入cache
                                * AppCache.getAppCache().putString(key,defaultvalue).commit();
                                * 之后需要数据亦可以从cache中取
                                *
                                * *//*

                                mMvpActivity.initViews(data)
                            }
                        }
                    }
                })
        subscriptions.add(subscription)

    }*/
}
