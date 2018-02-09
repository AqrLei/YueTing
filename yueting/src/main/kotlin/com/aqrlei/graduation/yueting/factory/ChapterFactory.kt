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
        fun init(bookInfo: BookInfo) {
            val chapter = ChapterInfo()
            chapter.apply {
                id = bookInfo.id
                name = bookInfo.name
                path = bookInfo.path
                createTime = bookInfo.createTime
                fileLength = bookInfo.fileLength
                encoding = bookInfo.encoding
                accessTime = bookInfo.accessTime

            }
            chapterList.clear()
            chapterList.add(chapter)
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
        if (chapterList.size > 1) {
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
            val isr = InputStreamReader(FileInputStream(File(chapterList[0].path))
                    , chapterList[0].encoding)
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
                            YueTingConstant.BOOK_TABLE_NAME,
                            arrayOf("catalog")), arrayOf(chapterList[i]),
                    null,
                    DBManager.SqlType.INSERT
            )
        }

    }

    private fun getChapterFromDB(): Boolean {
        val c = DBManager.sqlData(DBManager.SqlFormat.selectSqlFormat(YueTingConstant.BOOK_TABLE_NAME,
                "catalog"),
                null, null, DBManager.SqlType.SELECT)
                .getCursor()
        // isDone =( c?.moveToNext() == true)
        // c?.moveToPrevious()
        while (c?.moveToNext() == true) {
            try {
                val temp = DataSerializationUtil.byteArrayToSequence(
                        c.getBlob(c.getColumnIndex("catalog")))
                chapterList.clear()
                chapterList.add(temp as ChapterInfo)
            } catch (e: Exception) {
                e.printStackTrace()
                isDone = false
            }
        }
        // DBManager.releaseCursor()
        return isDone

    }
}