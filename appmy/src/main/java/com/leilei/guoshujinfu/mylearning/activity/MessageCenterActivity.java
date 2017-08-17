package com.leilei.guoshujinfu.mylearning.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.leilei.guoshujinfu.mylearning.R;
import com.leilei.guoshujinfu.mylearning.fragment.TabChatFragment;
import com.leilei.guoshujinfu.mylearning.fragment.TabHomeFragment;
import com.leilei.guoshujinfu.mylearning.fragment.TabPictureFragment;
import com.leilei.guoshujinfu.mylearning.presenter.MessageCenterPresenter;
import com.leilei.guoshujinfu.mylearning.util.AppToast;
import com.leilei.guoshujinfu.mylearning.util.MvpActivity;
import com.leilei.guoshujinfu.mylearning.util.view.viewpager.StateFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/9
 */

public class MessageCenterActivity extends MvpActivity <MessageCenterPresenter> {
    @BindView(R.id.tl_message)
    TabLayout mMessageTab;
    @BindView(R.id.vp_message)
    ViewPager mMessageVP;

    private TabLayout.Tab mHomeTab;
    private TabLayout.Tab mChatTab;
    private TabLayout.Tab mPictureTab;

    private List<Fragment> mFragments;

    public static void jumpToMessageCenterActivity (Context context) {
        Intent intent = new Intent(context, MessageCenterActivity.class);
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if(list != null && list.size() > 0) {
            context.startActivity(intent);

        }
        else {
            AppToast.toastShow(context,"MessageCenterActivity未注册");
        }
    }

    @Override
    protected void initComponents(Bundle savedInstanceState) {
        super.initComponents(savedInstanceState);
        mHomeTab = mMessageTab.newTab().setText("主页");
        mChatTab = mMessageTab.newTab().setText("去聊");
        mPictureTab = mMessageTab.newTab().setText("图片");
        mMessageTab.addTab(mHomeTab);
        mMessageTab.addTab(mChatTab);
        mMessageTab.addTab(mPictureTab);
        mFragments = new ArrayList<>();
        mFragments.add(TabHomeFragment.newInstance());
        mFragments.add(TabChatFragment.newInstance());
        mFragments.add(TabPictureFragment.newInstance());
        mMessageVP.setAdapter(new StateFragmentAdapter<Fragment>(
                getSupportFragmentManager(), mFragments));
        mMessageTab.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mMessageVP));
        mMessageVP.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mMessageTab));
    }

    @Override
    protected MessageCenterPresenter createPresenter() {
        return new MessageCenterPresenter(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_message_center;
    }
}
