package com.aqrlei.graduation.yueting.ui.fragment

import android.os.Bundle
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.baselib.mvp.MvpContract
import com.aqrlei.graduation.yueting.presenter.fragmentpresenter.TabReadPresenter
import com.aqrlei.graduation.yueting.ui.YueTingActivity
import kotlinx.android.synthetic.main.fragment_anim.view.*

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/8/26.
 */
class TabReadFragment : MvpContract.MvpFragment<TabReadPresenter, YueTingActivity>() {
    override val layoutRes: Int
        get() = R.layout.fragment_anim
    override val mPresenter: TabReadPresenter
        get() = TabReadPresenter(this)

    companion object {
        fun newInstance(): TabReadFragment {
            val args = Bundle()
            val fragment = TabReadFragment()
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