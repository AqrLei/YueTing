package com.leilei.guoshujinfu.mylearning.activity;

import android.widget.TextView;

import com.leilei.guoshujinfu.mylearning.R;
import com.leilei.guoshujinfu.mylearning.presenter.TestPresenter;
import com.leilei.guoshujinfu.mylearning.util.MvpActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/8
 */

public class TestActivity extends MvpActivity <TestPresenter> {
    @BindView(R.id.tv_test)
    TextView mTest;
    @Override
    protected TestPresenter createPresenter() {
        return new TestPresenter(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_test;
    }
    @OnClick(R.id.tv_test)
    public  void OnClick(TextView v) {
        v.setEnabled(false);

    }
}
