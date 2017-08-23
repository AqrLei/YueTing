/*
package com.aqrlei.graduation.truckrental.baselib.mvp;

import android.os.Bundle;

import com.aqrlei.graduation.truckrental.baselib.base.BaseActivity;

*/
/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/7
 *//*


*/
/**//*

public abstract class MvpActivity<T extends MvpActivityPresenter> extends BaseActivity {
    protected T mPresenter;

    @Override
    protected void initComponents(Bundle savedInstanceState) {
        super.initComponents(savedInstanceState);
        mPresenter = createPresenter();

    }

    protected abstract T createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.cancle();
            mPresenter = null;
        }
    }
}
*/
