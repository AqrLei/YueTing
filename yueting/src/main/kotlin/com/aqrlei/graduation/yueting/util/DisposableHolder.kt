package com.aqrlei.graduation.yueting.util

import io.reactivex.disposables.Disposable

/**
 * created by AqrLei at 17:30 on 星期日, 五月 06, 2018
 */
object DisposableHolder {
   private val disposableList: ArrayList<Disposable>
            by lazy { ArrayList<Disposable>() }

    fun addDisposable(disposable: Disposable) {
        disposableList.add(disposable)
    }

    fun dispose() {
        disposableList.filter { !it.isDisposed }
                .forEach { it.dispose() }
    }
}