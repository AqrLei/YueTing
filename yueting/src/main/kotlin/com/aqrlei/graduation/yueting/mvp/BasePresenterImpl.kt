package com.aqrlei.graduation.yueting.mvp

import io.reactivex.disposables.Disposable

/**
 * @author  aqrLei on 2018/5/15
 */
open class BasePresenterImpl : BasePresenter {
    private val disposableList: ArrayList<Disposable>
            by lazy {
                ArrayList<Disposable>()
            }

    override fun addDisposable(disposable: Disposable) {
        disposableList.add(disposable)
    }

    override fun finish() {
        disposableList.filter { !it.isDisposed }
                .forEach { it.dispose() }
    }
}