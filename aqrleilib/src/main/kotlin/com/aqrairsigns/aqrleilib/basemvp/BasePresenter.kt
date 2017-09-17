package com.aqrairsigns.aqrleilib.basemvp

import io.reactivex.disposables.CompositeDisposable


/**
 * @Author: AqrLei
 *@Name MyLearning
 *@Description:
 *@Date: 2017/8/22
 */
abstract class BasePresenter {
    private var disposablesList = ArrayList<CompositeDisposable>()
    fun addDisposables(disposable: CompositeDisposable) {
        disposablesList.add(disposable)
    }

    fun finish() {
        disposablesList.forEach {
            it.dispose()
            if (it.isDisposed) {
                it.clear()
            }
        }
    }
}