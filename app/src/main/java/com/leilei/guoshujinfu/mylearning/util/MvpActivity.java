package com.leilei.guoshujinfu.mylearning.util;

import android.os.Bundle;

import rx.Subscription;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/7
 */

public abstract class MvpActivity <T extends BaseActivityPresenter> extends BaseActivity {
    protected T mPresenter;

    @Override
    protected void initComponents(Bundle savedInstanceState) {
        super.initComponents(savedInstanceState);
        mPresenter = createPresenter();

    }
    protected abstract T createPresenter();
    public void addSubscription(Subscription subscription) {
        mPresenter.addSubscription(subscription);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter != null) {
            mPresenter.cancle();
            mPresenter = null;
        }
    }
}
