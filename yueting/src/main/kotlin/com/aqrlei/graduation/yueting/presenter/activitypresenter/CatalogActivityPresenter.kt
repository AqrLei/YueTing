package com.aqrlei.graduation.yueting.presenter.activitypresenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrlei.graduation.yueting.factory.ChapterFactory
import com.aqrlei.graduation.yueting.ui.CatalogActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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
        fun catalogsObservable(): Observable<Boolean> {
            return Observable.defer {
                ChapterFactory.CHAPTER.getBookMarkFromDB()
                Observable.just(ChapterFactory.CHAPTER.getChapter())
            }
        }
    }

    fun getData() {
        val disposables = CompositeDisposable()
        addDisposables(disposables)
        disposables.add(
                catalogsObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            mMvpActivity.loadCatalogDone(it)
                        })
        )

    }

}