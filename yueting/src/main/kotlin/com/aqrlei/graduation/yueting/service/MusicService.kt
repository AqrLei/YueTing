package com.aqrlei.graduation.yueting.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
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
import com.aqrlei.graduation.yueting.IListenerManager
import com.aqrlei.graduation.yueting.IOnLrcIndexListener
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.ActionConstant
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.enumtype.PlayState
import com.aqrlei.graduation.yueting.model.LrcInfo
import com.aqrlei.graduation.yueting.model.MusicInfo
import com.aqrlei.graduation.yueting.model.infotool.LrcInfoProcess
import com.aqrlei.graduation.yueting.ui.PlayActivity
import com.aqrlei.graduation.yueting.util.createNotificationChannel
import io.reactivex.disposables.Disposable
import java.io.IOException
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.collections.ArrayList


/**
 * @Author: AqrLei
 * @CreateTime: Date: 2017/9/19 Time: 13:28
 */
class MusicService : BaseService(),
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        AudioManager.OnAudioFocusChangeListener {
    companion object {
        private var mPlayer: MediaPlayer? = null
    }

    private lateinit var disposable: Disposable
    private var typeName: String = ""
    private var cPosition: Int = -1
    private var pPosition: Int = -1
    private var cDuration: Int = 0
    private var isPause: Boolean = false
    private var isSame: Boolean = false
    private var isLossFocus: Boolean = false
    private var playType: PlayType = PlayType.LIST
    private var playerReceiver: PlayerReceiver? = null
    private val handler = Handler()
    private var pi: PendingIntent? = null
    private val backIntent: Intent
            by lazy {
                Intent(this, PlayActivity::class.java)
            }
    private var stackBuilder: TaskStackBuilder? = null
    private lateinit var remoteViews: RemoteViews
    private lateinit var notification: Notification
    private lateinit var sendMessenger: Messenger
    private val musicInfoS: ArrayList<MusicInfo>
            by lazy {
                ArrayList<MusicInfo>()
            }
    private val lrcList: ArrayList<LrcInfo>
            by lazy {
                ArrayList<LrcInfo>()
            }
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
    private var lrcListener: IOnLrcIndexListener? = null
    private val lrcListenerBinder = object : IListenerManager.Stub() {
        override fun registerListener(listener: IOnLrcIndexListener?) {
            lrcListener = listener
        }
    }
    private var serviceAlive = AtomicBoolean(true)
    private val runnable: Runnable
            by lazy {
                Runnable {
                    while (serviceAlive.get()) {
                        try {
                            Thread.sleep(100L)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                        var index = 0
                        for (i in 0 until lrcList.size) {
                            if (i < lrcList.size - 1) {
                                if (cDuration < lrcList[i].lrcTime && i == 0) {
                                    index = i
                                }
                                if (cDuration > lrcList[i].lrcTime && cDuration < lrcList[i + 1].lrcTime) {
                                    index = i
                                }
                            }
                            if (i == lrcList.size - 1 && cDuration > lrcList[i].lrcTime) {
                                index = i
                            }
                        }
                        try {
                            if (lrcList.isNotEmpty()) {
                                lrcListener?.onLrcIndexListener(index)
                            }
                        } catch (e: RemoteException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

    override fun onPrepared(mp: MediaPlayer?) {
        refreshNotification()
        pPosition = cPosition

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

    override fun onCreate() {
        initPlayer()
        buildNotification()
        regReceiver()
        super.onCreate()
    }

    override fun onBind(p0: Intent?): IBinder? {
        super.onBind(p0)
        return lrcListenerBinder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        sendMessenger = intent.extras.get(YueTingConstant.SERVICE_MUSIC_MESSENGER) as Messenger
        typeName = intent.extras.getString(ActionConstant.ACTION_REFRESH_KEY)
                ?: DataConstant.DEFAULT_TYPE_NAME
        val position = intent.extras.getInt(YueTingConstant.SERVICE_MUSIC_ITEM_POSITION)
        getMusicInfo(typeName, position)
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        sendPlayState(PlayState.FINISH)
        serviceAlive.set(false)
        lrcListener = null
        if (mPlayer != null) {
            mPlayer?.release()
            mPlayer = null
        }
        if (!disposable.isDisposed) {
            disposable.dispose()
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
        val actionArray = arrayOf(
                ActionConstant.ACTION_PLAY,
                ActionConstant.ACTION_NEXT,
                ActionConstant.ACTION_PREVIOUS,
                ActionConstant.ACTION_FINISH,
                ActionConstant.ACTION_SINGLE,
                ActionConstant.ACTION_LIST,
                ActionConstant.ACTION_RANDOM,
                ActionConstant.ACTION_REFRESH
        )
        actionArray.forEach {
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
        val audioAttributes =
                AudioAttributes.Builder().apply {
                    setLegacyStreamType(AudioManager.STREAM_MUSIC)
                }.build()

        mPlayer?.also {
            it.setAudioAttributes(audioAttributes)
            it.setOnErrorListener(this)
            it.setOnPreparedListener(this)
            it.setOnCompletionListener(this)
        }
        val audioManager =
                (getSystemService(Context.AUDIO_SERVICE) as AudioManager)
        @Suppress("DEPRECATION")
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
        /**
         *add necessary data in order to jump to PlayActivity
         */
        bundle.putIntArray(YueTingConstant.SERVICE_PLAY_STATUS, initArray)
        bundle.putString(YueTingConstant.FRAGMENT_TITLE_VALUE, typeName)
        backIntent.putExtra(YueTingConstant.SERVICE_PLAY_STATUS_B, bundle)
        pi = stackBuilder?.getPendingIntent(
                YueTingConstant.SERVICE_PENDING_INTENT_ID,
                PendingIntent.FLAG_UPDATE_CURRENT)
        startForeground(YueTingConstant.SERVICE_NOTIFICATION_ID, notification)
    }

    private fun makeTaskStack(): PendingIntent? {

        stackBuilder = TaskStackBuilder.create(this)
        stackBuilder?.addParentStack(PlayActivity::class.java)
        stackBuilder?.addNextIntent(backIntent)
        return stackBuilder?.getPendingIntent(
                YueTingConstant.SERVICE_PENDING_INTENT_ID,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun buildNotification() {
        pi = makeTaskStack()
        remoteViews = RemoteViews(this.packageName, R.layout.music_notification_foreground)
        val playIntent = bindListener(ActionConstant.ACTION_PLAY, ActionConstant.ACTION_PLAY_REQ)
        val nextIntent = bindListener(ActionConstant.ACTION_NEXT, ActionConstant.ACTION_NEXT_REQ)
        val finishIntent = bindListener(ActionConstant.ACTION_FINISH, ActionConstant.ACTION_FINISH_REQ)
        /**
         * 设置自定义的Notification布局时，通过setContentIntent设置跳转到Activity会出错
         * 黑屏 且结束后为锁屏界面,
         * */
        remoteViews.setOnClickPendingIntent(R.id.musicInfoLl, pi)
        remoteViews.setOnClickPendingIntent(R.id.playControlIv, playIntent)
        remoteViews.setOnClickPendingIntent(R.id.nextIv, nextIntent)
        remoteViews.setOnClickPendingIntent(R.id.closeIv, finishIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(
                    applicationContext,
                    YueTingConstant.NOTIFICATION_CHANNEL_ID,
                    YueTingConstant.NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH)
        }

        notification = NotificationCompat.Builder(this.applicationContext, YueTingConstant.NOTIFICATION_CHANNEL_ID)
                .setContent(remoteViews)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_music_note_black)//必须设置，不然无法显示自定义的View
                .build()
        startForeground(YueTingConstant.SERVICE_NOTIFICATION_ID, notification)
    }

    private fun refreshNotification() {
        val musicInfo = musicInfoS[cPosition]
        remoteViews.setTextViewText(R.id.musicInfoTv, musicInfo.title)
        remoteViews.setTextViewText(R.id.musicInfoDetailTv, "\n${musicInfo.artist} - ${musicInfo.album}")
        val bitmap = ImageUtil.byteArrayToBitmap(musicInfo.picture)
        remoteViews.setImageViewBitmap(R.id.iv_album_picture, bitmap)
        startForeground(YueTingConstant.SERVICE_NOTIFICATION_ID, notification)
    }

    private fun refreshPlayView(playState: Int) {
        remoteViews.setImageViewResource(R.id.playControlIv,
                if (playState == YueTingConstant.PLAY_STATUS_PAUSE) R.drawable.music_selector_pause
                else R.drawable.music_selector_play)
        startForeground(YueTingConstant.SERVICE_NOTIFICATION_ID, notification)
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
        if (mPlayer != null && musicInfoS.size > 0) {
            if (isSame) {
                mPlayer?.seekTo(0)
                if (isPause) {
                    mPlayer?.seekTo(cDuration)
                }
                mPlayer?.start()
                sendPlayState(PlayState.PLAY)
                isPause = false
            } else {
                val musicInfo = musicInfoS[cPosition]
                mPlayer?.reset()
                try {
                    mPlayer?.setDataSource(musicInfo.albumUrl)
                    mPlayer?.prepareAsync()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            sendCDuration()
            sendLrcIndex()
        }
    }

    private fun sendLrcIndex() {
        lrcList.clear()
        lrcList.addAll(LrcInfoProcess.readLRC(musicInfoS[cPosition].albumUrl))
        Thread(runnable).start()
    }

    private fun sendCDuration() {
        handler.post(sendCDurationR)
    }

    private fun sendPlayState(playState: PlayState) {
        refreshPi()
        val message = Message()
        message.what = YueTingConstant.PLAY_STATE
        when (playState) {
            PlayState.PAUSE -> {
                message.arg1 = 0
                refreshPlayView(YueTingConstant.PLAY_STATUS_PAUSE)
            }
            PlayState.PLAY -> {
                message.arg1 = 1
                refreshPlayView(YueTingConstant.PLAY_STATUS_PLAY)
            }
            PlayState.COMPLETE -> {
                message.arg1 = 2
            }
            PlayState.PREPARE -> {
                message.arg1 = 3
                val bundle = Bundle()
                bundle.putInt(YueTingConstant.SERVICE_PLAY_AUDIO_SESSION, mPlayer?.audioSessionId
                        ?: 0)
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
                msg.arg1 = ActionConstant.ACTION_SINGLE_REQ
            }
            PlayType.LIST -> {
                msg.arg1 = ActionConstant.ACTION_LIST_REQ
            }
            PlayType.RANDOM -> {
                msg.arg1 = ActionConstant.ACTION_RANDOM_REQ
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
        cPosition = Random().nextInt(musicInfoS.size)
    }

    private fun nextPosition() {
        cPosition =
                if (cPosition + 1 > musicInfoS.size - 1) {
                    0
                } else {
                    cPosition + 1
                }
    }

    private fun previousPosition() {
        cPosition =
                if (cPosition - 1 < 0) {
                    musicInfoS.size - 1
                } else {
                    cPosition - 1
                }
    }

    private fun changePlayType(action: String?) {
        when (action) {
            ActionConstant.ACTION_SINGLE -> {
                playType = PlayType.SINGLE
            }
            ActionConstant.ACTION_LIST -> {
                playType = PlayType.LIST
            }
            ActionConstant.ACTION_RANDOM -> {
                playType = PlayType.RANDOM
            }
        }
    }

    private fun changePlayState(action: String?, intent: Intent?) {
        when (action) {
            ActionConstant.ACTION_PLAY -> {
                cPosition = intent?.getIntExtra(YueTingConstant.SERVICE_MUSIC_ITEM_POSITION, cPosition) ?: cPosition
                if (cPosition == pPosition) {
                    pauseOrPlay()
                } else {
                    play()
                }
            }
            ActionConstant.ACTION_NEXT -> {
                next()
            }
            ActionConstant.ACTION_PREVIOUS -> {
                previous()
            }
            AudioManager.ACTION_AUDIO_BECOMING_NOISY -> {
                pause()
            }
        }
    }

    private fun getNewMusicInfo(action: String?, intent: Intent?) {
        if (action == ActionConstant.ACTION_REFRESH) {
            typeName = intent?.extras?.getString(ActionConstant.ACTION_REFRESH_KEY)
                    ?: DataConstant.DEFAULT_TYPE_NAME
            val position = intent?.extras?.getInt(YueTingConstant.SERVICE_MUSIC_ITEM_POSITION)
                    ?: 0
            refreshStatus(position)
            disposable = fetchMusicPath(typeName)
        }
    }

    private fun getMusicInfo(typeName: String, position: Int) {
        cPosition = position
        disposable = fetchMusicPath(typeName)
    }

    fun refreshMusic(musicInfoList: List<MusicInfo>) {
        musicInfoS.clear()
        musicInfoS.addAll(musicInfoList)
        sendPlayState(PlayState.START)
        play()
    }

    private fun refreshStatus(position: Int) {
        cPosition = position
        pPosition = -1
        cDuration = 0
        isPause = false
        isSame = false
        isLossFocus = false
        mPlayer?.run {
            if (isPlaying) {
                pause()
            }
        }
    }

    inner class PlayerReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (action == ActionConstant.ACTION_FINISH) {
                val msg = Message()
                msg.what = ActionConstant.ACTION_FINISH_REQ
                sendMessenger.send(msg)
                ActivityCollector.killApp()
                stopSelf()
            }
            changePlayType(action)
            changePlayState(action, intent)
            sendPlayType()
            getNewMusicInfo(action, intent)
        }
    }

    enum class PlayType {
        SINGLE, RANDOM, LIST
    }

    private enum class PlayDirection {
        NEXT, PREVIOUS
    }
}