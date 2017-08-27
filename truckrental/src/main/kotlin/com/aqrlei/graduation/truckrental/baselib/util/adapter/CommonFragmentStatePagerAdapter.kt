package com.aqrlei.graduation.truckrental.baselib.util.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/8/26.
 */
class CommonFragmentStatePagerAdapter<out T : Fragment>(fm: FragmentManager,
                                                        private val mFragments: ArrayList<T>?) :
        FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int) = mFragments?.get(position)

    override fun getCount() = mFragments?.size ?: 0
}