package com.aqrlei.graduation.yueting.presenter.activitypresenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.DateFormatUtil
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.factory.ChapterFactory
import com.aqrlei.graduation.yueting.ui.TxtReadActivity
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
class TxtReadActivityPresenter(mMvpActivity: TxtReadActivity) :
        MvpContract.ActivityPresenter<TxtReadActivity>(mMvpActivity) {
    companion object {
        fun catalogsObservable(): Observable<Boolean> {
            return Observable.defer {
                ChapterFactory.CHAPTER.getBookMarkFromDB()
                Observable.just(ChapterFactory.CHAPTER.getChapter())
            }
        }

        fun markObservable(path: String, currentBegin: Int): Observable<Boolean> {
            return Observable.defer {
                val dateTime = DateFormatUtil.simpleDateFormat(System.currentTimeMillis())
                DBManager.sqlData(
                        DBManager.SqlFormat.insertSqlFormat(
                                YueTingConstant.MARK_TABLE_NAME,
                                arrayOf("path", "markPosition", "createTime")),
                        arrayOf(path, currentBegin, dateTime),
                        null,
                        DBManager.SqlType.INSERT
                )
                Observable.just(DBManager.finish())
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

    fun addMarkToDB(path: String, currentBegin: Int) {
        val disposables = CompositeDisposable()
        addDisposables(disposables)
        disposables.add(markObservable(path, currentBegin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Boolean>() {
                    override fun onComplete() {
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onNext(t: Boolean) {
                        AppToast.toastShow(mMvpActivity, if (t) "书签添加完毕" else "书签添加失败", 1000)
                    }
                }))

    }


}