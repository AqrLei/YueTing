package com.leilei.guoshujinfu.mylearning.presenter;

import com.leilei.guoshujinfu.mylearning.fragment.TabPictureFragment;
import com.leilei.guoshujinfu.mylearning.model.req.PictureReqBean;
import com.leilei.guoshujinfu.mylearning.model.resp.BannerBean;
import com.leilei.guoshujinfu.mylearning.model.resp.BaseRespBean;
import com.leilei.guoshujinfu.mylearning.model.resp.PictureRespBean;
import com.leilei.guoshujinfu.mylearning.net.HttpReqHelper;
import com.leilei.guoshujinfu.mylearning.net.service.PictureInfoService;
import com.leilei.guoshujinfu.mylearning.util.BaseFragmentPresenter;

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

public class TabPicturePresenter extends BaseFragmentPresenter<TabPictureFragment> {
    public TabPicturePresenter(TabPictureFragment mvpView) {
        super(mvpView);
    }
    public void getImg(String type) {
        PictureReqBean pictureReqBean = new PictureReqBean();
        pictureReqBean.setType(type);
        Subscription subscription = HttpReqHelper.getHttpHelper()
                .creatService(PictureInfoService.class)
                .getPicture(pictureReqBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<BaseRespBean<BannerBean>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(Response<BaseRespBean<BannerBean>> baseRespBeanResponse) {
                        if(baseRespBeanResponse != null) {
                            List<PictureRespBean> data = new ArrayList<PictureRespBean>();
                            data.addAll(baseRespBeanResponse.body().getData().getBannerConfig());
                            if(data.size() > 0) {
                                mMvpView.initViews(data);
                            }
                        }

                    }
                });
        subscriptions.add(subscription);
    }



}
