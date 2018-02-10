package com.aqrlei.graduation.yueting.factory

import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.DataSerializationUtil
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.BookInfo
import com.aqrlei.graduation.yueting.model.local.ChapterInfo
import java.io.*
import java.nio.charset.Charset

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
    fun getChapters(): ArrayList<ChapterInfo> {
        val chapterInfoS = ArrayList<ChapterInfo>()
        chapterInfoS.addAll(chapterList)
        chapterInfoS.removeAt(0)
        return chapterInfoS
    }

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

        try {
            val isr = InputStreamReader(FileInputStream(File(chapterBuffer.path))
                    , chapterBuffer.encoding)
            val reader = BufferedReader(isr)
            var temp = ""
            var bPosition = 0
            while ((temp.apply { temp = reader.readLine() ?: " " }) != " ") {
                val length = temp.toByteArray(Charset.forName(chapterList[0].encoding)).size
                bPosition += length
                if (temp.contains(YueTingConstant.CHAPTER_KEY_WORD[0])
                        && (temp.contains(YueTingConstant.CHAPTER_KEY_WORD[1])
                                || temp.contains(YueTingConstant.CHAPTER_KEY_WORD[2])
                                || temp.contains(YueTingConstant.CHAPTER_KEY_WORD[3]))) {
                    val chapterInfo = ChapterInfo()
                    chapterInfo.chapterName = temp
                    chapterInfo.bPosition = bPosition - length
                    chapterList.add(chapterInfo)
                }
            }

        } catch (f: FileNotFoundException) {
            f.printStackTrace()
            isDone = false

        } catch (e: IOException) {
            e.printStackTrace()
            isDone = false
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
        // isDone =( c?.moveToNext() == true)
        // c?.moveToPrevious()
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