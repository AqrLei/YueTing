package com.aqrlei.graduation.truckrental.presenter.activitypresenter


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.aqrlei.graduation.truckrental.R
import com.aqrlei.graduation.truckrental.baselib.mvp.MvpContract
import com.aqrlei.graduation.truckrental.baselib.util.AppConstant
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
    fun initFragments(savedInstanceState: Bundle?, fragmentManager: FragmentManager): ArrayList<Fragment> {
        val mTabHomeFragment: TabHomeFragment
        val mTabChatFragment: TabChatFragment
        val mTabAnimFragment: TabAnimFragment
        val mFragments = ArrayList<Fragment>()

        if (savedInstanceState != null) {
            mTabHomeFragment = (fragmentManager
                    .findFragmentByTag(AppConstant.TAB_FRAGMENT_TAGS[AppConstant.TAG_FRAGMENT_HOME])
                    ?: TabHomeFragment.newInstance()) as TabHomeFragment

            mTabAnimFragment = (fragmentManager
                    .findFragmentByTag(AppConstant.TAB_FRAGMENT_TAGS[AppConstant.TAG_FRAGMENT_ANIM])
                    ?: TabHomeFragment.newInstance()) as TabAnimFragment
            mTabChatFragment = (fragmentManager
                    .findFragmentByTag(AppConstant.TAB_FRAGMENT_TAGS[AppConstant.TAG_FRAGMENT_CHAT])
                    ?: TabHomeFragment.newInstance()) as TabChatFragment
        } else {
            mTabHomeFragment = TabHomeFragment.newInstance()
            mTabAnimFragment = TabAnimFragment.newInstance()
            mTabChatFragment = TabChatFragment.newInstance()
        }
        mFragments.add(mTabHomeFragment)
        mFragments.add(mTabAnimFragment)
        mFragments.add(mTabChatFragment)
        return mFragments
    }

    fun changeFragment(position: Int, fragmentManager: FragmentManager, mFragments: ArrayList<Fragment>) {
        for (i in mFragments.indices) {
            val currentFragment = mFragments[i]
            val ft = fragmentManager.beginTransaction()
            if (i == position) {
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
}