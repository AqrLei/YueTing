package com.aqrlei.graduation.yueting.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/28
 */
class TestFragmentPagerAdapter(fm: FragmentManager,
                               private val mFragments: ArrayList<Fragment>,
                               private val mTabs: ArrayList<String> ): FragmentStatePagerAdapter(fm){
    override fun getItem(position: Int)= mFragments[position]

    override fun getCount()= mFragments.size
    override fun getPageTitle(position: Int)= mTabs[position]

}