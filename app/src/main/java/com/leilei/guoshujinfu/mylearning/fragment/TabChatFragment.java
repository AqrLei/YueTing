package com.leilei.guoshujinfu.mylearning.fragment;

import com.leilei.guoshujinfu.mylearning.R;
import com.leilei.guoshujinfu.mylearning.activity.MainActivity;
import com.leilei.guoshujinfu.mylearning.presenter.TabChatPresenter;
import com.leilei.guoshujinfu.mylearning.util.MvpFragment;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/7
 */

public class TabChatFragment extends MvpFragment <TabChatPresenter, MainActivity> {
    @Override
    protected TabChatPresenter createPresenter() {
        return new TabChatPresenter(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_chat;
    }
}
