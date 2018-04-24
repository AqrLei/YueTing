package com.aqrlei.graduation.yueting.presenter.activitypresenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.DateFormatUtil
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.model.local.infotool.ShareBookInfo
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
        fun markObservable(path: String, currentBegin: Int): Observable<Boolean> {
            return Observable.defer {
                val dateTime = DateFormatUtil.simpleDateFormat(System.currentTimeMillis())
                DBManager.sqlData(
                        DBManager.SqlFormat.insertSqlFormat(
                                DataConstant.MARK_TABLE_NAME,
                                arrayOf(DataConstant.COMMON_COLUMN_PATH,
                                        DataConstant.MARK_TABLE_C1_MARK_POSITION,
                                        DataConstant.COMMON_COLUMN_CREATE_TIME)),
                        arrayOf(path, currentBegin, dateTime),
                        null,
                        DBManager.SqlType.INSERT
                )
                Observable.just(DBManager.finish())
            }
        }

        fun indexObservable(path: String, begin: Int, end: Int): Observable<Boolean> {
            return Observable.defer {
                if (ShareBookInfo.BookInfoTool.same(path)) {
                    ShareBookInfo.BookInfoTool.setBookInfoIndex(path, begin, end)
                }
                DBManager.sqlData(
                        DBManager.SqlFormat.updateSqlFormat(
                                DataConstant.BOOK_TABLE_NAME,
                                DataConstant.BOOK_TABLE_C2_INDEX_BEGIN,
                                DataConstant.COMMON_COLUMN_PATH, "="),
                        arrayOf(begin, path),
                        null,
                        DBManager.SqlType.UPDATE
                )
                DBManager.sqlData(
                        DBManager.SqlFormat.updateSqlFormat(
                                DataConstant.BOOK_TABLE_NAME,
                                DataConstant.BOOK_TABLE_C2_INDEX_BEGIN,
                                DataConstant.COMMON_COLUMN_PATH, "="),
                        arrayOf(end, path),
                        null,
                        DBManager.SqlType.UPDATE
                )
                Observable.just(DBManager.finish())
            }
        }
    }

    fun addIndexToDB(path: String, begin: Int, end: Int) {
        val disposables = CompositeDisposable()
        addDisposables(disposables)
        disposables.add(
                indexObservable(path, begin, end)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableObserver<Boolean>() {
                            override fun onComplete() {}
                            override fun onError(e: Throwable) {}
                            override fun onNext(t: Boolean) {}
                        }
                        ))

    }

    fun addMarkToDB(path: String, currentBegin: Int) {
        val disposables = CompositeDisposable()
        addDisposables(disposables)
        disposables.add(markObservable(path, currentBegin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Boolean>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {}
                    override fun onNext(t: Boolean) {
                        AppToast.toastShow(mMvpActivity, if (t) "书签添加完毕" else "书签添加失败", 1000)
                    }
                }))
    }
}