package com.leilei.guoshujinfu.mylearning.fragment;

import com.leilei.guoshujinfu.mylearning.activity.MainActivity;
import com.leilei.guoshujinfu.mylearning.presenter.TabPicturePresenter;
import com.leilei.guoshujinfu.mylearning.util.MvpFragment;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/7
 */

public class TabPictureFragment extends MvpFragment <TabPicturePresenter, MainActivity> {


    @Override
    protected TabPicturePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return 0;
    }
}
