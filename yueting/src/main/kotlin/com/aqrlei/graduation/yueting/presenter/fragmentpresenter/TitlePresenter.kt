package com.aqrlei.graduation.yueting.presenter.fragmentpresenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.DateFormatUtil
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.ui.fragment.TitleFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * created by AqrLei on 2018/4/23
 */
class TitlePresenter(mMvpView: TitleFragment) :
        MvpContract.FragmentPresenter<TitleFragment>(mMvpView) {
    companion object {
        fun selectTypeObservable(type: String): Observable<ArrayList<String>> {
            return Observable.defer {
                val typeList = ArrayList<String>()
                DBManager.sqlData(
                        DBManager.SqlFormat.selectSqlFormat(
                                DataConstant.TYPE_TABLE_NAME,
                                DataConstant.TYPE_TABLE_C0_NAME,
                                DataConstant.TYPE_TABLE_C1_TYPE,
                                "="),
                        null, arrayOf(type), DBManager.SqlType.SELECT)
                        .getCursor()?.apply {
                            while (moveToNext()) {
                                val temp = getString(getColumnIndex(DataConstant.TYPE_TABLE_C0_NAME))
                                typeList.add(temp)
                            }
                        }
                Observable.just(typeList)
            }
        }

        fun addDataToDB(type: String, name: String): Observable<Boolean> {
            return Observable.defer {
                val dateTime = DateFormatUtil.simpleDateFormat(System.currentTimeMillis())
                DBManager.sqlData(
                        DBManager.SqlFormat.insertSqlFormat(
                                DataConstant.TYPE_TABLE_NAME,
                                arrayOf(DataConstant.TYPE_TABLE_C0_NAME,
                                        DataConstant.TYPE_TABLE_C1_TYPE,
                                        DataConstant.COMMON_COLUMN_CREATE_TIME)),
                        arrayOf(name, type, dateTime),
                        null,
                        DBManager.SqlType.INSERT)
                Observable.just(DBManager.finish())

            }
        }

    }

    fun addData(type: String, name: String) {
        val disposable = CompositeDisposable().apply {
            add(addDataToDB(type, name)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        mMvpView.addFinish(it)
                    }))
        }
        addDisposables(disposable)
    }

    fun getTypeInfoFromDB(type: String) {
        val disposables = CompositeDisposable().apply {
            add(selectTypeObservable(type)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        mMvpView.setTitleList(it)
                    })
            )
        }
        addDisposables(disposables)
    }
}