package com.aqrlei.graduation.yueting.model.observable

import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.ChapterInfo
import com.aqrlei.graduation.yueting.util.BookPageLoader
import com.aqrlei.graduation.yueting.util.threadSwitch
import io.reactivex.Single
import java.nio.charset.Charset
import java.util.regex.Pattern

/**
 * created by AqrLei at 16:54 on 星期日, 五月 06, 2018
 */
object ChapterSingle {

    /**
     * in ChapterLoader getChapterFromDB
     */
    fun selectChapter(path: String, chapterList: ArrayList<ChapterInfo>): Single<Boolean> {
        return Single.defer {
            chapterList.clear()
            DBManager.sqlData(DBManager.SqlFormat.selectSqlFormat(DataConstant.CATALOG_TABLE_NAME,
                    "", DataConstant.COMMON_COLUMN_PATH, "="),
                    null, arrayOf(path), DBManager.SqlType.SELECT)
                    .getCursor()?.let {
                        while (it.moveToNext()) {
                            val chapterTemp = ChapterInfo()
                            chapterTemp.chapterName = it.getString(it.getColumnIndex(DataConstant.CATALOG_TABLE_C1_CATALOG_NAME))
                            chapterTemp.bPosition = it.getInt(it.getColumnIndex(DataConstant.CATALOG_TABLE_C2_CATALOG_POSITION))
                            chapterList.add(chapterTemp)
                        }
                        it.close()
                    }

            Single.just(chapterList.isNotEmpty())
        }.threadSwitch()
    }

    /**
     * in ChapterLoader getBookMark
     */
    fun selectBookMark(bookMarkList: ArrayList<ChapterInfo>, chapterBuffer: ChapterInfo): Single<Boolean> {
        return Single.defer {
            bookMarkList.clear()//书签数可能改变，故每次都要清空后重新从数据库获取，章节数固定不变故不必
            DBManager.sqlData(
                    DBManager.SqlFormat.selectSqlFormat(DataConstant.MARK_TABLE_NAME,
                            "", DataConstant.COMMON_COLUMN_PATH, "="),
                    null, arrayOf(chapterBuffer.path), DBManager.SqlType.SELECT)
                    .getCursor()?.let {
                        while (it.moveToNext()) {
                            val markInfo = ChapterInfo()
                            val tempP = it.getInt(it.getColumnIndex(DataConstant.MARK_TABLE_C1_MARK_POSITION))
                            val tempName = if (chapterBuffer.type == "txt") {
                                String(
                                        BookPageLoader.BOOKPAGEFACTORY.getBookByteArray(tempP),
                                        Charset.forName(chapterBuffer.encoding))
                            } else {
                                chapterBuffer.name + tempP
                            }
                            markInfo.bPosition = tempP
                            markInfo.chapterName = tempName
                            markInfo.createTime = it.getString(it.getColumnIndex("createTime"))
                            markInfo.flag = false
                            bookMarkList.add(markInfo)
                        }
                        it.close()
                    }
            Single.just(DBManager.finish())
        }.threadSwitch()
    }

    /**
     * getChapterFromBook and addDataToDB
     */

    fun insertChapter(path: String, chapterList: ArrayList<ChapterInfo>): Single<Boolean> {
        return Single.defer {
            chapterList.forEach {
                DBManager.sqlData(
                        DBManager.SqlFormat.insertSqlFormat(
                                DataConstant.CATALOG_TABLE_NAME,
                                arrayOf(
                                        DataConstant.COMMON_COLUMN_PATH,
                                        DataConstant.CATALOG_TABLE_C1_CATALOG_NAME,
                                        DataConstant.CATALOG_TABLE_C2_CATALOG_POSITION)),
                        arrayOf(path, it.chapterName, it.bPosition),
                        null,
                        DBManager.SqlType.INSERT)
            }
            Single.just(DBManager.finish())
        }.threadSwitch()
    }

    fun deleteMark(position: Int): Single<Boolean> {
        return Single.defer {
            DBManager.sqlData(
                    DBManager.SqlFormat.deleteSqlFormat(
                            DataConstant.MARK_TABLE_NAME,
                            DataConstant.MARK_TABLE_C1_MARK_POSITION,
                            "="),
                    null,
                    arrayOf(position.toString()),
                    DBManager.SqlType.DELETE)
            Single.just(DBManager.finish())
        }.threadSwitch()
    }

    fun deleteMark(pathList: List<String>): Single<Boolean> {
        return Single.defer {
            pathList.forEach {
                DBManager.sqlData(
                        DBManager.SqlFormat.deleteSqlFormat(
                                DataConstant.MARK_TABLE_NAME,
                                DataConstant.COMMON_COLUMN_PATH,
                                "="),
                        null,
                        arrayOf(it),
                        DBManager.SqlType.DELETE)
            }
            Single.just(DBManager.finish())
        }.threadSwitch()
    }

    fun deleteChapters(pathList: List<String>): Single<Boolean> {
        return Single.defer {
            pathList.forEach {
                DBManager.sqlData(
                        DBManager.SqlFormat.deleteSqlFormat(
                                DataConstant.CATALOG_TABLE_NAME,
                                DataConstant.COMMON_COLUMN_PATH,
                                "="),
                        null,
                        arrayOf(it),
                        DBManager.SqlType.DELETE)
            }
            Single.just(DBManager.finish())
        }.threadSwitch()
    }

    fun generateChapter(fileLength: Int, encoding: String): Single<ArrayList<ChapterInfo>> {
        return Single.defer {
            val chapterList = ArrayList<ChapterInfo>()
            var position = 0
            while (position < fileLength) {
                val bookByteArray = BookPageLoader.BOOKPAGEFACTORY.getBookByteArray(position)
                position += bookByteArray.size
                try {
                    val strLine = String(bookByteArray, Charset.forName(encoding))
                    val p = Pattern.compile(YueTingConstant.CHAPTER_KEY_WORD)
                    if (p.matcher(strLine).find()) {
                        val chapterInfo = ChapterInfo()
                        chapterInfo.chapterName = strLine
                        chapterInfo.bPosition = position - bookByteArray.size
                        chapterList.add(chapterInfo)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            Single.just(chapterList)
        }.threadSwitch()
    }

}