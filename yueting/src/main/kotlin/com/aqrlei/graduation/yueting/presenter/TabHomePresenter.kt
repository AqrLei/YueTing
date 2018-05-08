package com.aqrlei.graduation.yueting.presenter

import android.app.Service
import android.content.Context
import android.os.Messenger
import com.aqrlei.graduation.yueting.basemvp.MvpContract
import com.aqrlei.graduation.yueting.YueTingApplication
import com.aqrlei.graduation.yueting.constant.ActionConstant
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.SelectInfo
import com.aqrlei.graduation.yueting.model.infotool.ShareBookInfo
import com.aqrlei.graduation.yueting.model.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.model.observable.BookSingle
import com.aqrlei.graduation.yueting.model.observable.MusicSingle
import com.aqrlei.graduation.yueting.model.observable.TypeSingle
import com.aqrlei.graduation.yueting.ui.fragment.TabHomeFragment


/**
 * @Author: AqrLei
 * @Date: 2017/8/23
 */
/*
* @#param mMvpView 访问对应的Fragment
* */
class TabHomePresenter(mMvpView: TabHomeFragment) :
        MvpContract.FragmentPresenter<TabHomeFragment>(mMvpView) {

    fun fetchMusicInfo(typeName: String) {
        val disposables =
                MusicSingle.selectMusicInfo(typeName)
                        .subscribe({
                            mMvpView.setMusicInfo(it)
                        }, {})
        addDisposables(disposables)
    }

    fun fetchBookInfo(typeName: String) {
        val disposables =
                BookSingle.selectBookInfoByTypeName(typeName)
                        .subscribe({
                            mMvpView.setBookInfo(it)
                        }, {})

        addDisposables(disposables)
    }

    fun fetchTypeInfo(type: String) {
        val disposables =
                TypeSingle.selectType(type)
                        .subscribe({
                            mMvpView.setTypeInfo(it)
                        }, {})
        addDisposables(disposables)
    }

    fun deleteMusicItem(path: String) {
        val disposable =
                MusicSingle.deleteMusicInfo(path)
                        .subscribe()
        addDisposables(disposable)
    }

    fun deleteBookItem(path: String) {
        val disposable =
                BookSingle.deleteBookInfo(path)
                        .subscribe()
        addDisposables(disposable)
    }

    fun updateMusicTypeName(path: String, typeName: String) {
        val disposable =
                MusicSingle.updateTypeName(path, typeName)
                        .subscribe({
                            mMvpView.updateMusicFinish(it)
                        }, {})
        addDisposables(disposable)
    }

    fun updateBookTypeName(path: String, typeName: String) {
        val disposable =
                BookSingle.updateTypeName(path, typeName)
                        .subscribe({
                            mMvpView.updateBookFinish(it)
                        }, {})
        addDisposables(disposable)
    }

    fun startMusicService(context: Context, messenger: Messenger, position: Int, typeName: String) {
        val mContext = context.applicationContext as YueTingApplication
        val musicIntent = mContext.getServiceIntent()
        musicIntent.apply {
            putExtra(ActionConstant.ACTION_REFRESH_KEY, typeName)
            putExtra(YueTingConstant.SERVICE_MUSIC_ITEM_POSITION, position)
            putExtra(YueTingConstant.SERVICE_MUSIC_MESSENGER, messenger)
        }
        context.startService(musicIntent)
        context.bindService(musicIntent,ShareMusicInfo.MusicInfoTool.conn,Service.BIND_AUTO_CREATE)
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