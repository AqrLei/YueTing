package com.leilei.guoshujinfu.mylearning.fragment;

import com.leilei.guoshujinfu.mylearning.R;
import com.leilei.guoshujinfu.mylearning.activity.MainActivity;
import com.leilei.guoshujinfu.mylearning.presenter.TabHomePresenter;
import com.leilei.guoshujinfu.mylearning.util.MvpFragment;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/7
 */

public class TabHomeFragment extends MvpFragment<TabHomePresenter, MainActivity> {
    @Override
    protected TabHomePresenter createPresenter() {
        return new TabHomePresenter(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }
}
