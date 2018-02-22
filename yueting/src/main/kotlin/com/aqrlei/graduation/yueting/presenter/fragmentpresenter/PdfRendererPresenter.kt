package com.aqrlei.graduation.yueting.presenter.fragmentpresenter


import android.graphics.PointF
import android.view.MotionEvent
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.DateFormatUtil
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.factory.ChapterFactory
import com.aqrlei.graduation.yueting.ui.fragment.PdfRendererFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/23
 */
/*
* @#param mMvpView 访问对应的Fragment
* */
class PdfRendererPresenter(mMvpView: PdfRendererFragment) :
        MvpContract.FragmentPresenter<PdfRendererFragment>(mMvpView) {

    companion object {
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
                        AppToast.toastShow(mMvpView.activity, if (t) "书签添加完毕" else "书签添加失败", 1000)
                    }
                }))

    }
    fun distance(event: MotionEvent): Float {
        val dx = event.getX(1) - event.getX(0)
        val dy = event.getY(1) - event.getY(0)
        return Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
    }

    /**
     * 计算两个手指间的中间点
     */
    fun mid(event: MotionEvent): PointF {
        val midX = (event.getX(1) + event.getX(0)) / 2
        val midY = (event.getY(1) + event.getY(0)) / 2
        return PointF(midX, midY)
    }


}