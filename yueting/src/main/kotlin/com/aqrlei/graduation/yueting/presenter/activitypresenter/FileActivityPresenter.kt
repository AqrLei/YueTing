package com.aqrlei.graduation.yueting.presenter.activitypresenter

import android.media.MediaMetadataRetriever
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.info.FileInfo
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.DataSerializationUtil
import com.aqrairsigns.aqrleilib.util.DateFormatUtil
import com.aqrairsigns.aqrleilib.util.FileUtil
import com.aqrlei.graduation.yueting.aidl.MusicInfo
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.model.local.BookInfo
import com.aqrlei.graduation.yueting.model.local.infotool.ShareBookInfo
import com.aqrlei.graduation.yueting.model.local.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.ui.FileActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.io.File

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
                    if (suffix != "mp3" && suffix != "ape" && suffix != "txt" && suffix != "pdf") continue
                    val dateTime = DateFormatUtil.simpleDateFormat(System.currentTimeMillis())
                    val tempData = data[i]
                    val byteData = DataSerializationUtil.sequenceToByteArray(tempData)
                    val name = tempData.name.substring(0, tempData.name.lastIndexOf("."))//(fileInfo.name.toLowerCase()).replace("\\.mp3$".toRegex(), "")
                    if (suffix == "mp3" || suffix == "ape") {
                        val musicInfo = MusicInfo()
                        val mmr = MediaMetadataRetriever()
                        mmr.setDataSource(tempData.path)
                        musicInfo.albumUrl = tempData.path
                        musicInfo.title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                                ?: name
                        musicInfo.album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                                ?: "未知"
                        musicInfo.artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                                ?: "未知"
                        musicInfo.duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toInt()
                        musicInfo.picture = mmr.embeddedPicture
                        if (!ShareMusicInfo.MusicInfoTool.has(musicInfo)) {
                            ShareMusicInfo.MusicInfoTool.addInfo(musicInfo)
                        }
                        DBManager.sqlData(
                                DBManager.SqlFormat.insertSqlFormat(
                                        DataConstant.MUSIC_TABLE_NAME,
                                        arrayOf(DataConstant.COMMON_COLUMN_PATH,
                                                DataConstant.MUSIC_TABLE_C2_FILE_INFO,
                                                DataConstant.COMMON_COLUMN_CREATE_TIME)),
                                arrayOf(tempData.path, byteData, dateTime),
                                null,
                                DBManager.SqlType.INSERT
                        )
                    } else {
                        val bookInfo = BookInfo()
                        bookInfo.type = suffix
                        bookInfo.name = name
                        bookInfo.path = tempData.path
                        bookInfo.fileLength = File(tempData.path).length().toInt()
                        DBManager.sqlData(
                                DBManager.SqlFormat.insertSqlFormat(
                                        DataConstant.BOOK_TABLE_NAME,
                                        arrayOf(
                                                DataConstant.COMMON_COLUMN_PATH,
                                                DataConstant.BOOK_TABLE_C1_TYPE,
                                                DataConstant.BOOK_TABLE_C4_FILE_INFO,
                                                DataConstant.COMMON_COLUMN_CREATE_TIME)),
                                arrayOf(tempData.path, suffix, byteData, dateTime),
                                null,
                                DBManager.SqlType.INSERT
                        )
                        if (!ShareBookInfo.BookInfoTool.has(bookInfo)) {
                            ShareBookInfo.BookInfoTool.addInfo(bookInfo)
                        }
                    }
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

    fun addToDataBase(data: ArrayList<FileInfo>) {// True: music/ False: book
        val musicSize = ShareMusicInfo.MusicInfoTool.getSize()
        val bookSize = ShareBookInfo.BookInfoTool.getSize()
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
                        mMvpActivity.finishActivity(t,
                                musicSize != ShareMusicInfo.MusicInfoTool.getSize(),
                                bookSize != ShareBookInfo.BookInfoTool.getSize())
                    }
                }))


    }

}