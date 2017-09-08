package com.aqrlei.graduation.yueting.presenter.activitypresenter


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrlei.graduation.yueting.constant.AppConstant
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.ui.YueTingActivity
import com.aqrlei.graduation.yueting.ui.fragment.TabHomeFragment
import com.aqrlei.graduation.yueting.ui.fragment.TabMusicFragment
import com.aqrlei.graduation.yueting.ui.fragment.TabReadFragment

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/23
 */
/*
* @param mMvpActivity 访问对应的Activity
* */
class YueTingActivityPresenter(mMvpActivity: YueTingActivity) :
        MvpContract.ActivityPresenter<YueTingActivity>(mMvpActivity) {
    fun initFragments(savedInstanceState: Bundle?, fragmentManager: FragmentManager): ArrayList<Fragment> {
        val mTabHomeFragment: TabHomeFragment
        val mTabMusicFragment: TabMusicFragment
        val mTabReadFragment: TabReadFragment
        val mFragments = ArrayList<Fragment>()

        if (savedInstanceState != null) {
            mTabHomeFragment = (fragmentManager
                    .findFragmentByTag(AppConstant.TAB_FRAGMENT_TAGS[AppConstant.TAG_FRAGMENT_HOME])
                    ?: TabHomeFragment.newInstance()) as TabHomeFragment

            mTabReadFragment = (fragmentManager
                    .findFragmentByTag(AppConstant.TAB_FRAGMENT_TAGS[AppConstant.TAG_FRAGMENT_READ])
                    ?: TabHomeFragment.newInstance()) as TabReadFragment
            mTabMusicFragment = (fragmentManager
                    .findFragmentByTag(AppConstant.TAB_FRAGMENT_TAGS[AppConstant.TAG_FRAGMENT_MUSIC])
                    ?: TabHomeFragment.newInstance()) as TabMusicFragment
        } else {
            mTabHomeFragment = TabHomeFragment.newInstance()
            mTabReadFragment = TabReadFragment.newInstance()
            mTabMusicFragment = TabMusicFragment.newInstance()
        }
        mFragments.add(mTabHomeFragment)
        mFragments.add(mTabReadFragment)
        mFragments.add(mTabMusicFragment)
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