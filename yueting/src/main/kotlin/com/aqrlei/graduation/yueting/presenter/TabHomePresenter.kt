package com.aqrlei.graduation.yueting.presenter

import android.app.Service
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Messenger
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrlei.graduation.yueting.YueTingApplication
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.SelectInfo
import com.aqrlei.graduation.yueting.model.infotool.ShareBookInfo
import com.aqrlei.graduation.yueting.model.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.model.observable.BookSingle
import com.aqrlei.graduation.yueting.model.observable.MusicSingle
import com.aqrlei.graduation.yueting.ui.fragment.TabHomeFragment


/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/23
 */
/*
* @#param mMvpView 访问对应的Fragment
* */
class TabHomePresenter(mMvpView: TabHomeFragment) :
        MvpContract.FragmentPresenter<TabHomeFragment>(mMvpView) {
    fun sendMusicInfo(service: IBinder) {
        val disposables =
                MusicSingle.sendMusicInfo(service)
                        .subscribe({
                            if (it) {
                                mMvpView.unbindMusicService()
                            }
                        }, {})
        addDisposables(disposables)
    }

    fun getMusicInfoFromDB(typeName: String) {
        val disposables =
                MusicSingle.selectMusicInfo(typeName)
                        .subscribe({
                            mMvpView.setMusicInfo(it)
                        }, {})
        addDisposables(disposables)
    }

    fun getBookInfoFromDB(typeName: String) {
        val disposables =
                BookSingle.selectBookInfo(typeName)
                        .subscribe({
                            mMvpView.setBookInfo(it)
                        }, {})

        addDisposables(disposables)
    }

    fun deleteMusicItemFromDB(path: String) {
        DBManager.sqlData(
                DBManager.SqlFormat.deleteSqlFormat(DataConstant.MUSIC_TABLE_NAME,
                        DataConstant.COMMON_COLUMN_PATH, "="),
                null, arrayOf(path), DBManager.SqlType.DELETE)
    }

    fun deleteBookItemFromDB(path: String) {
        DBManager.sqlData(
                DBManager.SqlFormat.deleteSqlFormat(DataConstant.BOOK_TABLE_NAME,
                        DataConstant.COMMON_COLUMN_PATH, "="),
                null, arrayOf(path), DBManager.SqlType.DELETE)
        DBManager.sqlData(
                DBManager.SqlFormat.deleteSqlFormat(DataConstant.MARK_TABLE_NAME,
                        DataConstant.COMMON_COLUMN_PATH, "="),
                null, arrayOf(path), DBManager.SqlType.DELETE)
        DBManager.sqlData(
                DBManager.SqlFormat.deleteSqlFormat(DataConstant.CATALOG_TABLE_NAME,
                        DataConstant.COMMON_COLUMN_PATH, "="),
                null, arrayOf(path), DBManager.SqlType.DELETE)
    }

    fun startMusicService(context: Context, position: Int, messenger: Messenger, conn: ServiceConnection) {
        val mContext = context.applicationContext as YueTingApplication
        val musicIntent = mContext.getServiceIntent()
        musicIntent?.putExtra(YueTingConstant.SERVICE_MUSIC_ITEM_POSITION, position)
        musicIntent?.putExtra(YueTingConstant.SERVICE_MUSIC_MESSENGER, messenger)
        context.startService(musicIntent)
        context.bindService(musicIntent, conn, Service.BIND_AUTO_CREATE)
    }

    fun generateListSelectInfo(type: String): ArrayList<SelectInfo> {
        val tempList = ArrayList<SelectInfo>()
        if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK) {
            ShareBookInfo.BookInfoTool.getInfoS().forEach {

                tempList.add(SelectInfo(name = it.path))
            }
        } else {
            ShareMusicInfo.MusicInfoTool.getInfoS().forEach {
                tempList.add(SelectInfo(name = it.albumUrl))
            }
        }
        return tempList

    }

}