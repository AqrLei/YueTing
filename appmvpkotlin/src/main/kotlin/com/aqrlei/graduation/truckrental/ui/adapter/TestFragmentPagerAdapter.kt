package com.aqrlei.graduation.truckrental.ui.adapter

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
                               private val mFragmens: ArrayList<Fragment>,
                               private val mTabs: ArrayList<String> ): FragmentStatePagerAdapter(fm){
    override fun getItem(position: Int)= mFragmens[position]

    override fun getCount()= mFragmens.size
    override fun getPageTitle(position: Int)= mTabs[position]

}