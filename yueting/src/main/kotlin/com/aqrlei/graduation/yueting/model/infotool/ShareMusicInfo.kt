package com.aqrlei.graduation.yueting.model.infotool

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aqrairsigns.aqrleilib.basemvp.BaseActivity
import com.aqrairsigns.aqrleilib.ui.view.RoundBar
import com.aqrairsigns.aqrleilib.util.ActivityCollector
import com.aqrairsigns.aqrleilib.util.ImageUtil
import com.aqrlei.graduation.yueting.IListenerManager
import com.aqrlei.graduation.yueting.IOnLrcIndexListener
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.ActionConstant
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.enumtype.PlayState
import com.aqrlei.graduation.yueting.enumtype.SendType
import com.aqrlei.graduation.yueting.model.MusicInfo
import com.aqrlei.graduation.yueting.ui.PlayActivity
import com.aqrlei.graduation.yueting.ui.YueTingActivity


/**
 * @Author: AqrLei
 * @CreateTime: Date: 2017/9/20 Time: 9:06
 */
/*
* 音乐信息共享单例
* */
@Suppress("unused")
enum class ShareMusicInfo {

    MusicInfoTool;

    companion object {
        private var musicInfoList = ArrayList<MusicInfo>()
        private var mContext = ArrayList<BaseActivity>()
    }

