package com.aqrlei.graduation.yueting.presenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrlei.graduation.yueting.model.observable.TypeSingle
import com.aqrlei.graduation.yueting.ui.fragment.TitleFragment

/**
 * created by AqrLei on 2018/4/23
 */
class TitlePresenter(mMvpView: TitleFragment) :
        MvpContract.FragmentPresenter<TitleFragment>(mMvpView) {

    fun addData(type: String, name: String) {
        val disposable =
                TypeSingle.insertType(type, name)
                        .subscribe({
                            mMvpView.addFinish(it)
                        }, {})
        addDisposables(disposable)
    }

    fun getTypeInfo(type: String) {
        val disposables =
                TypeSingle.selectType(type)
                        .subscribe({
                            mMvpView.setTitleList(it)
                        }, {})
        addDisposables(disposables)
    }
}