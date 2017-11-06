package com.aqrlei.graduation.yueting.service

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.*
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.widget.RemoteViews
import com.aqrairsigns.aqrleilib.basemvp.BaseService
import com.aqrairsigns.aqrleilib.util.ActivityCollector
import com.aqrairsigns.aqrleilib.util.AppLog
import com.aqrairsigns.aqrleilib.util.ImageUtil
import com.aqrairsigns.aqrleilib.util.StringChangeUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.aidl.IMusicInfo
import com.aqrlei.graduation.yueting.aidl.MusicInfo
import com.aqrlei.graduation.yueting.constant.PlayState
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.ui.PlayActivity
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/19 Time: 13:28
 */
class MusicService : BaseService(),
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        AudioManager.OnAudioFocusChangeListener {
    override fun onPrepared(mp: MediaPlayer?) {
        refreshNotification()
        pPosition = cPosition
        refreshPi()
        sendPlayState(PlayState.PREPARE)
        if (!isPause) {
            mPlayer?.start()
            sendPlayState(PlayState.PLAY)

        } else {
            sendPlayState(PlayState.PAUSE)

        }
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        if (mPlayer != null) {
            mPlayer?.reset()
            sendPlayState(PlayState.PAUSE)
        }
        return true
    }

    override fun onCompletion(mp: MediaPlayer?) {
        if (playType == PlayType.SINGLE) {
            cPosition -= 1
        }
        sendPlayState(PlayState.COMPLETE)
        next()
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {//获得焦点
                if (isLossFocus) {
                    mPlayer?.setVolume(1.0F, 1.0F)
                    play()
                    isLossFocus = false
                }
                AppLog.logDebug("audioFocus", "AUDIOFOCUS_GAIN")
            }
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT -> {//暂时获得焦点
                AppLog.logDebug("audioFocus", "AUDIOFOCUS_GAIN_T")
            }
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE -> {//暂时独占焦点
                AppLog.logDebug("audioFocus", "AUDIOFOCUS_GAIN_T_E")
            }
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK -> {//暂时获得焦点，小声
                AppLog.logDebug("audioFocus", "AUDIOFOCUS_GAIN_T_M_D")

            }
            AudioManager.AUDIOFOCUS_LOSS -> {//失去焦点，应该释放资源
                pause()
                isLossFocus = true
                AppLog.logDebug("audioFocus", "AUDIOFOCUS_LOSS")
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {//暂时失去焦点， 暂停播放
                //mPlayer?.setVolume(0.2F,0.2F)
                pause()
                AppLog.logDebug("audioFocus", "AUDIOFOCUS_LOSS_T")
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {//暂时失去焦点，可以小声播放
                AppLog.logDebug("audioFocus", "AUDIOFOCUS_LOSS_T_C_D")
                mPlayer?.setVolume(0.2F, 0.2F)
            }
        }

    }

    companion object {
        private val mMusicInfoShare = ShareMusicInfo.MusicInfoTool
        private var mPlayer: MediaPlayer? = null
    }

    private var cPosition: Int = -1
    private var pPosition: Int = -1
    private var cDuration: Int = 0
    private var playType: PlayType = PlayType.LIST
    private var playerReceiver: PlayerReceiver? = null
    private var isPause: Boolean = false
    private var isSame: Boolean = false
    private var isLossFocus: Boolean = false
    private val handler = Handler()
    private var pi: PendingIntent? = null
    private var intent: Intent? = null
    private var stackBuilder: TaskStackBuilder? = null
    private lateinit var remoteViews: RemoteViews
    private lateinit var notification: Notification
    private lateinit var sendMessenger: Messenger
    private val NOTIFICATION_ID = 1
    private val PENDING_INTENT_ID = 0
    private val sendCDurationR = object : Runnable {
        override fun run() {
            cDuration = mPlayer?.currentPosition ?: 0
            val message = Message()
            message.what = YueTingConstant.CURRENT_DURATION
            message.arg1 = cDuration
            sendMessenger.send(message)
            if (!isPause) {
                handler.postDelayed(this, 100)
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        super.onBind(p0)
        return MusicBinder()

    }

    override fun onUnbind(intent: Intent?): Boolean {
        mMusicInfoShare.setInfoS(musicInfoS)
        play()
        return super.onUnbind(intent)
    }

    override fun onCreate() {
        initPlayer()
        regReceiver()
        buildNotification()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        cPosition = intent.extras.get("position") as Int
        sendMessenger = intent.extras.get("messenger") as Messenger
        sendPlayState(PlayState.START)
        return START_REDELIVER_INTENT
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

    override fun onDestroy() {
        sendPlayState(PlayState.FINISH)
        if (mPlayer != null) {
            mPlayer?.release()
            mPlayer = null
        }
        if (playerReceiver != null) {
            unregisterReceiver(playerReceiver)
        }
        stopForeground(true)
        super.onDestroy()
    }

    private fun regReceiver() {
        playerReceiver = PlayerReceiver()
        val filter = IntentFilter()
        YueTingConstant.ACTION_BROADCAST.forEach {
            filter.addAction(it)
        }
        filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        filter.priority = 1000
        registerReceiver(playerReceiver, filter)
    }

    private fun initPlayer() {
        if (mPlayer == null) {
            mPlayer = MediaPlayer()
        }

        mPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mPlayer?.setOnErrorListener(this)
        mPlayer?.setOnPreparedListener(this)
        mPlayer?.setOnCompletionListener(this)
        val audioManager = (getSystemService(Context.AUDIO_SERVICE) as AudioManager)
        audioManager.requestAudioFocus(
                this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN)

    }

    private fun refreshPi() {
        val bundle = Bundle()
        val playType = sendPlayType()
        val playState = if (mPlayer!!.isPlaying) 1 else 0
        val initArray = intArrayOf(cPosition, mPlayer?.audioSessionId ?: 0, playType, playState)
        bundle.putIntArray("init", initArray)
        intent?.putExtra("init_bundle", bundle)
        /*
        * stackBuilder?.addNextIntent(intent)
        * after the first addition will always exist, do not repeat the add
        */
        pi = stackBuilder?.getPendingIntent(
                PENDING_INTENT_ID,
                PendingIntent.FLAG_UPDATE_CURRENT)
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun makeTaskStack(): PendingIntent? {
        intent = Intent(this, PlayActivity::class.java)
        stackBuilder = TaskStackBuilder.create(this)
        stackBuilder?.addParentStack(PlayActivity::class.java)
        stackBuilder?.addNextIntent(intent)
        return stackBuilder?.getPendingIntent(
                PENDING_INTENT_ID,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun buildNotification() {

        pi = makeTaskStack()
        remoteViews = RemoteViews(this.packageName, R.layout.notification_foreground)
        /*
        * 设置自定义的Notification布局时，通过setContentIntent设置跳转到Activity会出错
        * 黑屏 且结束后为锁屏界面
        * */
        remoteViews.setOnClickPendingIntent(R.id.tv_music_info, pi)
        for (i in 0 until YueTingConstant.ACTION_BROADCAST.size) {

            if (i == 2) continue

            val pIntent = bindListener(
                    YueTingConstant.ACTION_BROADCAST[i],
                    YueTingConstant.ACTION_REQ_CODE[i])

            when (i) {
                0 -> {
                    remoteViews.setOnClickPendingIntent(R.id.tv_play_control, pIntent)
                }
                1 -> {
                    remoteViews.setOnClickPendingIntent(R.id.tv_next, pIntent)
                }
                3 -> {
                    remoteViews.setOnClickPendingIntent(R.id.tv_finish, pIntent)
                }
            }

            if (i == 3) break
        }

        notification = NotificationCompat.Builder(this.applicationContext)
                .setContent(remoteViews)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)//必须设置，不然无法显示自定义的View
                .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun refreshNotification() {
        val musicInfo = mMusicInfoShare.getInfo(cPosition)
        val musicString = StringChangeUtil.SPANNABLE.clear()
                .foregroundColorChange("#1c4243", musicInfo.title)
                .relativeSizeChange(2 / 3F, "\n${musicInfo.artist} - ${musicInfo.album}")
                .complete()
        remoteViews.setTextViewText(R.id.tv_music_info, musicString)
        val bitmap = ImageUtil.byteArrayToBitmap(musicInfo.picture)
        remoteViews.setImageViewBitmap(R.id.iv_album_picture, bitmap)
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun refreshPlayView(playState: String) {
        remoteViews.setTextViewText(R.id.tv_play_control, playState)
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun bindListener(action: String, requestCode: Int): PendingIntent {
        return PendingIntent.getBroadcast(
                this.applicationContext,
                requestCode,
                Intent(action),
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun play() {
        isSame = (cPosition == pPosition)
        if (mPlayer != null && mMusicInfoShare.getInfoS().size > 0) {
            if (isSame) {
                mPlayer?.seekTo(0)
                if (isPause) {
                    mPlayer?.seekTo(cDuration)
                }
                mPlayer?.start()
                sendPlayState(PlayState.PLAY)
                isPause = false
            } else {
                val musicInfo = mMusicInfoShare.getInfoS()[cPosition]
                mPlayer?.reset()
                try {
                    mPlayer?.setDataSource(musicInfo.albumUrl)
                    mPlayer?.prepareAsync()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            sendCDuration()
        }
    }

    private fun sendCDuration() {
        handler.post(sendCDurationR)
    }

    private fun sendPlayState(playState: PlayState) {
        val message = Message()
        message.what = YueTingConstant.PLAY_STATE
        when (playState) {
            PlayState.PAUSE -> {
                message.arg1 = 0
                refreshPlayView("播")
            }
            PlayState.PLAY -> {
                message.arg1 = 1
                refreshPlayView("停")
            }
            PlayState.COMPLETE -> {
                message.arg1 = 2
            }
            PlayState.PREPARE -> {
                message.arg1 = 3
                val bundle = Bundle()
                bundle.putInt("audioSessionId", mPlayer?.audioSessionId ?: 0)
                message.data = bundle
            }
            PlayState.FINISH -> {
                message.arg1 = 4
            }
            PlayState.START -> {
                message.arg1 = 5
            }
        }
        message.arg2 = cPosition
        sendMessenger.send(message)
    }

    private fun sendPlayType(): Int {
        val msg = Message()
        msg.what = YueTingConstant.PLAY_TYPE
        when (playType) {
            PlayType.SINGLE -> {
                msg.arg1 = YueTingConstant.ACTION_SINGLE
            }
            PlayType.LIST -> {
                msg.arg1 = YueTingConstant.ACTION_LIST
            }
            PlayType.RANDOM -> {
                msg.arg1 = YueTingConstant.ACTION_RANDOM
            }
        }
        sendMessenger.send(msg)
        return msg.arg1
    }

    private fun pauseOrPlay() {
        if (mPlayer != null && mPlayer!!.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    private fun pause() {
        mPlayer?.pause()
        sendPlayState(PlayState.PAUSE)
        isPause = true
    }

    private fun next() {
        changePosition(PlayDirection.NEXT)
        play()
    }

    private fun previous() {
        changePosition(PlayDirection.PREVIOUS)
        play()
    }

    private fun changePosition(direction: PlayDirection) {
        if (direction == PlayDirection.NEXT) {
            when (playType) {
                PlayType.SINGLE -> {
                    nextPosition()
                }
                PlayType.LIST -> {
                    nextPosition()
                }
                PlayType.RANDOM -> {
                    randomPosition()
                }
            }
        } else {
            when (playType) {
                PlayType.SINGLE -> {
                    previousPosition()
                }
                PlayType.LIST -> {
                    previousPosition()
                }
                PlayType.RANDOM -> {
                    randomPosition()
                }
            }
        }

    }

    private fun randomPosition() {
        cPosition = Random().nextInt(mMusicInfoShare.getSize())
    }

    private fun nextPosition() {
        cPosition =
                if (cPosition + 1 > mMusicInfoShare.getSize() - 1) {
                    0
                } else {
                    cPosition + 1
                }
    }

    private fun previousPosition() {
        cPosition =
                if (cPosition - 1 < 0) {
                    mMusicInfoShare.getSize() - 1
                } else {
                    cPosition - 1
                }
    }

    private fun changePlayType(action: String?) {
        val ACTION = YueTingConstant.ACTION_BROADCAST
        when (action) {
            ACTION[YueTingConstant.ACTION_SINGLE] -> {
                playType = PlayType.SINGLE
            }
            ACTION[YueTingConstant.ACTION_LIST] -> {
                playType = PlayType.LIST
            }
            ACTION[YueTingConstant.ACTION_RANDOM] -> {
                playType = PlayType.RANDOM
            }
        }
    }

    private fun changePlayState(action: String?, intent: Intent?) {
        val ACTION = YueTingConstant.ACTION_BROADCAST
        when (action) {
            ACTION[YueTingConstant.ACTION_PLAY] -> {
                cPosition = intent?.getIntExtra("position", cPosition) ?: cPosition
                if (cPosition == pPosition) {
                    pauseOrPlay()
                } else {
                    play()
                }
            }
            ACTION[YueTingConstant.ACTION_NEXT] -> {
                next()

            }
            ACTION[YueTingConstant.ACTION_PREVIOUS] -> {
                previous()
            }

            ACTION[YueTingConstant.ACTION_FINISH] -> {
                ActivityCollector.removeAll()
                stopSelf()
            }
            AudioManager.ACTION_AUDIO_BECOMING_NOISY -> {
                pause()
            }
        }
    }

    inner class PlayerReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            changePlayType(action)
            changePlayState(action, intent)
            sendPlayType()
        }
    }

    private val musicInfoS = ArrayList<MusicInfo>()

    inner class MusicBinder : IMusicInfo.Stub() {
        override fun setMusicInfo(infoS: MutableList<MusicInfo>?) {
            if (infoS != null) {
                musicInfoS.addAll(infoS)
            }
        }
    }

    enum class PlayType {
        SINGLE, RANDOM, LIST
    }

    private enum class PlayDirection {
        NEXT, PREVIOUS
    }

}