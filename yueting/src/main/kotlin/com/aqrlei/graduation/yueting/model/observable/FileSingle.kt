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
import com.aqrlei.graduation.yueting.model.FileSelectInfo
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

    fun insertFileInfo(data: ArrayList<FileSelectInfo>, listTitle: String): Single<Boolean> {
        return Single.defer {
            data.filter { it.status == FileSelectInfo.SELECTED }
                    .forEach {
                        val suffix = FileUtil.getFileSuffix(it.fileInfo)
                        if (suffix != YueTingConstant.PLAY_SUFFIX_MP3
                                && suffix != YueTingConstant.PLAY_SUFFIX_APE
                                && suffix != YueTingConstant.PLAY_SUFFIX_FLAC
                                && suffix != YueTingConstant.READ_SUFFIX_TXT
                                && suffix != YueTingConstant.READ_SUFFIX_PDF) return@forEach
                        val dateTime = DateFormatUtil.simpleDateFormat(System.currentTimeMillis())
                        val tempData = it.fileInfo
                        val byteData = DataSerializationUtil.sequenceToByteArray(tempData)
                        //(fileInfo.name.toLowerCase()).replace("\\.mp3$".toRegex(), "")
                        if (suffix == YueTingConstant.PLAY_SUFFIX_APE
                                || suffix == YueTingConstant.PLAY_SUFFIX_MP3
                                || suffix == YueTingConstant.PLAY_SUFFIX_FLAC) {
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
                        }
                    }
            val result = DBManager.finish()
            Single.just(result).threadSwitch()
        }
    }
}