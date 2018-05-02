package com.aqrlei.graduation.yueting.presenter.activitypresenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrlei.graduation.yueting.model.observable.BookSingle
import com.aqrlei.graduation.yueting.ui.TxtReadActivity

/**
 * Author : AqrLei
 * Date : 2017/11/17.
 */
class TxtReadActivityPresenter(mMvpActivity: TxtReadActivity) :
        MvpContract.ActivityPresenter<TxtReadActivity>(mMvpActivity) {

    fun addIndexToDB(path: String, begin: Int, end: Int) {
        val disposables =
                BookSingle.updateIndex(path, begin, end)
                        .subscribe()
        addDisposables(disposables)

    }

    fun addMarkToDB(path: String, currentBegin: Int) {
        val disposables =
               BookSingle.insertMark(path, currentBegin)
                        .subscribe({
                            AppToast.toastShow(mMvpActivity, if (it) "书签添加完毕" else "书签添加失败", 1000)
                        }, {})
        addDisposables(disposables)
    }
}