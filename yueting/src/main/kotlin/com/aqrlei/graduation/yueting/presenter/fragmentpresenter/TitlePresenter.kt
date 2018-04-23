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
        fun selectTypeObservable(type: String): Observable<Cursor?> {
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
                Observable.just(c)
            }
        }

    }

    fun getTypeInfoFromDB(type: String) {
        val disposables = CompositeDisposable().apply {
            add(selectTypeObservable(type)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableObserver<Cursor>() {
                        override fun onComplete() {}
                        override fun onError(e: Throwable) {}
                        override fun onNext(t: Cursor) {
                            val typeList = ArrayList<String>()
                            while (t.moveToNext()) {
                                val temp = t.getString(t.getColumnIndex(DataConstant.TYPE_TABLE_C0_NAME))
                                typeList.add(temp)
                            }
                            mMvpView.setTitleList(typeList)
                        }
                    })
            )
        }
        addDisposables(disposables)
    }
}