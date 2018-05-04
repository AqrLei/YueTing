package com.aqrlei.graduation.yueting.presenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrlei.graduation.yueting.model.observable.TypeSingle
import com.aqrlei.graduation.yueting.ui.fragment.TitleFragment

/**
 * created by AqrLei on 2018/4/23
 */
class TitlePresenter(mMvpView: TitleFragment) :
        MvpContract.FragmentPresenter<TitleFragment>(mMvpView) {

    fun modifyTypeName(isNew: Boolean, type: String, name: String, newName: String = "") {
        val disposable = if (isNew) {
            TypeSingle.insertType(type, name)
                    .subscribe({
                        mMvpView.modifyFinish(it,"创建成功")
                    }, {})
        } else {
            TypeSingle.updateType(oldTypeName = name, newTypeName = newName)
                    .subscribe({
                        mMvpView.modifyFinish(it,"修改成功")
                    },{})
        }
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