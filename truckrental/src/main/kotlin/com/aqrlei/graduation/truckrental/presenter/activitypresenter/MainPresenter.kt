package com.aqrlei.graduation.truckrental.presenter.activitypresenter

import android.util.Log
import com.aqrlei.graduation.truckrental.baselib.mvp.MvpContract
import com.aqrlei.graduation.truckrental.baselib.util.net.HttpReqHelper
import com.aqrlei.graduation.truckrental.baselib.util.net.service.PictureInfoService
import com.aqrlei.graduation.truckrental.model.req.PictureReqBean
import com.aqrlei.graduation.truckrental.model.resp.BannerBean
import com.aqrlei.graduation.truckrental.model.resp.BaseRespBean
import com.aqrlei.graduation.truckrental.model.resp.PictureRespBean
import com.aqrlei.graduation.truckrental.ui.MainActivity
import retrofit2.Response
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * @Author: AqrLei
 *@Name MyLearning
 *@Description:
 *@Date: 2017/8/22
 */
class MainPresenter(mMvpActivity: MainActivity) : MvpContract.Presenter<MainActivity>(mMvpActivity) {

    fun getImg(type: String) {
        Log.d("Leilei", "getImg")
        val pictureReqBean = PictureReqBean()
        pictureReqBean.type = type
        val subscription = HttpReqHelper.getHttpHelper()
                .creatService(PictureInfoService::class.java)
                .getPicture(pictureReqBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object: Observer<Response<BaseRespBean<BannerBean>>> {
                    override fun onCompleted() {

                    }
                    override fun onError(e: Throwable?) {

                    }

                    override fun onNext(t: Response<BaseRespBean<BannerBean>>?) {
                        if (t != null) {
                            val data = ArrayList<PictureRespBean>()
                            data.addAll(t.body().data!!.bannerConfig!!)
                            if (data.size > 0) {

                                mMvpActivity.initViews(data)
                            }
                        }
                    }
                })
        //subscriptions.add(subscription)

    }
}

private fun <T> Observable<T>.subscribe(observer: Observer<Response<BaseRespBean<BannerBean>>>) {}




