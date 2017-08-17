package com.leilei.guoshujinfu.mylearning.util;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/7
 */

public class BaseActivityPresenter <T extends MvpActivity> extends BasePresenter {
    protected T mMvpActivity;

    public BaseActivityPresenter(T mvpActivity) {
        super();
        this.mMvpActivity = mvpActivity;
    }
}
