package com.leilei.guoshujinfu.mylearning.tool.presenter;

import android.util.Log;

import com.leilei.guoshujinfu.mylearning.activity.PictureActivity;
import com.leilei.guoshujinfu.mylearning.model.req.PictureReqBean;
import com.leilei.guoshujinfu.mylearning.model.resp.BannerBean;
import com.leilei.guoshujinfu.mylearning.model.resp.BaseRespBean;
import com.leilei.guoshujinfu.mylearning.model.resp.PictureRespBean;
import com.leilei.guoshujinfu.mylearning.net.HttpReqHelper;
import com.leilei.guoshujinfu.mylearning.net.service.PictureInfoService;
import com.leilei.guoshujinfu.mylearning.util.BaseActivityPresenter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/7
 */

public class PicturePresenter extends BaseActivityPresenter<PictureActivity> {
    public PicturePresenter(PictureActivity mvpActivity) {
        super(mvpActivity);
    }
    public void getImg(String type){
         /*创建请求参数*/
        PictureReqBean pictureReqBean = new PictureReqBean();
        pictureReqBean.setType(type);
        /*Subscription subscription(RxJava, RxAndroid)*/
        Subscription subscription = HttpReqHelper.getHttpHelper()
                /*根据service接口*/
                .creatService(PictureInfoService.class)
                /*执行的方法*/
                .getPicture(pictureReqBean)
                /*调用之后回到Android的UI线程（RxAndroid)*/
                .observeOn(AndroidSchedulers.mainThread())
                /*调用之前启动新线程*/
                .subscribeOn(Schedulers.io())
                /*订阅（执行回调方法）*/
                .subscribe(new Observer<Response<BaseRespBean<BannerBean>>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("Amoryan", "onCompleted");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d("Amoryan", "onError");
                        e.printStackTrace();
                    }
                    @Override
                    public void onNext(Response<BaseRespBean<BannerBean>> baseRespBeanResponse) {
                        Log.d("Amoryan", "onNext");
                        if(baseRespBeanResponse != null) {
                            List<PictureRespBean> data = new ArrayList<PictureRespBean>();
                            data.addAll(baseRespBeanResponse.body().getData().getBannerConfig());
                            if(data.size() > 0) {
                              /*  mPictureRespBeanList.addAll(data);
                                initViews();*/
                              mMvpActivity.initViews(data);
                            }
                        }
                    }
                });
        subscriptions.add(subscription);

    }
}
