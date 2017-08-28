package com.aqrlei.graduation.truckrental.ui.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import com.aqrlei.graduation.truckrental.R
import com.aqrlei.graduation.truckrental.baselib.mvp.MvpContract
import com.aqrlei.graduation.truckrental.baselib.util.AppLog
import com.aqrlei.graduation.truckrental.baselib.util.AppToast
import com.aqrlei.graduation.truckrental.presenter.fragmentpresenter.TabHomePresenter
import com.aqrlei.graduation.truckrental.ui.AnimationActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/23
 */
/*
* @param mPresenter 访问Presenter
* @param mView 访问对应的布局
* @param mContainerActivity 访问对应的Activity
* */
class TabHomeFragment : MvpContract.MvpFragment<TabHomePresenter, AnimationActivity>(), TabLayout.OnTabSelectedListener {
    override fun onTabReselected(tab: TabLayout.Tab?) {
        AppToast.toastShow(mContainerActivity, tab?.text.toString(), 1000)
        tv_test_tab.text = tab?.text.toString()
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        //AppToast.toastShow(mContainerActivity, tab?.text.toString(), 1000)
        AppLog.logDebug(AppLog.LOG_TAG_FRAGMENT, "Unselected: \t" + tab?.text.toString())
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        AppToast.toastShow(mContainerActivity, tab?.text.toString(), 1000)
        tv_test_tab.text = tab?.text.toString()
    }

    override val mPresenter: TabHomePresenter
        get() = TabHomePresenter(this)
    override val layoutRes: Int
        get() = R.layout.fragment_home

    companion object {
        fun newInstance(): TabHomeFragment {
            val args = Bundle()
            val fragment = TabHomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initComponents() {
        super.initComponents()
        initTab()

    }

    private fun initTab() {
        val mTabLayout = mView.tl_tab_header
        mTabLayout.addTab(mTabLayout.newTab().setText("推荐"), true)
        mTabLayout.addTab(mTabLayout.newTab().setText("电影"))
        mTabLayout.addTab(mTabLayout.newTab().setText("悦读"))
        mTabLayout.addTab(mTabLayout.newTab().setText("音乐"))
        mTabLayout.addTab(mTabLayout.newTab().setText("技术"))
        mTabLayout.addTab(mTabLayout.newTab().setText("消息"))
        mTabLayout.addTab(mTabLayout.newTab().setText("关注"))
        mTabLayout.addOnTabSelectedListener(this)

    }
}