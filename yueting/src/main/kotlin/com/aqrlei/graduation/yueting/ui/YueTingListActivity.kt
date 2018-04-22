package com.aqrlei.graduation.yueting.ui

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.presenter.activitypresenter.YueTingListPresenter

/**
 * created by AqrLei on 2018/4/22
 */
class YueTingListActivity : MvpContract.MvpActivity<YueTingListPresenter>() {
    override val mPresenter: YueTingListPresenter
        get() = YueTingListPresenter(this)
    override val layoutRes: Int
        get() = R.layout.main_activity_yueting_list
}