package com.aqrlei.graduation.yueting.model.observable

import com.aqrairsigns.aqrleilib.info.FileInfo
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.DataSerializationUtil
import com.aqrairsigns.aqrleilib.util.DateFormatUtil
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.factory.ChapterFactory
import com.aqrlei.graduation.yueting.model.BookInfo
import com.aqrlei.graduation.yueting.model.SelectInfo
import com.aqrlei.graduation.yueting.model.infotool.ShareBookInfo
import com.aqrlei.graduation.yueting.util.threadSwitch
import io.reactivex.Single
import java.io.File

/**
 * @author  aqrLei on 2018/5/2
 */
object BookSingle {

    fun selectChapters(): Single<Boolean> {
        return Single.defer {
            ChapterFactory.CHAPTER.getBookMarkFromDB()
            Single.just(ChapterFactory.CHAPTER.getChapter())
        }.threadSwitch()
    }

    fun insertMark(path: String, currentBegin: Int): Single<Boolean> {
        return Single.defer {
            val dateTime = DateFormatUtil.simpleDateFormat(System.currentTimeMillis())
            DBManager.sqlData(
                    DBManager.SqlFormat.insertSqlFormat(
                            DataConstant.MARK_TABLE_NAME,
                            arrayOf(DataConstant.COMMON_COLUMN_PATH,
                                    DataConstant.MARK_TABLE_C1_MARK_POSITION,
                                    DataConstant.COMMON_COLUMN_CREATE_TIME)),
                    arrayOf(path, currentBegin, dateTime),
                    null,
                    DBManager.SqlType.INSERT)
            Single.just(DBManager.finish())
        }.threadSwitch()
    }

    fun updateIndex(path: String, begin: Int, end: Int): Single<Boolean> {
        return Single.defer {
            if (ShareBookInfo.BookInfoTool.same(path)) {
                ShareBookInfo.BookInfoTool.setBookInfoIndex(path, begin, end)
            }
            DBManager.sqlData(
                    DBManager.SqlFormat.updateSqlFormat(
                            DataConstant.BOOK_TABLE_NAME,
                            DataConstant.BOOK_TABLE_C2_INDEX_BEGIN,
                            DataConstant.COMMON_COLUMN_PATH, "="),
                    arrayOf(begin, path),
                    null,
                    DBManager.SqlType.UPDATE)
            DBManager.sqlData(
                    DBManager.SqlFormat.updateSqlFormat(
                            DataConstant.BOOK_TABLE_NAME,
                            DataConstant.BOOK_TABLE_C2_INDEX_BEGIN,
                            DataConstant.COMMON_COLUMN_PATH, "="),
                    arrayOf(end, path),
                    null,
                    DBManager.SqlType.UPDATE)
            Single.just(DBManager.finish())
        }.threadSwitch()
    }

    fun updateTypeName(path: String, typeName: String): Single<Boolean> {
        return Single.defer {
            DBManager.sqlData(
                    DBManager.SqlFormat.updateSqlFormat(
                            DataConstant.BOOK_TABLE_NAME,
                            DataConstant.BOOK_TABLE_C5_TYPE_NAME,
                            DataConstant.COMMON_COLUMN_PATH, "="),
                    arrayOf(typeName, path),
                    null,
                    DBManager.SqlType.UPDATE)
            Single.just(DBManager.finish())
        }.threadSwitch()
    }

