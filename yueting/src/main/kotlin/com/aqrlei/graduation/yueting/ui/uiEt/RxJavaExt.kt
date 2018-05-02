package com.aqrlei.graduation.yueting.ui.uiEt

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author  aqrLei on 2018/5/2
 */
@Suppress("unused")
fun <T> Single<T>.threadSwitch(): Single<T> {
    return observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
}