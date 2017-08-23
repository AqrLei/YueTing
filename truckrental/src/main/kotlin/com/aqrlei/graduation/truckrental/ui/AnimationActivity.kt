package com.aqrlei.graduation.truckrental.ui

import android.os.Bundle
import com.aqrlei.graduation.truckrental.R
import com.aqrlei.graduation.truckrental.baselib.mvp.MvpContract
import com.aqrlei.graduation.truckrental.presenter.activitypresenter.AnimationPresenter
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/23
 */
class AnimationActivity : MvpContract.MvpActivity<AnimationPresenter>() {
    override val mPresenter: AnimationPresenter
        get() = AnimationPresenter(this)
    override val layoutRes: Int
        get() = R.layout.activity_main

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        iv_anim_test.setOnClickListener({
            tv_spannable_test.text = mPresenter.getSpannableString()
            tv_spannable_test.startAnimation(mPresenter.getTweenAnimation())
            mPresenter.getAnimator(tv_spannable_test).start()
            mPresenter.getFrameAnimation(iv_anim_test).start()
        })
    }
}