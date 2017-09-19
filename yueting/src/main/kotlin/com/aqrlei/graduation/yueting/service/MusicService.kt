package com.aqrlei.graduation.yueting.service

import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import com.aqrairsigns.aqrleilib.basemvp.BaseService
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrlei.graduation.yueting.model.local.MusicInfo

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/19 Time: 13:28
 */
class MusicService : BaseService() {
    private lateinit var mMusicInfoList: ArrayList<MusicInfo>
    private var position: Int = 0
    override fun onBind(p0: Intent?): IBinder? {
        return super.onBind(p0)
    }

    override fun onCreate() {
        mMusicInfoList = ArrayList()
        super.onCreate()
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        AppToast.toastShow(applicationContext, "onStartCommand", Toast.LENGTH_LONG)
        val bundle = intent.extras as Bundle
        mMusicInfoList = bundle.getParcelableArrayList<MusicInfo>("musicInfo")
        position = bundle.getInt("position")
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}