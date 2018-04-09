package com.aqrlei.graduation.yueting.presenter.fragmentpresenter

import android.app.Service
import android.content.Context
import android.content.ServiceConnection
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.os.IBinder
import android.os.Messenger
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.info.FileInfo
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.DataSerializationUtil
import com.aqrlei.graduation.yueting.YueTingApplication
import com.aqrlei.graduation.yueting.aidl.IMusicInfo
import com.aqrlei.graduation.yueting.aidl.MusicInfo
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.BookInfo
import com.aqrlei.graduation.yueting.model.local.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.ui.fragment.TabHomeFragment
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
 * @Date: 2017/8/23
 */
/*
* @#param mMvpView 访问对应的Fragment
* */
class TabHomePresenter(mMvpView: TabHomeFragment) :
        MvpContract.FragmentPresenter<TabHomeFragment>(mMvpView) {
    companion object {
        fun selectMusicObservable(): Observable<Cursor?> {
            return Observable.defer {
                val c = DBManager.sqlData(DBManager.SqlFormat.selectSqlFormat(
                        DataConstant.MUSIC_TABLE_NAME),
                        null, null, DBManager.SqlType.SELECT)
                        .getCursor()
                Observable.just(c)
            }
        }

        fun selectBookObservable(): Observable<Cursor?> {
            return Observable.defer {
                val c = DBManager.sqlData(DBManager.SqlFormat.selectSqlFormat(
                        DataConstant.BOOK_TABLE_NAME),
                        null, null, DBManager.SqlType.SELECT)
                        .getCursor()
                Observable.just(c)
            }
        }

        fun sendMusicInfoObservable(service: IBinder): Observable<Boolean> {
            return Observable.defer {
                val bool = try {
                    val binder = IMusicInfo.Stub.asInterface(service)
                    val musicInfoList = ArrayList<MusicInfo>()
                    musicInfoList.addAll(ShareMusicInfo.MusicInfoTool.getInfoS())
                    var size = 0
                    var position = 0
                    /*binder一次性传递数据量大小由限制，大概为1024KB，故要分批传递*/
                    for (i in 0 until musicInfoList.size) {
                        val it = musicInfoList[i]
                        size += it.album.length
                        size += it.albumUrl.length
                        size += it.artist.length
                        size += it.createTime.length
                        size += it.picture?.size ?: 0
                        size += it.title.length
                        size += 8
                        if ((size / (1024 * 512)) >= 1) {
                            val list = musicInfoList.subList(position, i + 1)
                            size = 0
                            position = i + 1
                            binder.setMusicInfo(list)
                        }
                    }
                    //将最后的数据传输
                    val list = musicInfoList.subList(position, musicInfoList.size)
                    binder.setMusicInfo(list)
                    true
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
                Observable.just(bool)
            }
        }
    }

    fun sendMusicInfo(service: IBinder) {
        val disposables = CompositeDisposable()
        addDisposables(disposables)
        disposables.add(
                sendMusicInfoObservable(service)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableObserver<Boolean>() {
                            override fun onComplete() {


                            }

                            override fun onError(e: Throwable) {

                            }

                            override fun onNext(t: Boolean) {
                                if (t) {
                                    mMvpView.unbindMusicService()
                                }
                            }
                        })
        )

    }

    fun getMusicInfoFromDB() {
        val disposables = CompositeDisposable()
        addDisposables(disposables)
        val musicInfoList = ArrayList<MusicInfo>()
        disposables.add(
                selectMusicObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableObserver<Cursor>() {
                            override fun onComplete() {
                            }

                            override fun onError(e: Throwable) {
                            }

                            override fun onNext(t: Cursor) {
                                while (t.moveToNext()) {
                                    val musicInfo = MusicInfo()
                                    val mmr = MediaMetadataRetriever()
                                    val fileInfo = DataSerializationUtil.byteArrayToSequence(t.getBlob(t.getColumnIndex("fileInfo")))
                                            as FileInfo

                                    val name = fileInfo.name.substring(0, fileInfo.name.lastIndexOf("."))//(fileInfo.name.toLowerCase()).replace("\\.mp3$".toRegex(), "")
                                    val path = t.getString(t.getColumnIndex(DataConstant.MUSIC_TABLE_C0_PATH))
                                    mmr.setDataSource(path)
                                    musicInfo.id = t.getInt(t.getColumnIndex(DataConstant.COMMON_COLUMN_ID))
                                    musicInfo.createTime = t.getString(t.getColumnIndex(DataConstant.COMMON_COLUNM_CREATE_TIME))
                                    musicInfo.albumUrl = path ?: " "
                                    musicInfo.title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                                            ?: name
                                    musicInfo.album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                                            ?: "未知"
                                    musicInfo.artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                                            ?: "未知"
                                    musicInfo.duration = mmr.extractMetadata(
                                            MediaMetadataRetriever.METADATA_KEY_DURATION).toInt()
                                    musicInfo.picture = mmr.embeddedPicture



                                    musicInfoList.add(musicInfo)
                                    mmr.release()
                                }
                                //  DBManager.releaseCursor()
                                mMvpView.setMusicInfo(musicInfoList)
                            }

                        })
        )
    }

    fun getBookInfoFromDB() {
        val disposables = CompositeDisposable()
        addDisposables(disposables)
        val bookInfoList = ArrayList<BookInfo>()
        disposables.add(
                selectBookObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableObserver<Cursor>() {
                            override fun onComplete() {
                            }

                            override fun onError(e: Throwable) {
                            }

                            override fun onNext(t: Cursor) {
                                while (t.moveToNext()) {
                                    val bookInfo = BookInfo()
                                    val fileInfo = DataSerializationUtil.byteArrayToSequence(t.getBlob(t.getColumnIndex(DataConstant.BOOK_TABLE_C4_FILE_INFO)))
                                            as FileInfo
                                    val name = fileInfo.name.substring(0, fileInfo.name.lastIndexOf("."))
                                    val path = t.getString(t.getColumnIndex(DataConstant.BOOK_TABLE_C0_PATH))
                                    bookInfo.id = t.getInt(t.getColumnIndex(DataConstant.COMMON_COLUMN_ID))
                                    bookInfo.createTime = t.getString(t.getColumnIndex(DataConstant.COMMON_COLUNM_CREATE_TIME))
                                    bookInfo.type = t.getString(t.getColumnIndex(DataConstant.BOOK_TABLE_C1_TYPE))
                                    bookInfo.name = name
                                    bookInfo.path = path ?: ""
                                    bookInfo.fileLength = File(path).length().toInt()
                                    bookInfo.indexBegin = t.getInt(t.getColumnIndex(DataConstant.BOOK_TABLE_C2_INDEX_BEGIN))
                                    bookInfo.indexEnd = t.getInt(t.getColumnIndex(DataConstant.BOOK_TABLE_C3_INDEX_END))


                                    bookInfoList.add(bookInfo)
                                }
                                //  DBManager.releaseCursor()
                                mMvpView.setBookInfo(bookInfoList)
                            }

                        })
        )
    }

    fun deleteMusicItemFromDB(path: String) {
        DBManager.sqlData(
                DBManager.SqlFormat.deleteSqlFormat(DataConstant.MUSIC_TABLE_NAME,
                        DataConstant.MUSIC_TABLE_C0_PATH, "="),
                null, arrayOf(path), DBManager.SqlType.DELETE)
    }

    fun deleteBookItemFromDB(path: String) {
        DBManager.sqlData(
                DBManager.SqlFormat.deleteSqlFormat(DataConstant.BOOK_TABLE_NAME,
                        DataConstant.BOOK_TABLE_C0_PATH, "="),
                null, arrayOf(path), DBManager.SqlType.DELETE)
        DBManager.sqlData(
                DBManager.SqlFormat.deleteSqlFormat(DataConstant.MARK_TABLE_NAME,
                        DataConstant.MARK_TABLE_C0_PATH, "="),
                null, arrayOf(path), DBManager.SqlType.DELETE)
        DBManager.sqlData(
                DBManager.SqlFormat.deleteSqlFormat(DataConstant.CATALOG_TABLE_NAME,
                        DataConstant.CATALOG_TABLE_C0_PATH, "="),
                null, arrayOf(path), DBManager.SqlType.DELETE)
    }


    fun startMusicService(context: Context, position: Int, messenger: Messenger, conn: ServiceConnection) {
        val mContext = context.applicationContext as YueTingApplication
        val musicIntent = mContext.getServiceIntent()
        musicIntent?.putExtra(YueTingConstant.SERVICE_MUSIC_ITEM_POSITION, position)
        musicIntent?.putExtra(YueTingConstant.SERVICE_MUSIC_MESSENGER, messenger)
        context.startService(musicIntent)
        context.bindService(musicIntent, conn, Service.BIND_AUTO_CREATE)
    }

}