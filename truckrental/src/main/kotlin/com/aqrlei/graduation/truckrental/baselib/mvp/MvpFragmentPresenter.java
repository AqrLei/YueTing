package com.aqrlei.graduation.truckrental.baselib.mvp;

import com.aqrlei.graduation.truckrental.baselib.base.BasePresenter;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/7
 */

public class MvpFragmentPresenter<T extends MvpFragment> extends BasePresenter {
    protected T mMvpView;

    public MvpFragmentPresenter(T mvpView) {
        super();
        this.mMvpView = mvpView;
    }
}
