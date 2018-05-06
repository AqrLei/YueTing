package com.aqrlei.graduation.yueting.util

import com.aqrlei.graduation.yueting.model.BookInfo
import com.aqrlei.graduation.yueting.model.ChapterInfo
import com.aqrlei.graduation.yueting.model.observable.ChapterSingle

/**
 * Author : AqrLei
 * Date : 2018/2/7.
 */
enum class ChapterLoader {
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
        if (deleteFromDB(markInfo.bPosition)) {
            getBookMark()
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

    fun getBookMark() {
        val disposable =  ChapterSingle.selectBookMark(bookMarkList, chapterBuffer)
                .subscribe()
        DisposableHolder.addDisposable(disposable)
    }

    private fun deleteFromDB(bPosition: Int): Boolean {
        var flag = false
        val disposable =ChapterSingle.deleteMark(bPosition).subscribe({
            flag = it
        }, {})
        DisposableHolder.addDisposable(disposable)
        return flag
    }

    private fun getChapterFromBook(): Boolean {
        val disposable = ChapterSingle.insertChapter(chapterBuffer, chapterList)
                .subscribe({
                    isDone = it
                }, {})
        DisposableHolder.addDisposable(disposable)
        return isDone
    }

    private fun getChapterFromDB(): Boolean {
        val disposable=ChapterSingle.selectChapter(chapterBuffer, chapterList)
                .subscribe({
                    isDone = it
                }, {})
        DisposableHolder.addDisposable(disposable)
        return isDone
    }
}