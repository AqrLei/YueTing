package com.aqrlei.graduation.yueting.ui.fragment

import android.os.Bundle
import android.view.View
import com.aqrlei.graduation.yueting.R
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrlei.graduation.yueting.presenter.fragmentpresenter.TabItemReadPresenter
import com.aqrlei.graduation.yueting.ui.YueTingActivity

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/8/26.
 */
class TabItemReadFragment : MvpContract.MvpFragment<TabItemReadPresenter, YueTingActivity>() {
    override val layoutRes: Int
        get() = R.layout.yueting_fragment_read
    override val mPresenter: TabItemReadPresenter
        get() = TabItemReadPresenter(this)

    companion object {
        fun newInstance(): TabItemReadFragment {
            val args = Bundle()
            val fragment = TabItemReadFragment()
            fragment.arguments = args
            return fragment

        }
    }

    override fun initComponents(view: View?, savedInstanceState: Bundle?) {
        super.initComponents(view, savedInstanceState)
    }

}