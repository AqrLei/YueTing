package com.aqrlei.graduation.yueting.model.observable

import android.media.MediaMetadataRetriever
import android.os.IBinder
import com.aqrairsigns.aqrleilib.info.FileInfo
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.DataSerializationUtil
import com.aqrlei.graduation.yueting.aidl.IMusicInfo
import com.aqrlei.graduation.yueting.aidl.MusicInfo
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.ui.uiEt.threadSwitch
import io.reactivex.Single

/**
 * @author  aqrLei on 2018/5/2
 */
object MusicSingle {

    fun selectMusicInfo(typeName: String): Single<ArrayList<MusicInfo>> {
        return Single.defer {
            val musicInfoList = ArrayList<MusicInfo>()
            DBManager.sqlData(
                    DBManager.SqlFormat.selectSqlFormat(
                            DataConstant.MUSIC_TABLE_NAME,
                            "",
                            DataConstant.MUSIC_TABLE_C1_TYPE_NAME,
                            "="),
                    null, arrayOf(typeName), DBManager.SqlType.SELECT)
                    .getCursor()?.let {
                        while (it.moveToNext()) {
                            val musicInfo = MusicInfo()
                            val mmr = MediaMetadataRetriever()
                            val fileInfo = DataSerializationUtil.byteArrayToSequence(it.getBlob(it.getColumnIndex("fileInfo")))
                                    as FileInfo
                            val name = fileInfo.name.substring(0, fileInfo.name.lastIndexOf("."))//(fileInfo.name.toLowerCase()).replace("\\.mp3$".toRegex(), "")
                            val path = it.getString(it.getColumnIndex(DataConstant.COMMON_COLUMN_PATH))
                            mmr.setDataSource(path)
                            musicInfo.id = it.getInt(it.getColumnIndex(DataConstant.COMMON_COLUMN_ID))
                            musicInfo.createTime = it.getString(it.getColumnIndex(DataConstant.COMMON_COLUMN_CREATE_TIME))
                            musicInfo.albumUrl = path ?: " "
                            musicInfo.title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                                    ?: name
                            musicInfo.album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                                    ?: YueTingConstant.INFO_UNKNOWN
                            musicInfo.artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                                    ?: YueTingConstant.INFO_UNKNOWN
                            musicInfo.duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toInt()

                            musicInfo.picture = mmr.embeddedPicture
                            musicInfoList.add(musicInfo)
                            mmr.release()
                        }
                    }
            Single.just(musicInfoList).threadSwitch()
        }
    }

    fun sendMusicInfo(service: IBinder): Single<Boolean> {
        return Single.defer {
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
            Single.just(bool).threadSwitch()
        }
    }
}