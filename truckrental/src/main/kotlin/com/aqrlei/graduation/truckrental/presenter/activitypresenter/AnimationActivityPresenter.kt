package com.aqrlei.graduation.truckrental.presenter.activitypresenter


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

import com.aqrlei.graduation.truckrental.R
import com.aqrlei.graduation.truckrental.baselib.mvp.MvpContract
import com.aqrlei.graduation.truckrental.baselib.util.AppConstant
import com.aqrlei.graduation.truckrental.baselib.util.AppLog
import com.aqrlei.graduation.truckrental.ui.AnimationActivity
import com.aqrlei.graduation.truckrental.ui.fragment.TabAnimFragment
import com.aqrlei.graduation.truckrental.ui.fragment.TabChatFragment
import com.aqrlei.graduation.truckrental.ui.fragment.TabHomeFragment

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/23
 */
/*
* @param mMvpActivity 访问对应的Activity
* */
class AnimationActivityPresenter(mMvpActivity: AnimationActivity) :
        MvpContract.ActivityPresenter<AnimationActivity>(mMvpActivity) {

    private var currentTab: Int = 0

    companion object {
        private lateinit var mFragmentManager: FragmentManager
        private lateinit var mTabHomeFragment: TabHomeFragment
        private lateinit var mTabChatFragment: TabChatFragment
        private lateinit var mTabAnimFragment: TabAnimFragment
        private var mFragments = ArrayList<Fragment>()
    }

    fun initFragments(savedInstanceState: Bundle?, fragmentManager: FragmentManager) {
        AppLog.logDebug(AppLog.LOG_TAG_PRESENTER, "initFragments")
        mFragments = ArrayList()
        mFragmentManager = fragmentManager
        if (savedInstanceState != null) {
            mTabHomeFragment = (mFragmentManager
                    .findFragmentByTag(AppConstant.TAB_FRAGMENT_TAGS[AppConstant.TAG_FRAGMENT_HOME])
                    ?: TabHomeFragment.newInstance()) as TabHomeFragment

            mTabAnimFragment = (mFragmentManager
                    .findFragmentByTag(AppConstant.TAB_FRAGMENT_TAGS[AppConstant.TAG_FRAGMENT_ANIM])
                    ?: TabHomeFragment.newInstance()) as TabAnimFragment
            mTabChatFragment = (mFragmentManager
                    .findFragmentByTag(AppConstant.TAB_FRAGMENT_TAGS[AppConstant.TAG_FRAGMENT_CHAT])
                    ?: TabHomeFragment.newInstance()) as TabChatFragment
        } else {
            AppLog.logDebug(AppLog.LOG_TAG_PRESENTER, "initFragments-null")
            mTabHomeFragment = TabHomeFragment.newInstance()
            mTabAnimFragment = TabAnimFragment.newInstance()
            mTabChatFragment = TabChatFragment.newInstance()
        }
        mFragments.add(mTabHomeFragment)
        mFragments.add(mTabAnimFragment)
        mFragments.add(mTabChatFragment)
    }

    fun changeFragment(position: Int) {
        currentTab = position
        for (i in mFragments.indices) {

            val currentFragment = mFragments[i]
            val ft = mFragmentManager.beginTransaction()
            if (i == currentTab) {
                if (!currentFragment.isAdded) {
                    ft.add(R.id.fl_fragment, currentFragment, AppConstant.TAB_FRAGMENT_TAGS[i])
                }
                ft.show(currentFragment)
            } else {
                ft.hide(currentFragment)
            }
            ft.commit()
        }
    }

    fun finish() {
        mFragments.clear()
    }
}