package com.aqrlei.graduation.yueting.presenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.info.FileInfo
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.FileSelectInfo
import com.aqrlei.graduation.yueting.model.infotool.ShareBookInfo
import com.aqrlei.graduation.yueting.model.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.model.observable.FileSingle
import com.aqrlei.graduation.yueting.ui.FileActivity

/**
 * Author: AqrLei
 * CreateTime: Date: 2017/9/11 Time: 13:41
 */
class FilePresenter(mMvpActivity: FileActivity) :
        MvpContract.ActivityPresenter<FileActivity>(mMvpActivity) {

    fun getFileInfo(path: String, type: String) {
        val disposables =
                FileSingle.createFileInfo(path, type)
                        .subscribe({
                            mMvpActivity.changeFileInfo(it)
                        }, {})
        addDisposables(disposables)
    }

    fun addFileInfoToDB(type:String, data: ArrayList<FileSelectInfo>, listTitle: String) {
        val flag = type == YueTingConstant.FRAGMENT_TITLE_TYPE_MUSIC
        val disposables =
                FileSingle.insertFileInfo(data, listTitle)
                        .subscribe({
                            mMvpActivity.finishActivity(it,
                                    !flag,flag)
                        }, {})
        addDisposables(disposables)
    }
}