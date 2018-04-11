package com.aqrlei.graduation.yueting.model.local.infotool

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aqrairsigns.aqrleilib.basemvp.BaseActivity
import com.aqrairsigns.aqrleilib.ui.view.RoundBar
import com.aqrairsigns.aqrleilib.util.ImageUtil
import com.aqrairsigns.aqrleilib.util.StringChangeUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.aidl.MusicInfo
import com.aqrlei.graduation.yueting.constant.ActionConstant
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.enumtype.PlayState
import com.aqrlei.graduation.yueting.enumtype.SendType
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

    fun has(other: MusicInfo): Boolean {
        if (musicInfoList.isNotEmpty()) {
            musicInfoList.forEachIndexed { _, musicInfo ->
                if (musicInfo == other) {
                    return true
                }
            }
        }
        return false
    }


    fun sendBroadcast(context: Context, type: SendType, code: Int = YueTingConstant.PLAY_LIST) {
        val intent = Intent()
        val action = when (type) {
            SendType.PLAY -> {
                ActionConstant.ACTION_PLAY
            }
            SendType.NEXT -> {
                ActionConstant.ACTION_NEXT
            }
            SendType.PREVIOUS -> {
                ActionConstant.ACTION_PREVIOUS
            }
            SendType.PLAY_TYPE -> {
                when (code) {
                    YueTingConstant.PLAY_SINGLE -> {
                        ActionConstant.ACTION_SINGLE
                    }
                    YueTingConstant.PLAY_LIST -> {
                        ActionConstant.ACTION_LIST
                    }
                    YueTingConstant.PLAY_RANDOM -> {
                        ActionConstant.ACTION_RANDOM
                    }
                    else -> {
                        ActionConstant.ACTION_LIST
                    }
                }
            }
        }
        intent.action = action

        context.sendOrderedBroadcast(intent, null)
    }

    fun getSize() = musicInfoList.size

    fun clearMusicInfo() {
        musicInfoList.clear()
    }

    fun clear() {
        mContext.clear()
    }

    private var duration: Int = 0
    private var position: Int = 0
    private var audioSessionId: Int = 0
    private var playType: String = "表"
    private var playState: PlayState = PlayState.PAUSE
    private var mHandler: Handler? = null
    private var isStartService: Boolean = false
    fun setPosition(p: Int) {
        position = p
    }

    fun setAudioSessionId(id: Int) {
        audioSessionId = id
    }

    fun getPosition() = position
    fun getDuration() = duration
    fun getAudioSessionId() = audioSessionId
    fun getPlayType() = playType
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
                                refreshPlayView(it.getMPlayView(), msg)
                            }
                            if (it is YueTingActivity) {
                                refreshPlayView(it.getMPlayView(), msg)
                            }
                        }
                    }
                }
            }
        }
        return mHandler ?: Handler()
    }

    fun shareViewInit(view: ViewGroup, position: Int, duration: Int) {
        val musicInfo = getInfo(position)
        val roundBar = view.findViewById(R.id.rb_progress_play) as RoundBar
        val playState = playState
        val bitmap = ImageUtil.byteArrayToBitmap(musicInfo.picture)
        val maxProgress = musicInfo.duration.toFloat()

        roundBar.setMaxProgress(maxProgress)
        roundBar.setProgress(duration.toFloat())
        (view.findViewById(R.id.tv_play_control) as TextView).text =
                if (PlayState.PAUSE == playState) "播" else "停"
        (view.findViewById(R.id.iv_album_picture) as ImageView).setImageBitmap(bitmap)
        (view.findViewById(R.id.tv_music_info) as TextView).text =
                StringChangeUtil.SPANNABLE.clear()
                        .foregroundColorChange("#1c4243", musicInfo.title)
                        .relativeSizeChange(2 / 3F, "\n${musicInfo.artist} - ${musicInfo.album}")
                        .complete()
    }

    fun isStartService() = isStartService
    fun isStartService(isStart: Boolean) {
        isStartService = isStart
    }

    private fun refreshPlayView(mPlayView: ViewGroup, msg: Message) {

        if (msg.what == YueTingConstant.CURRENT_DURATION) {
            (mPlayView.findViewById(R.id.rb_progress_play) as RoundBar).setProgress(msg.arg1.toFloat())
            duration = msg.arg1
        }
        if (msg.what == YueTingConstant.PLAY_STATE) {
            changePlayState(msg.arg1, mPlayView, msg)
        }
        if (msg.what == YueTingConstant.PLAY_TYPE) {//PlayActivity privately-owned
            val tv = mPlayView.findViewById(R.id.tv_play_type)
            if (tv != null) {
                changePlayType(msg.arg1, tv as TextView)
            }
        }
    }

    fun changePlayType(type: Int, tv: TextView) {
        when (type) {
            ActionConstant.ACTION_SINGLE_REQ -> {
                tv.text = "单"
                playType = "单"
            }
            ActionConstant.ACTION_LIST_REQ -> {
                tv.text = "表"
                playType = "表"

            }
            ActionConstant.ACTION_RANDOM_REQ -> {
                tv.text = "变"
                playType = "变"
            }
        }
    }

    fun changePlayState(state: Int = 0, mPlayView: ViewGroup, msg: Message = Message()) {
        when (state) {
            0 -> {//PAUSE
                (mPlayView.findViewById(R.id.tv_play_control) as TextView).text = "播"
                playState = PlayState.PAUSE
            }
            1 -> {//PLAY
                (mPlayView.findViewById(R.id.tv_play_control) as TextView).text = "停"
                playState = PlayState.PLAY
            }
            2 -> {//COMPLETE
                //(mPlayView.findViewById(R.id.rb_progress_play) as RoundBar).setProgress(0F)
            }
            3 -> {//PREPARE
                val position = msg.arg2
                val audioSessionId = msg.data[YueTingConstant.SERVICE_PLAY_AUDIO_SESSION] as Int
                val tv = mPlayView.findViewById(R.id.tv_play_type)
                if (tv != null) {
                    (tv as TextView).text = getPlayType()
                }
                setPosition(position)
                setAudioSessionId(audioSessionId)
                shareViewInit(mPlayView, position, duration)
            }
            4 -> {//FINISH
                isStartService = false
                mPlayView.visibility = View.GONE
            }
            5 -> {
                isStartService = true
                mPlayView.visibility = View.VISIBLE

            }
        }
    }


    companion object {
        private var musicInfoList = ArrayList<MusicInfo>()
        private var mContext = ArrayList<BaseActivity>()
    }

}