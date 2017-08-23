package com.aqrlei.graduation.truckrental.presenter.fragmentpresenter

import com.aqrlei.graduation.truckrental.baselib.mvp.MvpContract
import com.aqrlei.graduation.truckrental.ui.fragment.TabHomeFragment

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/23
 */
class TabHomePresenter(mMvpView : TabHomeFragment): MvpContract.FragmentPresenter<TabHomeFragment>(mMvpView) {
    fun getSomething(): Int {
        return 1
    }
}