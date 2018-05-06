package com.aqrlei.graduation.yueting.model.observable

import android.content.ContentResolver
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrlei.graduation.yueting.model.MusicInfo
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.model.SelectInfo
import com.aqrlei.graduation.yueting.provider.MusicProvider
import com.aqrlei.graduation.yueting.util.generateMusicInfo
import com.aqrlei.graduation.yueting.util.threadSwitch
import io.reactivex.Single

/**
 * @author  aqrLei on 2018/5/2
 */
object MusicSingle {

    fun queryMusicInfo(typeName: String, contentResolver: ContentResolver): Single<List<MusicInfo>> {
        return Single.defer {
            val musicInfoList = ArrayList<MusicInfo>()
            if (typeName == DataConstant.DEFAULT_TYPE_NAME) {
                contentResolver.query(MusicProvider.MUSIC_CONTENT_URI,
                        null,
                        null,
                        null,
                        null)
            } else {
                contentResolver.query(
                        MusicProvider.MUSIC_CONTENT_URI,
                        null,
                        DataConstant.MUSIC_TABLE_C1_TYPE_NAME,
                        arrayOf(typeName),
                        null)
            }?.let {
                while (it.moveToNext()) {
                    val path = it.getString(it.getColumnIndex(DataConstant.COMMON_COLUMN_PATH))
                    val id = it.getInt(it.getColumnIndex(DataConstant.COMMON_COLUMN_ID))
                    val createTime = it.getString(it.getColumnIndex(DataConstant.COMMON_COLUMN_CREATE_TIME))
                    val musicInfo = generateMusicInfo(path, id, createTime)
                    musicInfoList.add(musicInfo)
                }
                it.close()
            }
            Single.just(musicInfoList.toList())
        }.threadSwitch()
    }

    fun selectMusicInfo(typeName: String): Single<ArrayList<MusicInfo>> {
        return Single.defer {
            val musicInfoList = ArrayList<MusicInfo>()
            if (typeName == DataConstant.DEFAULT_TYPE_NAME) {
                DBManager.sqlData(
                        DBManager.SqlFormat.selectSqlFormat(
                                DataConstant.MUSIC_TABLE_NAME),
                        null, null, DBManager.SqlType.SELECT)
                        .getCursor()
            } else {
                DBManager.sqlData(
                        DBManager.SqlFormat.selectSqlFormat(
                                DataConstant.MUSIC_TABLE_NAME,
                                "",
                                DataConstant.MUSIC_TABLE_C1_TYPE_NAME,
                                "="),
                        null, arrayOf(typeName), DBManager.SqlType.SELECT)
                        .getCursor()
            }?.let {
                while (it.moveToNext()) {
                    val path = it.getString(it.getColumnIndex(DataConstant.COMMON_COLUMN_PATH))
                    val id = it.getInt(it.getColumnIndex(DataConstant.COMMON_COLUMN_ID))
                    val createTime = it.getString(it.getColumnIndex(DataConstant.COMMON_COLUMN_CREATE_TIME))
                    val musicInfo = generateMusicInfo(path, id, createTime)
                    musicInfoList.add(musicInfo)
                }
                it.close()
            }
            Single.just(musicInfoList)
        }.threadSwitch()
    }

    fun updateTypeName(path: String, typeName: String): Single<Boolean> {
        return Single.defer {
            DBManager.sqlData(
                    DBManager.SqlFormat.updateSqlFormat(
                            DataConstant.MUSIC_TABLE_NAME,
                            DataConstant.MUSIC_TABLE_C1_TYPE_NAME,
                            DataConstant.COMMON_COLUMN_PATH, "="),
                    arrayOf(typeName, path),
                    null,
                    DBManager.SqlType.UPDATE)
            Single.just(DBManager.finish())
        }.threadSwitch()
    }

    fun updateTypeNameList(oldTypeName: String, newTypeName: String): Single<Boolean> {
        return Single.defer {
            DBManager.sqlData(
                    DBManager.SqlFormat.updateSqlFormat(
                            DataConstant.MUSIC_TABLE_NAME,
                            DataConstant.MUSIC_TABLE_C1_TYPE_NAME,
                            DataConstant.MUSIC_TABLE_C1_TYPE_NAME, "="),
                    arrayOf(newTypeName, oldTypeName),
                    null,
                    DBManager.SqlType.UPDATE)
            Single.just(DBManager.finish())
        }.threadSwitch()
    }

    fun deleteMusicInfo(pathList: List<SelectInfo>): Single<Boolean> {
        return Single.defer {
            pathList.filter { it.status == SelectInfo.SELECTED }
                    .forEach {
                        DBManager.sqlData(
                                DBManager.SqlFormat.deleteSqlFormat(
                                        DataConstant.MUSIC_TABLE_NAME,
                                        DataConstant.COMMON_COLUMN_PATH,
                                        "="),
                                null,
                                arrayOf(it.name),
                                DBManager.SqlType.DELETE)
                    }
            Single.just(DBManager.finish())
        }.threadSwitch()
    }

    fun deleteMusicInfo(path: String): Single<Boolean> {
        return Single.defer {
            DBManager.sqlData(
                    DBManager.SqlFormat.deleteSqlFormat(
                            DataConstant.MUSIC_TABLE_NAME,
                            DataConstant.COMMON_COLUMN_PATH,
                            "="),
                    null,
                    arrayOf(path),
                    DBManager.SqlType.DELETE)
            Single.just(DBManager.finish())
        }.threadSwitch()
    }

    fun deleteMusicInfoByList(typeNameList: List<SelectInfo>): Single<Boolean> {
        return Single.defer {
            typeNameList.filter { it.status == SelectInfo.SELECTED }
                    .forEach {
                        DBManager.sqlData(
                                DBManager.SqlFormat.deleteSqlFormat(
                                        DataConstant.MUSIC_TABLE_NAME,
                                        DataConstant.MUSIC_TABLE_C1_TYPE_NAME,
                                        "="),
                                null,
                                arrayOf(it.name),
                                DBManager.SqlType.DELETE)
                    }
            Single.just(DBManager.finish())
        }.threadSwitch()
    }
}
