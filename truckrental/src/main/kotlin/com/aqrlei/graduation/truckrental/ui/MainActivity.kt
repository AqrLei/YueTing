package com.aqrlei.graduation.truckrental.ui

import com.aqrlei.graduation.truckrental.baselib.mvp.MvpActivity
import com.aqrlei.graduation.truckrental.presenter.activitypresenter.MainPresenter

/**
 * @Author: AqrLei
 *@Name MyLearning
 *@Description:
 *@Date: 2017/8/22
 */

class MainActivity(override val layoutRes: Int) : MvpActivity<MainPresenter>() {
    override fun createPresenter(): MainPresenter {
        return MainPresenter(this)
    }
}