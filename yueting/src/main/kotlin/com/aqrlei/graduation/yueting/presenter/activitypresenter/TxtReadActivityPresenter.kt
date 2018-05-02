package com.aqrlei.graduation.yueting.presenter.activitypresenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.DateFormatUtil
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.model.local.infotool.ShareBookInfo
import com.aqrlei.graduation.yueting.ui.TxtReadActivity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
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
        fun markObservable(path: String, currentBegin: Int): Single<Boolean> {
            return Single.defer {
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
                Single.just(DBManager.finish())
            }
        }

        fun indexObservable(path: String, begin: Int, end: Int): Single<Boolean> {
            return Single.defer {
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
                Single.just(DBManager.finish())
            }
        }
    }

    fun addIndexToDB(path: String, begin: Int, end: Int) {
        val disposables =
                indexObservable(path, begin, end)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
        addDisposables(disposables)

    }

    fun addMarkToDB(path: String, currentBegin: Int) {
        val disposables =
                markObservable(path, currentBegin)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            AppToast.toastShow(mMvpActivity, if (it) "书签添加完毕" else "书签添加失败", 1000)
                        }, {})
        addDisposables(disposables)
    }
}