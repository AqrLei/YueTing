package com.leilei.guoshujinfu.mylearning.fragment;

import android.os.Bundle;

import com.leilei.guoshujinfu.mylearning.R;
import com.leilei.guoshujinfu.mylearning.activity.MainActivity;
import com.leilei.guoshujinfu.mylearning.activity.MessageCenterActivity;
import com.leilei.guoshujinfu.mylearning.presenter.TabHomePresenter;
import com.leilei.guoshujinfu.mylearning.util.MvpFragment;

import butterknife.OnClick;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/7
 */

public class TabHomeFragment extends MvpFragment<TabHomePresenter, MainActivity> {


    public static TabHomeFragment newInstance( ) {
        Bundle args = new Bundle();
        TabHomeFragment fragment = new TabHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected TabHomePresenter createPresenter() {
        return new TabHomePresenter(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.tab_main_home;
    }
    @OnClick(R.id.iv_tab_home)
    public void onClick() {
        MessageCenterActivity.jumpToMessageCenterActivity(TabHomeFragment.this.getContainerActivity());

    }
}
