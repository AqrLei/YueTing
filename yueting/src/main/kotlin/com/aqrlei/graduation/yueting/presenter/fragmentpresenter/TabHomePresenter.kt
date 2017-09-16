package com.aqrlei.graduation.yueting.presenter.fragmentpresenter

import android.database.Cursor
import android.media.MediaMetadataRetriever
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.info.FileInfo
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.SQLDataUtil
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.MusicInfo
import com.aqrlei.graduation.yueting.ui.fragment.TabHomeFragment
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
class TabHomePresenter(mMvpView: TabHomeFragment) :
        MvpContract.FragmentPresenter<TabHomeFragment>(mMvpView) {

    companion object {
        fun selectObservable(): Observable<Cursor?> {
            return Observable.defer {
                val c = DBManager.sqlData(DBManager.SqlFormat.selectSqlFormat(
                        YueTingConstant.MUSIC_TABLE_NAME),
                        null, null, DBManager.SqlType.SELECT)
                        .getCursor()
                Observable.just(c)
            }
        }
    }

    fun getMusicInfoFromDB() {
        val disposables = CompositeDisposable()
        addDisposables(disposables)
        val musicInfoList = ArrayList<MusicInfo>()
        disposables.add(
                selectObservable()
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
                                    val fileInfo = SQLDataUtil.getData(t.getBlob(t.getColumnIndex("fileInfo")))
                                            as FileInfo
                                    val name = (fileInfo.name.toLowerCase()).replace("\\.mp3$".toRegex(), "")
                                    mmr.setDataSource(t.getString(t.getColumnIndex("path")))
                                    musicInfo.id = t.getInt(t.getColumnIndex("id"))
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
                                DBManager.releaseCursor()
                                mMvpView.setMusicInfo(musicInfoList)
                            }

                        })
        )
    }


}