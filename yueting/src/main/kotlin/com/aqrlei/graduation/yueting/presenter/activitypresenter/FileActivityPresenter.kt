package com.aqrlei.graduation.yueting.presenter.activitypresenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.info.FileInfo
import com.aqrlei.graduation.yueting.model.infotool.ShareBookInfo
import com.aqrlei.graduation.yueting.model.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.model.observable.FileSingle
import com.aqrlei.graduation.yueting.ui.FileActivity

/**
 * Author: AqrLei
 * CreateTime: Date: 2017/9/11 Time: 13:41
 */
class FileActivityPresenter(mMvpActivity: FileActivity) :
        MvpContract.ActivityPresenter<FileActivity>(mMvpActivity) {

    fun getFileInfo(path: String, type: String) {
        val disposables =
                FileSingle.createFileInfo(path, type)
                        .subscribe({
                            mMvpActivity.changeFileInfo(it)
                        }, {})
        addDisposables(disposables)
    }

    fun addToDataBase(data: ArrayList<FileInfo>, listTitle: String) {// True: music/ False: book
        val musicSize = ShareMusicInfo.MusicInfoTool.getSize()
        val bookSize = ShareBookInfo.BookInfoTool.getSize()
        val disposables =
                FileSingle.insertFileInfo(data, listTitle)
                        .subscribe({
                            mMvpActivity.finishActivity(it,
                                    bookSize != ShareBookInfo.BookInfoTool.getSize(),
                                    musicSize != ShareMusicInfo.MusicInfoTool.getSize())
                        }, {})
        addDisposables(disposables)
    }
}