    fun selectBookInfo(typeName: String): Single<ArrayList<BookInfo>> {
        return Single.defer {
            val bookInfoList = ArrayList<BookInfo>()
            if (typeName == DataConstant.DEFAULT_TYPE_NAME) {
                DBManager.sqlData(
                        DBManager.SqlFormat.selectSqlFormat(
                                DataConstant.BOOK_TABLE_NAME),
                        null, null, DBManager.SqlType.SELECT)
                        .getCursor()
            } else {
                DBManager.sqlData(
                        DBManager.SqlFormat.selectSqlFormat(
                                DataConstant.BOOK_TABLE_NAME,
                                "",
                                DataConstant.BOOK_TABLE_C5_TYPE_NAME,
                                "="),
                        null, arrayOf(typeName), DBManager.SqlType.SELECT)
                        .getCursor()
            }?.let {
                while (it.moveToNext()) {
                    val bookInfo = BookInfo()
                    val fileInfo = DataSerializationUtil.byteArrayToSequence(it.getBlob(it.getColumnIndex(DataConstant.BOOK_TABLE_C4_FILE_INFO)))
                            as FileInfo
                    val name = fileInfo.name.substring(0, fileInfo.name.lastIndexOf("."))
                    val path = it.getString(it.getColumnIndex(DataConstant.COMMON_COLUMN_PATH))
                    bookInfo.id = it.getInt(it.getColumnIndex(DataConstant.COMMON_COLUMN_ID))
                    bookInfo.createTime = it.getString(it.getColumnIndex(DataConstant.COMMON_COLUMN_CREATE_TIME))
                    bookInfo.type = it.getString(it.getColumnIndex(DataConstant.BOOK_TABLE_C1_TYPE))
                    bookInfo.name = name
                    bookInfo.path = path ?: ""
                    bookInfo.fileLength = File(path).length().toInt()
                    bookInfo.indexBegin = it.getInt(it.getColumnIndex(DataConstant.BOOK_TABLE_C2_INDEX_BEGIN))
                    bookInfo.indexEnd = it.getInt(it.getColumnIndex(DataConstant.BOOK_TABLE_C3_INDEX_END))
                    bookInfoList.add(bookInfo)
                }
            }
            Single.just(bookInfoList)
        }.threadSwitch()
    }

    fun deleteBookInfo(pathList: List<SelectInfo>): Single<Boolean> {
        return Single.defer {
            pathList.filter { it.status == SelectInfo.SELECTED }
                    .forEach {
                        DBManager.sqlData(
                                DBManager.SqlFormat.deleteSqlFormat(
                                        DataConstant.BOOK_TABLE_NAME,
                                        DataConstant.COMMON_COLUMN_PATH,
                                        "="),
                                null,
                                arrayOf(it.name),
                                DBManager.SqlType.DELETE)
                    }
            Single.just(DBManager.finish())
        }.threadSwitch()
    }

    fun deleteBookInfo(path: String): Single<Boolean> {
        return Single.defer {
            DBManager.sqlData(
                    DBManager.SqlFormat.deleteSqlFormat(DataConstant.BOOK_TABLE_NAME,
                            DataConstant.COMMON_COLUMN_PATH, "="),
                    null, arrayOf(path), DBManager.SqlType.DELETE)
            DBManager.sqlData(
                    DBManager.SqlFormat.deleteSqlFormat(DataConstant.MARK_TABLE_NAME,
                            DataConstant.COMMON_COLUMN_PATH, "="),
                    null, arrayOf(path), DBManager.SqlType.DELETE)
            DBManager.sqlData(
                    DBManager.SqlFormat.deleteSqlFormat(DataConstant.CATALOG_TABLE_NAME,
                            DataConstant.COMMON_COLUMN_PATH, "="),
                    null, arrayOf(path), DBManager.SqlType.DELETE)
            Single.just(DBManager.finish())
        }.threadSwitch()
    }

    fun deleteBookInfoByList(typeNameList: List<SelectInfo>): Single<Boolean> {
        return Single.defer {
            typeNameList.filter { it.status == SelectInfo.SELECTED }
                    .forEach {
                        DBManager.sqlData(
                                DBManager.SqlFormat.deleteSqlFormat(
                                        DataConstant.BOOK_TABLE_NAME,
                                        DataConstant.BOOK_TABLE_C5_TYPE_NAME,
                                        "="),
                                null,
                                arrayOf(it.name),
                                DBManager.SqlType.DELETE)
                    }
            Single.just(DBManager.finish())
        }.threadSwitch()
    }
}