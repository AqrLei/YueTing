package com.aqrlei.graduation.yueting.presenter.activitypresenter


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrlei.graduation.yueting.constant.SendType
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.infotool.ShareMusicInfo
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
    fun initFragments(savedInstanceState: Bundle?, fragmentManager: FragmentManager) {
        val mTabHomeFragment: TabHomeFragment
        val mTabMusicFragment: TabMusicFragment
        val mTabReadFragment: TabReadFragment
        val mFragments = ArrayList<Fragment>()

        if (savedInstanceState != null) {
            mTabHomeFragment = (fragmentManager
                    .findFragmentByTag(YueTingConstant.TAB_FRAGMENT_TAGS[YueTingConstant.TAG_FRAGMENT_HOME])
                    ?: TabHomeFragment.newInstance()) as TabHomeFragment

            mTabReadFragment = (fragmentManager
                    .findFragmentByTag(YueTingConstant.TAB_FRAGMENT_TAGS[YueTingConstant.TAG_FRAGMENT_READ])
                    ?: TabReadFragment.newInstance()) as TabReadFragment
            mTabMusicFragment = (fragmentManager
                    .findFragmentByTag(YueTingConstant.TAB_FRAGMENT_TAGS[YueTingConstant.TAG_FRAGMENT_MUSIC])
                    ?: TabMusicFragment.newInstance()) as TabMusicFragment
        } else {
            mTabHomeFragment = TabHomeFragment.newInstance()
            mTabReadFragment = TabReadFragment.newInstance()
            mTabMusicFragment = TabMusicFragment.newInstance()
        }
        mFragments.add(mTabHomeFragment)
        mFragments.add(mTabReadFragment)
        mFragments.add(mTabMusicFragment)
        mMvpActivity.setFragments(mFragments)
    }
}