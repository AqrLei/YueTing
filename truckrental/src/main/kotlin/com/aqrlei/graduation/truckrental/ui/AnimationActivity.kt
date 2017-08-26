package com.aqrlei.graduation.truckrental.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import com.aqrlei.graduation.truckrental.R
import com.aqrlei.graduation.truckrental.baselib.mvp.MvpContract
import com.aqrlei.graduation.truckrental.baselib.util.IntentUtil
import com.aqrlei.graduation.truckrental.presenter.activitypresenter.AnimationActivityPresenter
import com.aqrlei.graduation.truckrental.ui.fragment.TabHomeFragment

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/23
 */
/*
* @param mPresenter 访问对应的Presenter
* */
class AnimationActivity : MvpContract.MvpActivity<AnimationActivityPresenter>() {
    private lateinit var mFragmentManager: FragmentManager
    private var mTabHomeFragment: TabHomeFragment? = null
    private val TAB_HOME_TAG: String = "tab_home"

    override val mPresenter: AnimationActivityPresenter
        get() = AnimationActivityPresenter(this)
    override val layoutRes: Int
        get() = R.layout.activity_animation

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        mFragmentManager = supportFragmentManager
        initFragments(savedInstanceState)

    }

    private fun initFragments(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mTabHomeFragment = mFragmentManager.findFragmentByTag(TAB_HOME_TAG) as TabHomeFragment
            mTabHomeFragment = if (mTabHomeFragment == null)
                TabHomeFragment.newInstance()
            else
                mTabHomeFragment
        } else {
            mTabHomeFragment = TabHomeFragment.newInstance()
        }
        val ft = mFragmentManager.beginTransaction()
        ft.add(R.id.fl_fragment, mTabHomeFragment, TAB_HOME_TAG)
        ft.show(mTabHomeFragment)
        ft.commit()

    }

    companion object {
        fun jumpToAnimationActivity(context: Context, data: Int) {
            val intent = Intent(context, AnimationActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("code", data)
            if (IntentUtil.queryActivities(context, intent)) context.startActivity(intent)
        }
    }
}