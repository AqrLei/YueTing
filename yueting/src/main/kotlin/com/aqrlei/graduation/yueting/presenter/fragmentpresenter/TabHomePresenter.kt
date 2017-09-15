package com.aqrlei.graduation.yueting.presenter.fragmentpresenter

import android.media.MediaMetadataRetriever
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.MusicInfo
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
    fun getMusicInfoFromDB() {
        val musicInfoList = ArrayList<MusicInfo>()
        DBManager.createDB()
        val c = DBManager.sqlData(DBManager.SqlFormat.selectSqlFormat(YueTingConstant.MUSIC_TABLE_NAME),
                null, null, DBManager.SqlType.SELECT)
                .getCursor()
        if (c != null) {
            while (c.moveToNext()) {
                val musicInfo = MusicInfo()
                val mmr = MediaMetadataRetriever()
                mmr.setDataSource(c.getString(c.getColumnIndex("path")))
                musicInfo.id = c.getInt(c.getColumnIndex("id"))
                musicInfo.title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                musicInfo.album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                musicInfo.artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                musicInfo.duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toInt()
                musicInfo.picture = mmr.embeddedPicture

                musicInfoList.add(musicInfo)
                mmr.release()
            }
        }
        mMvpView.setMusicInfo(musicInfoList)
    }


}