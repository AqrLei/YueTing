package com.aqrlei.graduation.yueting.model.observable

import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.DateFormatUtil
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.model.BookInfo
import com.aqrlei.graduation.yueting.model.SelectInfo
import com.aqrlei.graduation.yueting.model.infotool.ShareBookInfo
import com.aqrlei.graduation.yueting.util.generateBookInfo
import com.aqrlei.graduation.yueting.util.threadSwitch
import io.reactivex.Single

/**
 * @author  aqrLei on 2018/5/2
 */
object BookSingle {

    fun selectIndex(path: String): Single<IntArray> {
        return Single.defer {
            val temp = IntArray(2)
            DBManager.sqlData(DBManager.SqlFormat.selectSqlFormat(DataConstant.BOOK_TABLE_NAME,
                    "${DataConstant.BOOK_TABLE_C2_INDEX_BEGIN}, ${DataConstant.BOOK_TABLE_C3_INDEX_END}",
                    DataConstant.COMMON_COLUMN_PATH,
                    "="),
                    null, arrayOf(path), DBManager.SqlType.SELECT)
                    .getCursor()?.let {
                        while (it.moveToNext()) {
                            val begin = it.getInt(it.getColumnIndex(DataConstant.BOOK_TABLE_C2_INDEX_BEGIN))
                            val end = it.getInt(it.getColumnIndex(DataConstant.BOOK_TABLE_C3_INDEX_END))
                            temp[0] = begin
                            temp[1] = end
                        }
                    }

            Single.just(temp)
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
                                DataConstant.BOOK_TABLE_C4_TYPE_NAME,
                                "="),
                        null, arrayOf(typeName), DBManager.SqlType.SELECT)
                        .getCursor()
            }?.let {
                while (it.moveToNext()) {
                    val path = it.getString(it.getColumnIndex(DataConstant.COMMON_COLUMN_PATH))
                    val id = it.getInt(it.getColumnIndex(DataConstant.COMMON_COLUMN_ID))
                    val createTime = it.getString(it.getColumnIndex(DataConstant.COMMON_COLUMN_CREATE_TIME))
                    val type = it.getString(it.getColumnIndex(DataConstant.BOOK_TABLE_C1_TYPE))
                    val begin = it.getInt(it.getColumnIndex(DataConstant.BOOK_TABLE_C2_INDEX_BEGIN))
                    val end = it.getInt(it.getColumnIndex(DataConstant.BOOK_TABLE_C3_INDEX_END))
                    val bookInfo = generateBookInfo(path, id, createTime, type, begin, end)
                    bookInfoList.add(bookInfo)
                }
                it.close()
            }
            Single.just(bookInfoList)
        }.threadSwitch()
    }

    fun selectBookPath(typeNameList: List<SelectInfo>): Single<List<String>> {
        return Single.defer {
            val temp = ArrayList<String>()
            typeNameList.filter { it.status == SelectInfo.SELECTED }
                    .forEach {
                        DBManager.sqlData(
                                DBManager.SqlFormat.selectSqlFormat(
                                        DataConstant.BOOK_TABLE_NAME,
                                        "",
                                        DataConstant.BOOK_TABLE_C4_TYPE_NAME,
                                        "="),
                                null, arrayOf(it.name), DBManager.SqlType.SELECT)
                                .getCursor()?.let {
                                    while (it.moveToNext()) {
                                        val path = it.getString(it.getColumnIndex(DataConstant.COMMON_COLUMN_PATH))
                                        temp.add(path)
                                    }
                                    it.close()
                                }
                    }
            Single.just(temp.toList())
        }
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
                            DataConstant.BOOK_TABLE_C3_INDEX_END,
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
                            DataConstant.BOOK_TABLE_C4_TYPE_NAME,
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
                            DataConstant.BOOK_TABLE_NAME,
                            DataConstant.BOOK_TABLE_C4_TYPE_NAME,
                            DataConstant.BOOK_TABLE_C4_TYPE_NAME, "="),
                    arrayOf(newTypeName, oldTypeName),
                    null,
                    DBManager.SqlType.UPDATE)
            Single.just(DBManager.finish())
        }.threadSwitch()
    }

    fun deleteBookInfo(pathList: List<SelectInfo>): Single<List<String>> {
        return Single.defer {
            val temp = ArrayList<String>()
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
                        if (DBManager.finish()) {
                            temp.add(it.name)
                        }
                    }
            Single.just(temp.toList())
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
                                        DataConstant.BOOK_TABLE_C4_TYPE_NAME,
                                        "="),
                                null,
                                arrayOf(it.name),
                                DBManager.SqlType.DELETE)
                    }
            Single.just(DBManager.finish())
        }.threadSwitch()
    }


}