package com.aqrlei.graduation.yueting.ui.fragment

import android.os.Bundle
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.baselib.mvp.MvpContract
import com.aqrlei.graduation.yueting.presenter.fragmentpresenter.TabMusicPresenter
import com.aqrlei.graduation.yueting.ui.YueTingActivity

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