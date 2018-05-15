package com.aqrlei.graduation.yueting.contract

import com.aqrlei.graduation.yueting.model.FileInfo
import com.aqrlei.graduation.yueting.model.FileSelectInfo
import com.aqrlei.graduation.yueting.mvp.BasePresenter
import com.aqrlei.graduation.yueting.mvp.BaseView

/**
 * @author  aqrLei on 2018/5/15
 */
interface FileContract {
    interface Presenter : BasePresenter {
        fun getFileInfo(path: String, type: String)
        fun addFileInfoToDB(type: String, data: ArrayList<FileSelectInfo>, listTitle: String)
    }

    interface View : BaseView {
        fun changeFileInfo(data: ArrayList<FileInfo>)
        fun finishActivity(result: Boolean, bookChange: Boolean, musicChange: Boolean)
    }
}