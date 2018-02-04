package com.aqrlei.graduation.yueting.presenter.activitypresenter

import android.os.Environment
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.info.FileInfo
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.DataSerializationUtil
import com.aqrairsigns.aqrleilib.util.DateFormatUtil
import com.aqrairsigns.aqrleilib.util.FileUtil
import com.aqrairsigns.aqrleilib.view.PageView
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.factory.PageFactory
import com.aqrlei.graduation.yueting.model.local.BookInfo
import com.aqrlei.graduation.yueting.ui.ReadActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/11/17.
 */
class ReadActivityPresenter(mMvpActivity: ReadActivity) :
        MvpContract.ActivityPresenter<ReadActivity>(mMvpActivity) {
    companion object {
        fun addDataToDB(data: ArrayList<FileInfo>): Observable<Boolean> {
            return Observable.defer {
                for (i in 0 until data.size) {
                    val suffix = FileUtil.getFileSuffix(data[i])
                    if (suffix != "txt" && suffix != "pdf") continue
                    val dateTime = DateFormatUtil.simpleDateFormat(System.currentTimeMillis())
                    val tempData = data[i]
                    val byteData = DataSerializationUtil.sequenceToByteArray(tempData)

                    DBManager.sqlData(
                            DBManager.SqlFormat.insertSqlFormat(YueTingConstant.BOOK_TABLE_NAME,
                                    arrayOf("path", "fileInfo", "createTime")),
                            arrayOf(tempData.path, byteData, dateTime),
                            null,
                            DBManager.SqlType.INSERT
                    )


                }
                val result = DBManager.finish()
                Observable.just(result)
            }
        }

    }

    fun addDataToDataBase(data: ArrayList<FileInfo>) {
        val disposables = CompositeDisposable()
        addDisposables(disposables)
        disposables.add(addDataToDB(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Boolean>() {
                    override fun onComplete() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onError(e: Throwable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onNext(t: Boolean) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
        )

    }

    fun getPageFactory(bookInfo: BookInfo?, pageView: PageView): PageFactory {
        val bookMessage = BookInfo()
        val sd = Environment.getExternalStorageDirectory()
        val path = bookInfo?.path ?: sd.path+"/太古神王.txt"
        val file = File(path)
        bookMessage.name = file.name
        bookMessage.path = file.path
        bookMessage.encoding = "GBK"
        return PageFactory(pageView, bookMessage)
    }
}