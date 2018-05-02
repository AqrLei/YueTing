package com.aqrlei.graduation.yueting.model.observable

import android.media.MediaMetadataRetriever
import com.aqrairsigns.aqrleilib.info.FileInfo
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.DataSerializationUtil
import com.aqrairsigns.aqrleilib.util.DateFormatUtil
import com.aqrairsigns.aqrleilib.util.FileUtil
import com.aqrlei.graduation.yueting.aidl.MusicInfo
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.BookInfo
import com.aqrlei.graduation.yueting.model.infotool.ShareBookInfo
import com.aqrlei.graduation.yueting.model.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.ui.uiEt.threadSwitch
import io.reactivex.Single
import java.io.File

/**
 * @author  aqrLei on 2018/5/2
 */
object FileSingle {
    fun createFileInfo(path: String, type: String): Single<ArrayList<FileInfo>> {
        return Single.defer {
            val fileInfoList = FileUtil.createFileInfoS(path)
            val data = ArrayList<FileInfo>()
            fileInfoList.filter {
                val suffix = FileUtil.getFileSuffix(it)
                if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_MUSIC) {
                    it.isDir
                            || suffix == YueTingConstant.PLAY_SUFFIX_MP3
                            || suffix == YueTingConstant.PLAY_SUFFIX_APE
                            || suffix == YueTingConstant.PLAY_SUFFIX_FLAC
                } else {
                    it.isDir
                            || suffix == YueTingConstant.READ_SUFFIX_TXT
                            || suffix == YueTingConstant.READ_SUFFIX_PDF
                }

            }.forEach {
                data.add(it)
            }
            Single.just(data).threadSwitch()
        }
    }

    fun insertFileInfo(data: ArrayList<FileInfo>, listTitle: String): Single<Boolean> {
        return Single.defer {
            for (i in 0 until data.size) {
                val suffix = FileUtil.getFileSuffix(data[i])
                if (suffix != YueTingConstant.PLAY_SUFFIX_MP3
                        && suffix != YueTingConstant.PLAY_SUFFIX_APE
                        && suffix != YueTingConstant.PLAY_SUFFIX_FLAC
                        && suffix != YueTingConstant.READ_SUFFIX_TXT
                        && suffix != YueTingConstant.READ_SUFFIX_PDF) continue
                val dateTime = DateFormatUtil.simpleDateFormat(System.currentTimeMillis())
                val tempData = data[i]
                val byteData = DataSerializationUtil.sequenceToByteArray(tempData)
                //(fileInfo.name.toLowerCase()).replace("\\.mp3$".toRegex(), "")
                val name = tempData.name.substring(0, tempData.name.lastIndexOf("."))
                if (suffix == YueTingConstant.PLAY_SUFFIX_APE
                        || suffix == YueTingConstant.PLAY_SUFFIX_MP3
                        || suffix == YueTingConstant.PLAY_SUFFIX_FLAC) {
                    val musicInfo = MusicInfo()
                    val mmr = MediaMetadataRetriever()
                    mmr.setDataSource(tempData.path)
                    musicInfo.albumUrl = tempData.path
                    musicInfo.title =
                            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                            ?: name
                    musicInfo.album =
                            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                            ?: YueTingConstant.INFO_UNKNOWN
                    musicInfo.artist =
                            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                            ?: YueTingConstant.INFO_UNKNOWN
                    musicInfo.duration =
                            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toInt()
                    musicInfo.picture = mmr.embeddedPicture
                    if (!ShareMusicInfo.MusicInfoTool.has(musicInfo)) {
                        ShareMusicInfo.MusicInfoTool.addInfo(musicInfo)
                    }
                    DBManager.sqlData(
                            DBManager.SqlFormat.insertSqlFormat(
                                    DataConstant.MUSIC_TABLE_NAME,
                                    arrayOf(DataConstant.COMMON_COLUMN_PATH,
                                            DataConstant.MUSIC_TABLE_C1_TYPE_NAME,
                                            DataConstant.MUSIC_TABLE_C2_FILE_INFO,
                                            DataConstant.COMMON_COLUMN_CREATE_TIME)),
                            arrayOf(tempData.path, listTitle, byteData, dateTime),
                            null,
                            DBManager.SqlType.INSERT)
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
                                            DataConstant.BOOK_TABLE_C5_TYPE_NAME,
                                            DataConstant.COMMON_COLUMN_CREATE_TIME)),
                            arrayOf(tempData.path, suffix, byteData, listTitle, dateTime),
                            null,
                            DBManager.SqlType.INSERT)
                    if (!ShareBookInfo.BookInfoTool.has(bookInfo)) {
                        ShareBookInfo.BookInfoTool.addInfo(bookInfo)
                    }
                }
            }
            val result = DBManager.finish()
            Single.just(result).threadSwitch()
        }
    }
}