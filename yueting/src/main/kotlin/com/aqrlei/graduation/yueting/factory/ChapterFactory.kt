package com.aqrlei.graduation.yueting.factory

import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.BookInfo
import com.aqrlei.graduation.yueting.model.ChapterInfo
import java.nio.charset.Charset
import java.util.regex.Pattern

/**
 * Author : AqrLei
 * Date : 2018/2/7.
 */
enum class ChapterFactory {
    CHAPTER;

    companion object {
        private var chapterList = ArrayList<ChapterInfo>()
        private var bookMarkList = ArrayList<ChapterInfo>()
        private var chapterBuffer = ChapterInfo()
        fun init(bookInfo: BookInfo) {
            chapterBuffer.apply {
                id = bookInfo.id
                name = bookInfo.name
                path = bookInfo.path
                type = bookInfo.type
                createTime = bookInfo.createTime
                fileLength = bookInfo.fileLength
                encoding = bookInfo.encoding
                accessTime = bookInfo.accessTime
            }
        }
    }

    private var isDone: Boolean = true
    fun getChapters() = chapterList
    fun getBookMarks() = bookMarkList
    fun clearAllDatas() {
        chapterList.clear()
        bookMarkList.clear()
    }

    fun removeBookMark(position: Int) {
        val markInfo = bookMarkList.removeAt(position)
        deleteFromDB(markInfo.bPosition)
        if (DBManager.finish()) {
            getBookMarkFromDB()
        }
    }

    fun getChapter(): Boolean {
        if (chapterBuffer.type == "pdf") {
            return isDone
        }
        if (chapterList.size > 0) {
            return isDone
        }
        return if (getChapterFromDB()) {
            isDone
        } else {
            getChapterFromBook()
        }

    }

    fun getBookMarkFromDB(): Boolean {
        var haveMark = false
        bookMarkList.clear()//书签数可能改变，故每次都要清空后重新从数据库获取，章节数固定不变故不必
        val c = DBManager.sqlData(
                DBManager.SqlFormat.selectSqlFormat(DataConstant.MARK_TABLE_NAME,
                        "", DataConstant.COMMON_COLUMN_PATH, "="),
                null, arrayOf(chapterBuffer.path), DBManager.SqlType.SELECT)
                .getCursor()
        while (c?.moveToNext() == true) {
            haveMark = true
            val markInfo = ChapterInfo()
            val tempP = c.getInt(c.getColumnIndex(DataConstant.MARK_TABLE_C1_MARK_POSITION))
            val tempName = if (chapterBuffer.type == "txt") {
                String(
                        BookPageFactory.BOOKPAGEFACTORY.getBookByteArray(tempP),
                        Charset.forName(chapterBuffer.encoding))
            } else {
                chapterBuffer.name + tempP
            }
            markInfo.bPosition = tempP
            markInfo.chapterName = tempName
            markInfo.createTime = c.getString(c.getColumnIndex("createTime"))
            markInfo.flag = false
            bookMarkList.add(markInfo)
        }
        return haveMark
    }

    private fun deleteFromDB(bPosition: Int) {
        DBManager.sqlData(
                DBManager.SqlFormat.deleteSqlFormat(DataConstant.MARK_TABLE_NAME,
                        DataConstant.MARK_TABLE_C1_MARK_POSITION, "="),
                null, arrayOf(bPosition.toString()), DBManager.SqlType.DELETE)
    }

    private fun getChapterFromBook(): Boolean {//需要在线程中执行
        var position = 0
        while (position < chapterBuffer.fileLength) {
            val bookByteArray = BookPageFactory.BOOKPAGEFACTORY.getBookByteArray(position)
            position += bookByteArray.size
            try {
                val strLine = String(bookByteArray, Charset.forName(chapterBuffer.encoding))
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
        addChapterToDB()

        return isDone
    }

    private fun addChapterToDB() {
        for (i in 0 until chapterList.size) {
            DBManager.sqlData(
                    DBManager.SqlFormat.insertSqlFormat(
                            DataConstant.CATALOG_TABLE_NAME,
                            arrayOf(
                                    DataConstant.COMMON_COLUMN_PATH,
                                    DataConstant.CATALOG_TABLE_C1_CATALOG_NAME,
                                    DataConstant.CATALOG_TABLE_C2_CATALOG_POSITION
                            )
                    ),
                    arrayOf(chapterBuffer.path, chapterList[i].chapterName, chapterList[i].bPosition),
                    null,
                    DBManager.SqlType.INSERT
            )
        }
    }

    private fun getChapterFromDB(): Boolean {
        val c = DBManager.sqlData(DBManager.SqlFormat.selectSqlFormat(DataConstant.CATALOG_TABLE_NAME,
                "", DataConstant.COMMON_COLUMN_PATH, "="),
                null, arrayOf(chapterBuffer.path), DBManager.SqlType.SELECT)
                .getCursor()
        isDone = (c?.moveToNext() == true)
        c?.moveToPrevious()
        while (c?.moveToNext() == true) {
            try {
                val chapterTemp = ChapterInfo()
                chapterTemp.chapterName = c.getString(c.getColumnIndex(DataConstant.CATALOG_TABLE_C1_CATALOG_NAME))
                chapterTemp.bPosition = c.getInt(c.getColumnIndex(DataConstant.CATALOG_TABLE_C2_CATALOG_POSITION))
                chapterList.add(chapterTemp)
            } catch (e: Exception) {
                e.printStackTrace()
                isDone = false
            }
        }
        // DBManager.releaseCursor()
        return isDone

    }
}