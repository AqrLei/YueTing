package com.leilei.guoshujinfu.mylearning.util;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/7
 */

public abstract class MvpFragment <T extends BaseFragmentPresenter,
        V extends BaseActivity> extends BaseFragment<V> {
    protected T mPresenter;

    @Override
    protected void initComponents() {
        super.initComponents();
        mPresenter = createPresenter();
    }
    protected abstract T createPresenter();
    /*public void addSubscription(Subscription subscription) {
        mPresenter.addSubscription(subscription);

    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mPresenter != null) {
            mPresenter.cancle();
            mPresenter = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter != null) {
            mPresenter.cancle();
            mPresenter = null;
        }
    }
}
