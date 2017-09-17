package com.aqrlei.graduation.yueting.presenter.activitypresenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrlei.graduation.yueting.ui.MainActivity


/**
 * @Author: AqrLei
 *@Name MyLearning
 *@Description:
 *@Date: 2017/8/22
 */
class MainActivityPresenter(mMvpActivity: MainActivity) : MvpContract.ActivityPresenter<MainActivity>(mMvpActivity) {

    /* fun getImg(type: String) {
         Log.d("Leilei", "getImg")
         val pictureReqBean = PictureReqBean()
         pictureReqBean.type = type
         val subscription = HttpReqHelper.httpHelper
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
                             data.addAll(t.body().data.bannerConfig)
                             if (data.size > 0) {

                                 mMvpActivity.initViews(data)
                             }
                         }
                     }
                 })
         subscriptions.add(subscription)

     }*/
}

//private fun <T> Observable<T>.subscribe(observer: Observer<Response<BaseRespBean<BannerBean>>>) {}




