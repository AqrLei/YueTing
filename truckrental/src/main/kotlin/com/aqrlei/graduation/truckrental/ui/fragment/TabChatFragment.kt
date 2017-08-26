package com.aqrlei.graduation.truckrental.ui.fragment

import android.os.Bundle
import com.aqrlei.graduation.truckrental.R
import com.aqrlei.graduation.truckrental.baselib.mvp.MvpContract
import com.aqrlei.graduation.truckrental.presenter.fragmentpresenter.TabChatPresenter
import com.aqrlei.graduation.truckrental.ui.AnimationActivity

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/24
 */
class TabChatFragment:MvpContract.MvpFragment<TabChatPresenter, AnimationActivity>(){
    override val layoutRes: Int
        get() = R.layout.fragment_chat
    override val mPresenter: TabChatPresenter
        get() = TabChatPresenter(this)

    companion object {
        fun newInstance(): TabChatFragment {
            val args = Bundle()
            val fragment = TabChatFragment()
            fragment.arguments =  args
            return fragment
        }
    }

    override fun initComponents() {
        super.initComponents()

    }
}