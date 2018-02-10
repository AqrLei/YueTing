package com.aqrlei.graduation.yueting.factory

import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.ReaderUtil
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.BookInfo
import com.aqrlei.graduation.yueting.model.local.ChapterInfo
import java.nio.charset.Charset
import java.util.regex.Pattern

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2018/2/7.
 */
enum class ChapterFactory {
    CHAPTER;

    companion object {
        private var chapterList = ArrayList<ChapterInfo>()
        private var chapterBuffer = ChapterInfo()
        fun init(bookInfo: BookInfo) {

            chapterBuffer.apply {
                id = bookInfo.id
                name = bookInfo.name
                path = bookInfo.path
                createTime = bookInfo.createTime
                fileLength = bookInfo.fileLength
                encoding = bookInfo.encoding
                accessTime = bookInfo.accessTime

            }
        }
    }

    private var isDone: Boolean = true
    fun getChapters() = chapterList


    fun getChapter(): Boolean {
        if (chapterList.size > 0) {
            return isDone
        }
        return if (getChapterFromDB()) {
            isDone
        } else {
            getChapterFromBook()
        }

    }

    private fun getChapterFromBook(): Boolean {//需要在线程中执行
        var position = 0
        while (position < chapterBuffer.fileLength) {
            val bookByteArray = PageFactory.PAGEFACTORY.getBookByteArray(position)
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
                            YueTingConstant.CATALOG_TABLE_NAME,
                            YueTingConstant.CATALOG_TABLE_C),
                    arrayOf(chapterBuffer.path, chapterList[i].chapterName, chapterList[i].bPosition),
                    null,
                    DBManager.SqlType.INSERT
            )
        }

    }

    private fun getChapterFromDB(): Boolean {
        val c = DBManager.sqlData(DBManager.SqlFormat.selectSqlFormat(YueTingConstant.CATALOG_TABLE_NAME,
                "", YueTingConstant.CATALOG_TABLE_C[0], "="),
                null, arrayOf(chapterBuffer.path), DBManager.SqlType.SELECT)
                .getCursor()
        isDone = (c?.moveToNext() == true)
        c?.moveToPrevious()
        while (c?.moveToNext() == true) {
            try {
                val chapterTemp = ChapterInfo()
                chapterTemp.chapterName = c.getString(c.getColumnIndex(YueTingConstant.CATALOG_TABLE_C[1]))
                chapterTemp.bPosition = c.getInt(c.getColumnIndex(YueTingConstant.CATALOG_TABLE_C[2]))
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