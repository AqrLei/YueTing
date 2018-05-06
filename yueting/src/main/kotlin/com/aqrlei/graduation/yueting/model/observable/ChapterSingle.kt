package com.aqrlei.graduation.yueting.model.observable

import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.ChapterInfo
import com.aqrlei.graduation.yueting.util.BookPageLoader
import com.aqrlei.graduation.yueting.util.ChapterLoader
import com.aqrlei.graduation.yueting.util.threadSwitch
import io.reactivex.Single
import java.nio.charset.Charset
import java.util.regex.Pattern

/**
 * created by AqrLei at 16:54 on 星期日, 五月 06, 2018
 */
object ChapterSingle {

    fun selectChapters(): Single<Boolean> {
        return Single.defer {
            ChapterLoader.CHAPTER.getBookMark()
            Single.just(ChapterLoader.CHAPTER.getChapter())
        }.threadSwitch()
    }

    /**
     * in ChapterLoader getChapterFromDB
     */
    fun selectChapter(chapterBuffer: ChapterInfo, chapterList: ArrayList<ChapterInfo>): Single<Boolean> {
        return Single.defer {
            val isDone = DBManager.sqlData(DBManager.SqlFormat.selectSqlFormat(DataConstant.CATALOG_TABLE_NAME,
                    "", DataConstant.COMMON_COLUMN_PATH, "="),
                    null, arrayOf(chapterBuffer.path), DBManager.SqlType.SELECT)
                    .getCursor()?.let {
                        var temp = false
                        while (it.moveToNext()) {
                            temp = true
                            val chapterTemp = ChapterInfo()
                            chapterTemp.chapterName = it.getString(it.getColumnIndex(DataConstant.CATALOG_TABLE_C1_CATALOG_NAME))
                            chapterTemp.bPosition = it.getInt(it.getColumnIndex(DataConstant.CATALOG_TABLE_C2_CATALOG_POSITION))
                            chapterList.add(chapterTemp)
                        }
                        it.close()
                        temp
                    } ?: false
            Single.just(isDone)
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
    fun insertChapter(chapterBuffer: ChapterInfo, chapterList: ArrayList<ChapterInfo>): Single<Boolean> {
        return Single.defer {
            var position = 0
            var isDone = true
            while (position < chapterBuffer.fileLength) {
                val bookByteArray = BookPageLoader.BOOKPAGEFACTORY.getBookByteArray(position)
                position += bookByteArray.size
                try {
                    val strLine = String(bookByteArray, Charset.forName(chapterBuffer.encoding))
                    val p = Pattern.compile(YueTingConstant.CHAPTER_KEY_WORD)
                    if (p.matcher(strLine).find()) {
                        val chapterInfo = ChapterInfo()
                        chapterInfo.chapterName = strLine
                        chapterInfo.bPosition = position - bookByteArray.size
                        DBManager.sqlData(
                                DBManager.SqlFormat.insertSqlFormat(
                                        DataConstant.CATALOG_TABLE_NAME,
                                        arrayOf(
                                                DataConstant.COMMON_COLUMN_PATH,
                                                DataConstant.CATALOG_TABLE_C1_CATALOG_NAME,
                                                DataConstant.CATALOG_TABLE_C2_CATALOG_POSITION)),
                                arrayOf(chapterBuffer.path, chapterInfo.chapterName, chapterInfo.bPosition),
                                null,
                                DBManager.SqlType.INSERT
                        )
                        isDone = DBManager.finish()
                        chapterList.add(chapterInfo)
                    }
                } catch (e: Exception) {
                    isDone = false
                    e.printStackTrace()
                }
            }
            Single.just(isDone)
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
}