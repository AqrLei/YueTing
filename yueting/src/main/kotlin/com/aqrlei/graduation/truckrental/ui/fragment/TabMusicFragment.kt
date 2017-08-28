package com.aqrlei.graduation.truckrental.ui.fragment

import android.os.Bundle
import com.aqrlei.graduation.truckrental.R
import com.aqrlei.graduation.truckrental.baselib.mvp.MvpContract
import com.aqrlei.graduation.truckrental.presenter.fragmentpresenter.TabMusicPresenter
import com.aqrlei.graduation.truckrental.ui.YueTingActivity

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/24
 */
class TabMusicFragment :MvpContract.MvpFragment<TabMusicPresenter, YueTingActivity>(){
    override val layoutRes: Int
        get() = R.layout.fragment_chat
    override val mPresenter: TabMusicPresenter
        get() = TabMusicPresenter(this)

    companion object {
        fun newInstance(): TabMusicFragment {
            val args = Bundle()
            val fragment = TabMusicFragment()
            fragment.arguments =  args
            return fragment
        }
    }

    override fun initComponents() {
        super.initComponents()

    }
}