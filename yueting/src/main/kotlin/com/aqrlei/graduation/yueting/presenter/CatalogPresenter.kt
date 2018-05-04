package com.aqrlei.graduation.yueting.presenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrlei.graduation.yueting.model.observable.BookSingle
import com.aqrlei.graduation.yueting.ui.CatalogActivity

/**
 * Author : AqrLei
 * Date : 2017/11/17.
 */
class CatalogPresenter(mMvpActivity: CatalogActivity) :
        MvpContract.ActivityPresenter<CatalogActivity>(mMvpActivity) {
    fun getChapter() {
        val disposables =
                BookSingle.selectChapters()
                        .subscribe({
                            mMvpActivity.loadCatalogDone(it)
                        }, {})
        addDisposables(disposables)
    }

}