    private var listenerManager: IListenerManager? = null
    val lrcListener: IOnLrcIndexListener
            by lazy {
                object : IOnLrcIndexListener.Stub() {
                    override fun onLrcIndexListener(index: Int) {
                        mContext.forEach {
                            if (it is PlayActivity && !it.isFinishing) {
                                it.refreshLrc(index)
                            }
                        }
                    }
                }
            }
    val conn: ServiceConnection
            by lazy {
                object : ServiceConnection {
                    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                        listenerManager = IListenerManager.Stub.asInterface(service)
                        listenerManager?.registerListener(lrcListener)
                    }

                    override fun onServiceDisconnected(name: ComponentName?) {
                        listenerManager = null
                    }
                }
            }
    private var duration: Int = 0
    private var position: Int = 0
    private var audioSessionId: Int = 0
    private var playType: Int = YueTingConstant.PLAY_TYPE_REPEAT
    private var playState: PlayState = PlayState.PAUSE
    private var mHandler: Handler? = null
    private var isStartService: Boolean = false
    var oldSize: Int = 0
    var oldTypeName: String = ""
    private var musicName: String = ""

    fun getInfo(position: Int) = musicInfoList[position]
    fun getInfoS() = musicInfoList
    fun getSize() = musicInfoList.size
    fun getPosition() = position
    fun getDuration() = duration
    fun getAudioSessionId() = audioSessionId
    fun getPlayType() = playType
    fun isStartService() = isStartService

    fun addInfo(musicInfo: MusicInfo) {
        musicInfoList.add(musicInfo)
    }

    fun removeInfo(info: MusicInfo) {
        if (!musicInfoList.isEmpty()) {
            musicInfoList.remove(info)
        }
    }

    fun setInfoS(infoS: ArrayList<MusicInfo>) {
        oldSize = musicInfoList.size
        clearMusicInfo()
        musicInfoList.addAll(infoS)
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

    fun clearMusicInfo() {
        musicInfoList.clear()
    }

    fun clear() {
        mContext.clear()
    }

    fun setPosition(p: Int) {
        position = p
    }

    fun setAudioSessionId(id: Int) {
        audioSessionId = id
    }

    fun getHandler(context: BaseActivity? = null): Handler {
        if (context != null) {
            mContext.add(context)
        }
        if (mHandler == null) {
            mHandler = @SuppressLint("HandlerLeak")
            object : Handler() {
                override fun handleMessage(msg: Message) {
                    if (msg.what == ActionConstant.ACTION_FINISH_REQ) {
                        ActivityCollector.killApp()
                    }
                    mContext.forEach {
                        if (!it.isFinishing) {
                            when (it) {
                                is PlayActivity -> {
                                    refreshPlayView(it.getMPlayView(), msg,it)
                                }
                                is YueTingActivity -> {
                                    refreshPlayView(it.getMPlayView(), msg)
                                    it.setMusicTitle(musicName)
                                    it.dismissDialog()
                                }
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
        (view.findViewById(R.id.playControlIv) as ImageView).setImageLevel(
                if (PlayState.PAUSE == playState) YueTingConstant.PLAY_STATUS_PAUSE
                else YueTingConstant.PLAY_STATUS_PLAY)

        (view.findViewById(R.id.iv_album_picture) as ImageView).setImageBitmap(bitmap)
        (view.findViewById(R.id.musicInfoTv) as TextView).text = musicInfo.title
        (view.findViewById(R.id.musicInfoDetailTv) as TextView).text = " ${musicInfo.artist} - ${musicInfo.album} "
        musicName = musicInfo.title
    }

    fun isStartService(isStart: Boolean) {
        isStartService = isStart
    }

    fun changePlayType(type: Int, iv: ImageView) {
        when (type) {
            ActionConstant.ACTION_SINGLE_REQ -> {

                iv.setImageLevel(YueTingConstant.PLAY_TYPE_REPEAT_ONE)
                playType = YueTingConstant.PLAY_TYPE_REPEAT_ONE
            }
            ActionConstant.ACTION_LIST_REQ -> {
                iv.setImageLevel(YueTingConstant.PLAY_TYPE_REPEAT)
                playType = YueTingConstant.PLAY_TYPE_REPEAT

            }
            ActionConstant.ACTION_RANDOM_REQ -> {
                iv.setImageLevel(YueTingConstant.PLAY_TYPE_RANDOM)
                playType = YueTingConstant.PLAY_TYPE_RANDOM
            }
        }
    }

    fun changePlayState(state: Int = 0,
                        mPlayView: ViewGroup,
                        msg: Message = Message(),
                        context: PlayActivity?=null) {
        when (state) {
            0 -> {//PAUSE
                (mPlayView.findViewById(R.id.playControlIv) as ImageView).setImageLevel(YueTingConstant.PLAY_STATUS_PAUSE)
                playState = PlayState.PAUSE
            }
            1 -> {//PLAY
                (mPlayView.findViewById(R.id.playControlIv) as ImageView).setImageLevel(YueTingConstant.PLAY_STATUS_PLAY)
                playState = PlayState.PLAY
            }
            2 -> {//COMPLETE
                //(mPlayView.findViewById(R.id.rb_progress_play) as RoundBar).setProgress(0F)
            }
            3 -> {//PREPARE
                val position = msg.arg2
                val audioSessionId = msg.data[YueTingConstant.SERVICE_PLAY_AUDIO_SESSION] as Int
                mPlayView.findViewById<ImageView>(R.id.playTypeIv)?.let {
                    (it as ImageView).setImageLevel(getPlayType())
                }
                setPosition(position)
                setAudioSessionId(audioSessionId)
                shareViewInit(mPlayView, position, duration)
                context?.refresh()

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

    private fun refreshPlayView(mPlayView: ViewGroup, msg: Message, context: PlayActivity? = null) {

        if (msg.what == YueTingConstant.CURRENT_DURATION) {
            (mPlayView.findViewById(R.id.rb_progress_play) as RoundBar).setProgress(msg.arg1.toFloat())
            duration = msg.arg1
        }
        if (msg.what == YueTingConstant.PLAY_STATE) {
            changePlayState(msg.arg1, mPlayView, msg,context)
        }
        if (msg.what == YueTingConstant.PLAY_TYPE) {//PlayActivity privately-owned
            mPlayView.findViewById<ImageView>(R.id.playTypeIv)?.let {
                changePlayType(msg.arg1, it as ImageView)
            }
        }
    }
}