package com.aqrlei.graduation.yueting.presenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.observable.BookSingle
import com.aqrlei.graduation.yueting.model.observable.MusicSingle
import com.aqrlei.graduation.yueting.model.observable.TypeSingle
import com.aqrlei.graduation.yueting.ui.fragment.TitleFragment
import io.reactivex.Single

/**
 * created by AqrLei on 2018/4/23
 */
class TitlePresenter(mMvpView: TitleFragment) :
        MvpContract.FragmentPresenter<TitleFragment>(mMvpView) {

    fun modifyTypeName(isNew: Boolean, type: String, name: String, newName: String = "") {
        val disposable = if (isNew) {
            TypeSingle.insertType(type, name)
                    .subscribe({
                        mMvpView.modifyFinish(it, if (it) "创建成功" else "创建失败")
                    }, {})
        } else {
            TypeSingle.updateType(oldTypeName = name, newTypeName = newName)
                    .flatMap {
                        if (it) {
                            if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_MUSIC)
                                MusicSingle.updateTypeNameList(oldTypeName = name, newTypeName = newName)
                            else
                                BookSingle.updateTypeNameList(oldTypeName = name, newTypeName = newName)
                        } else {
                            Single.just(it)
                        }
                    }
                    .subscribe({
                        mMvpView.modifyFinish(it, if (it) "修改成功" else "修改失败")
                    }, {})
        }
        addDisposables(disposable)
    }

    fun fetchTypeInfo(type: String) {
        val disposables =
                TypeSingle.selectType(type)
                        .subscribe({
                            mMvpView.setTitleList(it)
                        }, {})
        addDisposables(disposables)
    }
}