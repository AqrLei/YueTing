package com.aqrlei.graduation.truckrental.ui.fragment

import com.aqrlei.graduation.truckrental.R
import com.aqrlei.graduation.truckrental.baselib.mvp.MvpContract
import com.aqrlei.graduation.truckrental.presenter.fragmentpresenter.TabHomePresenter
import com.aqrlei.graduation.truckrental.ui.MainActivity

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/23
 */
class TabHomeFragment: MvpContract.MvpFragment<TabHomePresenter, MainActivity>() {
    override val mPresenter: TabHomePresenter
        get() = TabHomePresenter(this)
    override val layoutRes: Int
        get() = R.layout.activity_main


}