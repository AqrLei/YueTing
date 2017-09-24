package com.aqrlei.graduation.yueting.model.local.infotool

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import com.aqrairsigns.aqrleilib.basemvp.BaseActivity
import com.aqrlei.graduation.yueting.aidl.MusicInfo
import com.aqrlei.graduation.yueting.constant.PlayState
import com.aqrlei.graduation.yueting.constant.SendType
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.ui.PlayActivity
import com.aqrlei.graduation.yueting.ui.YueTingActivity


/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/20 Time: 9:06
 */
/*
* 音乐信息共享单例
* */
enum class ShareMusicInfo {

    MusicInfoTool;


    fun setInfoS(infoS: ArrayList<MusicInfo>) {
        clearMusicInfo()
        musicInfoList.addAll(infoS)
    }

    fun getInfo(position: Int) = musicInfoList[position]

    fun getInfoS() = musicInfoList
    fun addInfo(musicInfo: MusicInfo) {
        musicInfoList.add(musicInfo)
    }

    fun removeInfo(info: MusicInfo) {
        if (!musicInfoList.isEmpty()) {
            musicInfoList.remove(info)
        }
    }

    fun removeInfo(position: Int) {
        if (!musicInfoList.isEmpty()) {
            musicInfoList.removeAt(position)
        }
    }


    fun sendBroadcast(context: Context, type: SendType, code: Int = 1) {
        val intent = Intent()
        val action = when (type) {
            SendType.PLAY -> {
                YueTingConstant.ACTION_BROADCAST[YueTingConstant.ACTION_PLAY]
            }
            SendType.NEXT -> {
                YueTingConstant.ACTION_BROADCAST[YueTingConstant.ACTION_NEXT]
            }
            SendType.PREVIOUS -> {
                YueTingConstant.ACTION_BROADCAST[YueTingConstant.ACTION_PREVIOUS]
            }
            SendType.PLAY_TYPE -> {
                when (code) {
                    0 -> {
                        YueTingConstant.ACTION_BROADCAST[YueTingConstant.ACTION_SINGLE]
                    }
                    1 -> {
                        YueTingConstant.ACTION_BROADCAST[YueTingConstant.ACTION_LIST]
                    }
                    2 -> {
                        YueTingConstant.ACTION_BROADCAST[YueTingConstant.ACTION_RANDOM]
                    }
                    else -> {
                        YueTingConstant.ACTION_BROADCAST[YueTingConstant.ACTION_LIST]
                    }
                }
            }
        }
        intent.action = action

        context.sendOrderedBroadcast(intent, null)
    }

    fun getSize() = musicInfoList.size

    private fun clearMusicInfo() {
        musicInfoList.clear()
    }

    fun clear() {
        mContext.clear()
    }

    private var duration: Int = 0
    private var position: Int = 0
    private var playType: String = "表"
    private var playState: PlayState = PlayState.PAUSE
    private var mHandler: Handler? = null
    fun setPosition(p: Int) {
        position = p
    }

    fun getPosition() = position
    fun setDuration(d: Int) {
        duration = d
    }

    fun setPlayType(type: String) {
        playType = type
    }

    fun setPlayState(state: PlayState) {
        playState = state
    }

    fun getDuration() = duration
    fun getPlayType() = playType
    fun getPlayState() = playState
    fun getHandler(context: BaseActivity? = null): Handler {
        if (context != null) {
            mContext.add(context)
        }
        if (mHandler == null) {
            mHandler = object : Handler() {
                override fun handleMessage(msg: Message) {
                    mContext.forEach {
                        if (!it.isFinishing) {
                            if (it is PlayActivity) {
                                it.refreshPlayView(msg)
                            }
                            if (it is YueTingActivity) {
                                it.refreshPlayView(msg)
                            }
                        }
                    }
                }
            }
        }
        return mHandler ?: Handler()
    }


    companion object {
        private var musicInfoList = ArrayList<MusicInfo>()
        private var mContext = ArrayList<BaseActivity>()
    }

}