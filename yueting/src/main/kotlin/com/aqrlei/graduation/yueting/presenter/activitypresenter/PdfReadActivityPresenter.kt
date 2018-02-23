package com.aqrlei.graduation.yueting.presenter.activitypresenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.DateFormatUtil
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.factory.ChapterFactory
import com.aqrlei.graduation.yueting.ui.PdfReadActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/11/17.
 */
class PdfReadActivityPresenter(mMvpActivity: PdfReadActivity) :
        MvpContract.ActivityPresenter<PdfReadActivity>(mMvpActivity) {
    companion object {
        fun catalogsObservable(): Observable<Boolean> {
            return Observable.defer {
                Observable.just(ChapterFactory.CHAPTER.getBookMarkFromDB())
            }
        }
    }

    fun getCatalog() {
        val disposables = CompositeDisposable()
        addDisposables(disposables)
        disposables.add(
                catalogsObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableObserver<Boolean>() {
                            override fun onComplete() {
                            }

                            override fun onError(e: Throwable) {
                            }

                            override fun onNext(t: Boolean) {
                                mMvpActivity.jumpToCatalog(t)
                            }
                        }
                        )

        )

    }


}