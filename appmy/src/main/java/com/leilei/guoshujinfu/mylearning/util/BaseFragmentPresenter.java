package com.leilei.guoshujinfu.mylearning.util;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/7
 */

public class BaseFragmentPresenter<T extends MvpFragment> extends BasePresenter {
    protected T mMvpView;

    public BaseFragmentPresenter(T mvpView) {
        super();
        this.mMvpView = mvpView;
    }
}
