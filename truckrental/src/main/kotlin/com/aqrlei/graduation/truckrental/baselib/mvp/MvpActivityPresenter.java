package com.aqrlei.graduation.truckrental.baselib.mvp;

import com.aqrlei.graduation.truckrental.baselib.base.BasePresenter;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/7
 */

public class MvpActivityPresenter<T extends MvpActivity> extends BasePresenter {
    protected T mMvpActivity;

    public MvpActivityPresenter(T mvpActivity) {
        super();
        this.mMvpActivity = mvpActivity;
    }
}
