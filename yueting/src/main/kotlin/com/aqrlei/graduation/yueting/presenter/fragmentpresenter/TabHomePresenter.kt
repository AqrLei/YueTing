package com.aqrlei.graduation.yueting.presenter.fragmentpresenter

import android.app.Service
import android.content.Context
import android.content.ServiceConnection
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.os.Message
import android.os.Messenger
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.info.FileInfo
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.DataSerializationUtil
import com.aqrairsigns.aqrleilib.util.ImageUtil
import com.aqrairsigns.aqrleilib.util.StringChangeUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.YueTingApplication
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.aidl.MusicInfo
import com.aqrlei.graduation.yueting.model.local.MusicInfoList
import com.aqrlei.graduation.yueting.model.local.infotool.ShareMusicInfo
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

        fun sendMusicInfoObservable(messenger: Messenger): Observable<Boolean> {
            return Observable.defer {
                var bool = false
                try {
                    val msg = Message()
                    msg.what = 0x11
                    val musicInfoList = MusicInfoList(ShareMusicInfo.MusicInfoTool.getInfoS())

                    msg.obj = musicInfoList
                    messenger.send(msg)
                    bool = true
                } catch (e: Exception) {
                    e.printStackTrace()
                    bool = false
                }
                Observable.just(bool)
            }
        }
    }

    fun sendMusicInfo(messenger: Messenger) {
        val disposables = CompositeDisposable()
        addDisposables(disposables)
        disposables.add(
                sendMusicInfoObservable(messenger)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableObserver<Boolean>() {
                            override fun onComplete() {


                            }

                            override fun onError(e: Throwable) {

                            }

                            override fun onNext(t: Boolean) {
                                mMvpView.unbindMusicService()
                            }
                        })
        )

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
                                    val fileInfo = DataSerializationUtil.byteArrayToSequence(t.getBlob(t.getColumnIndex("fileInfo")))
                                            as FileInfo
                                    val name = (fileInfo.name.toLowerCase()).replace("\\.mp3$".toRegex(), "")
                                    val path = t.getString(t.getColumnIndex("path"))
                                    mmr.setDataSource(path)
                                    musicInfo.id = t.getInt(t.getColumnIndex("id"))
                                    musicInfo.createTime = t.getString(t.getColumnIndex("createTime"))
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
                                DBManager.releaseCursor()
                                mMvpView.setMusicInfo(musicInfoList)
                            }

                        })
        )
    }

    fun startMusicService(context: Context, position: Int, messenger: Messenger, conn: ServiceConnection) {
        val mContext = context.applicationContext as YueTingApplication
        val musicIntent = mContext.getServiceIntent()
        musicIntent?.putExtra("position", position)
        musicIntent?.putExtra("messenger", messenger)
        context.startService(musicIntent)
        context.bindService(musicIntent, conn, Service.BIND_AUTO_CREATE)
    }

    fun refreshPlayView(view: LinearLayout, msg: Message) {
        view.visibility = View.VISIBLE
        when (msg.what) {
            YueTingConstant.CURRENT_DURATION -> {

            }
            YueTingConstant.PLAY_STATE -> {
                val musicInfo = ShareMusicInfo.MusicInfoTool.getInfo(msg.arg2)
                val bitmap = ImageUtil.byteArrayToBitmap(musicInfo.picture)
                (view.findViewById(R.id.iv_album_picture) as ImageView).setImageBitmap(bitmap)

                (view.findViewById(R.id.tv_music_info) as TextView).text =
                        StringChangeUtil.SPANNABLE.clear()
                                .foregroundColorChange("#1c4243", musicInfo.title)
                                .relativeSizeChange(2 / 3F, "\n${musicInfo.artist} - ${musicInfo.album}")
                                .complete()
            }
        }

    }


}