package com.aqrlei.graduation.yueting.factory

import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.BookInfo
import com.aqrlei.graduation.yueting.model.local.ChapterInfo
import java.io.*

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
        if (getChapterFromDB()) {

        } else {
            getChapterFromBook()
        }
        return isDone
    }

    private fun getChapterFromBook(): Boolean {//需要在线程中执行

        try {
            val isr = InputStreamReader(FileInputStream(File(chapterList[0].path))
                    , chapterList[0].encoding)
            val reader = BufferedReader(isr)
            var temp = ""
            while ((temp.apply { temp = reader.readLine() ?: " " }) != " ") {
                if (temp.contains(YueTingConstant.CHAPTER_KEY_WORD[0])
                        && (temp.contains(YueTingConstant.CHAPTER_KEY_WORD[1])
                                || temp.contains(YueTingConstant.CHAPTER_KEY_WORD[2])
                                || temp.contains(YueTingConstant.CHAPTER_KEY_WORD[3]))) {
                    val chapterInfo = ChapterInfo()
                    chapterInfo.chapterName = temp
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

    }

    private fun getChapterFromDB(): Boolean {
        return false

    }
}