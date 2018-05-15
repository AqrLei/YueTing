package com.aqrlei.graduation.yueting.mvp

import io.reactivex.disposables.Disposable

/**
 * @author  aqrLei on 2018/5/15
 */
interface BasePresenter {
    fun addDisposable(disposable: Disposable)
    fun finish()
}