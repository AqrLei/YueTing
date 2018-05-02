package com.aqrlei.graduation.yueting.presenter.activitypresenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrlei.graduation.yueting.factory.ChapterFactory
import com.aqrlei.graduation.yueting.ui.CatalogActivity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/11/17.
 */
class CatalogActivityPresenter(mMvpActivity: CatalogActivity) :
        MvpContract.ActivityPresenter<CatalogActivity>(mMvpActivity) {
    companion object {
        fun catalogsObservable(): Single<Boolean> {
            return Single.defer {
                ChapterFactory.CHAPTER.getBookMarkFromDB()
                Single.just(ChapterFactory.CHAPTER.getChapter())
            }
        }
    }

    fun getData() {
        val disposables =
                catalogsObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            mMvpActivity.loadCatalogDone(it)
                        }, {})
        addDisposables(disposables)
    }

}