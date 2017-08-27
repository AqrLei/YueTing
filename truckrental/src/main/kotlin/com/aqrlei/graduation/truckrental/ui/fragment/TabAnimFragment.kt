package com.aqrlei.graduation.truckrental.ui.fragment

import android.os.Bundle
import com.aqrlei.graduation.truckrental.R
import com.aqrlei.graduation.truckrental.baselib.mvp.MvpContract
import com.aqrlei.graduation.truckrental.presenter.fragmentpresenter.TabAnimPresenter
import com.aqrlei.graduation.truckrental.ui.AnimationActivity
import kotlinx.android.synthetic.main.fragment_anim.view.*

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/8/26.
 */
class TabAnimFragment : MvpContract.MvpFragment<TabAnimPresenter, AnimationActivity>() {
    override val layoutRes: Int
        get() = R.layout.fragment_anim
    override val mPresenter: TabAnimPresenter
        get() = TabAnimPresenter(this)

    companion object {
        fun newInstance(): TabAnimFragment {
            val args = Bundle()
            val fragment = TabAnimFragment()
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