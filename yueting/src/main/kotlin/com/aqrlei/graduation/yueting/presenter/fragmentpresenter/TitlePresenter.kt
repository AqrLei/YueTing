package com.aqrlei.graduation.yueting.presenter.fragmentpresenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.DateFormatUtil
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.ui.fragment.TitleFragment
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * created by AqrLei on 2018/4/23
 */
class TitlePresenter(mMvpView: TitleFragment) :
        MvpContract.FragmentPresenter<TitleFragment>(mMvpView) {
    companion object {
        fun selectTypeObservable(type: String): Single<ArrayList<String>> {
            return Single.defer {
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
                Single.just(typeList)
            }
        }

        fun addDataToDB(type: String, name: String): Single<Boolean> {
            return Single.defer {
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
                Single.just(DBManager.finish())

            }
        }

    }

    fun addData(type: String, name: String) {
        addDataToDB(type, name)
        val disposable =
                addDataToDB(type, name)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            mMvpView.addFinish(it)
                        }, {})
        addDisposables(disposable)
    }

    fun getTypeInfoFromDB(type: String) {
        val disposables =
                selectTypeObservable(type)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            mMvpView.setTitleList(it)
                        }, {})
        addDisposables(disposables)
    }
}