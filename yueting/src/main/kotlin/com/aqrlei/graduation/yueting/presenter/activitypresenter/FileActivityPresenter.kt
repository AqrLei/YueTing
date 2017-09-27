package com.aqrlei.graduation.yueting.presenter.activitypresenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.info.FileInfo
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.DateFormatUtil
import com.aqrairsigns.aqrleilib.util.FileUtil
import com.aqrairsigns.aqrleilib.util.DataSerializationUtil
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.ui.FileActivity
import kotlin.collections.ArrayList
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/11 Time: 13:41
 */
class FileActivityPresenter(mMvpActivity: FileActivity) :
        MvpContract.ActivityPresenter<FileActivity>(mMvpActivity) {
    companion object {
        fun createFileInfo(path: String): Observable<ArrayList<FileInfo>> {
            return Observable.defer {
                val fileInfoList = FileUtil.createFileInfoS(path)
                Observable.just(fileInfoList)
            }
        }

        fun addDataToDB(data: ArrayList<FileInfo>): Observable<Boolean> {
            return Observable.defer {
                for (i in 0 until data.size) {

                    val suffix = FileUtil.getFileSuffix(data[i])
                    if (suffix != "mp3" && suffix != "ape") continue

                    val dateTime = DateFormatUtil.simpleDateFormat(System.currentTimeMillis())
                    val tempData = data[i]
                    val byteData = DataSerializationUtil.sequenceToByteArray(tempData)

                    DBManager.sqlData(
                            DBManager.SqlFormat.insertSqlFormat(YueTingConstant.MUSIC_TABLE_NAME,
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

    fun getFileInfo(path: String) {
        val disposables = CompositeDisposable()
        addDisposables(disposables)
        disposables.add(createFileInfo(path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<ArrayList<FileInfo>>() {
                    override fun onComplete() {
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onNext(t: ArrayList<FileInfo>) {
                        mMvpActivity.changeFileInfo(t)
                    }
                }))
    }

    fun addToDataBase(data: ArrayList<FileInfo>) {
        val disposables = CompositeDisposable()
        addDisposables(disposables)
        disposables.add(addDataToDB(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Boolean>() {
                    override fun onComplete() {
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onNext(t: Boolean) {
                        if (t) {
                            mMvpActivity.finishActivity()
                        }

                    }
                }))


    }

}