package com.aqrlei.graduation.truckrental.ui

import android.os.Bundle
import android.support.v4.app.FragmentManager
import com.aqrlei.graduation.truckrental.R
import com.aqrlei.graduation.truckrental.baselib.mvp.MvpContract
import com.aqrlei.graduation.truckrental.presenter.activitypresenter.AnimationActivityPresenter
import com.aqrlei.graduation.truckrental.ui.fragment.TabHomeFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/23
 */
class AnimationActivity : MvpContract.MvpActivity<AnimationActivityPresenter>() {
    private var mFragmentManager: FragmentManager? = null
    private var mTabHomeFragment: TabHomeFragment? = null

    override val mPresenter: AnimationActivityPresenter
        get() = AnimationActivityPresenter(this)
    override val layoutRes: Int
        get() = R.layout.activity_main

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        mFragmentManager = supportFragmentManager

        iv_anim_test.setOnClickListener({
            tv_spannable_test.text = mPresenter.getSpannableString()
            tv_spannable_test.startAnimation(mPresenter.getTweenAnimation())
            mPresenter.getAnimator(tv_spannable_test).start()
            mPresenter.getFrameAnimation(iv_anim_test).start()
        })
    }
    private fun initFragments(savedInstanceState: Bundle?) {
        if(savedInstanceState != null) {
            mTabHomeFragment = mTabHomeFragment
        }
    }
}