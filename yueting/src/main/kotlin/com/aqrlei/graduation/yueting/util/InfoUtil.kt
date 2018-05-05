package com.aqrlei.graduation.yueting.util

import android.media.MediaMetadataRetriever
import com.aqrlei.graduation.yueting.aidl.MusicInfo
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.BookInfo
import java.io.File

/**
 * created by AqrLei at 11:38 on 星期六, 五月 05, 2018
 */
fun generateMusicInfo(path: String, id: Int, createTime: String): MusicInfo {
    return MusicInfo().apply {
        val mmr = MediaMetadataRetriever()
        val name = File(path).let {
            val temp = it.name.substring(0, it.name.lastIndexOf("."))
            //(it.name.toLowerCase()).replace("\\.mp3$".toRegex(), "")
            temp
        }
        mmr.setDataSource(path)
        this.id = id
        this.createTime = createTime
        this.albumUrl = path
        this.title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                ?: name
        this.album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                ?: YueTingConstant.INFO_UNKNOWN
        this.artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                ?: YueTingConstant.INFO_UNKNOWN
        this.duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toInt()

        this.picture = mmr.embeddedPicture
        mmr.release()
    }
}

fun generateBookInfo(path: String, id: Int, createTime: String, type: String, begin: Int, end: Int): BookInfo {
    return BookInfo().apply {
        val name = File(path).let {
            val temp = it.name.substring(0, it.name.lastIndexOf("."))
            //(it.name.toLowerCase()).replace("\\.mp3$".toRegex(), "")
            temp
        }
        this.id = id
        this.createTime = createTime
        this.type = type
        this.name = name
        this.path = path
        fileLength = File(path).length().toInt()
        indexBegin = begin
        indexEnd = end
    }
}