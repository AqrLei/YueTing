package com.aqrlei.graduation.truckrental.ui.fragment

import android.os.Bundle
import com.aqrlei.graduation.truckrental.R
import com.aqrlei.graduation.truckrental.baselib.mvp.MvpContract
import com.aqrlei.graduation.truckrental.presenter.fragmentpresenter.TabHomePresenter
import com.aqrlei.graduation.truckrental.ui.AnimationActivity
import kotlinx.android.synthetic.main.activity_main.view.*

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/23
 */
class TabHomeFragment : MvpContract.MvpFragment<TabHomePresenter, AnimationActivity>() {

    override val mPresenter: TabHomePresenter
        get() = TabHomePresenter(this)
    override val layoutRes: Int
        get() = R.layout.activity_main

    companion object {
        fun newInstance(): TabHomeFragment {
            val args = Bundle()
            val fragment = TabHomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initComponents() {
        super.initComponents()

      mView.iv_anim_test.setOnClickListener({
            mView.tv_spannable_test.text = mPresenter.getSpannableString()
            mView.tv_spannable_test.startAnimation(mPresenter.getTweenAnimation(mContainerActivity))
            mPresenter.getAnimator(mContainerActivity, mView.tv_spannable_test).start()
            mPresenter.getFrameAnimation(mView.iv_anim_test).start()
        })
    }


}