package com.aqrlei.graduation.truckrental.baselib.mvp;

import com.aqrlei.graduation.truckrental.baselib.base.BaseActivity;
import com.aqrlei.graduation.truckrental.baselib.base.BaseFragment;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/7
 */

public abstract class MvpFragment<T extends MvpFragmentPresenter,
        V extends BaseActivity> extends BaseFragment<V> {
    protected T mPresenter;

    @Override
    protected void initComponents() {
        super.initComponents();
        mPresenter = createPresenter();
    }

    protected abstract T createPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.cancle();
            mPresenter = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.cancle();
            mPresenter = null;
        }
    }


}
