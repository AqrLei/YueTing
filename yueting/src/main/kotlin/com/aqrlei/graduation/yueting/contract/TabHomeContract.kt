package com.aqrlei.graduation.yueting.contract

import android.content.Context
import android.os.Messenger
import com.aqrlei.graduation.yueting.model.BookInfo
import com.aqrlei.graduation.yueting.model.MusicInfo
import com.aqrlei.graduation.yueting.model.SelectInfo
import com.aqrlei.graduation.yueting.mvp.BasePresenter
import com.aqrlei.graduation.yueting.mvp.BaseView

/**
 * @author  aqrLei on 2018/5/15
 */
interface TabHomeContract {
    interface Presenter : BasePresenter {

        fun fetchMusicInfo(typeName: String)
        fun fetchBookInfo(typeName: String)
        fun fetchTypeInfo(type: String)
        fun deleteMusicItem(path: String)
        fun deleteBookItem(path: String)
        fun updateMusicTypeName(path: String, typeName: String)
        fun updateBookTypeName(path: String, typeName: String)
        fun startMusicService(context: Context, messenger: Messenger, position: Int, typeName: String)
        fun generateListSelectInfo(type: String)
    }

    interface View : BaseView {
        fun setMusicInfo(musicInfo: ArrayList<MusicInfo>)
        fun setBookInfo(bookInfo: ArrayList<BookInfo>)
        fun setTypeInfo(typeInfo: ArrayList<String>)
        fun finish(result: Boolean, msg: String)
        fun setGenerate(generateInfo: ArrayList<SelectInfo>)
    }
}