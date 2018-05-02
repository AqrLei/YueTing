package com.aqrlei.graduation.yueting.presenter.fragmentpresenter

import android.database.Cursor
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.ui.fragment.TitleFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * created by AqrLei on 2018/4/23
 */
class TitlePresenter(mMvpView: TitleFragment) :
        MvpContract.FragmentPresenter<TitleFragment>(mMvpView) {
    companion object {
        fun selectTypeObservable(type: String): Observable<ArrayList<String>> {
            return Observable.defer {
                val c = DBManager.sqlData(
                        DBManager.SqlFormat.selectSqlFormat(
                                DataConstant.TYPE_TABLE_NAME,
                                DataConstant.TYPE_TABLE_C0_NAME,
                                DataConstant.TYPE_TABLE_C1_TYPE,
                                "="
                        ),
                        null, arrayOf(type), DBManager.SqlType.SELECT
                )
                        .getCursor()
                val typeList = ArrayList<String>()
                c?.let {
                    while (it.moveToNext()) {
                        val temp = it.getString(it.getColumnIndex(DataConstant.TYPE_TABLE_C0_NAME))
                        typeList.add(temp)
                    }
                }

                Observable.just(typeList)
            }
        }

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