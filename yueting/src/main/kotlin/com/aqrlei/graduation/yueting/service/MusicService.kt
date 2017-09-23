package com.aqrlei.graduation.yueting.service

import android.app.Notification
import android.app.PendingIntent
import android.content.*
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.*
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.widget.RemoteViews
import com.aqrairsigns.aqrleilib.basemvp.BaseService
import com.aqrairsigns.aqrleilib.util.ActivityCollector
import com.aqrairsigns.aqrleilib.util.ImageUtil
import com.aqrairsigns.aqrleilib.util.StringChangeUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.aidl.IMusicInfo
import com.aqrlei.graduation.yueting.aidl.MusicInfo
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.ui.MainActivity
import com.aqrlei.graduation.yueting.ui.YueTingActivity
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
        sendPlayState(PlayState.PREPARE)
        if (!isPause) {
            mPlayer?.start()
            sendPlayState(PlayState.PLAY)
        } else {
            sendPlayState(PlayState.PAUSE)
        }
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        if (mp != null) {
            mp.release()
            mp.reset()
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
    private val handler = Handler()
    private lateinit var remoteViews: RemoteViews
    private lateinit var notification: Notification
    private lateinit var sendMessenger: Messenger
    private val NOTIFICATION_ID = 1
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
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {

        if (mPlayer != null) {
            mPlayer?.release()
            mPlayer = null
        }
        stopForeground(true)
        if (playerReceiver != null) {
            unregisterReceiver(playerReceiver)
        }
        super.onDestroy()
    }


    private fun regReceiver() {
        playerReceiver = PlayerReceiver()
        val filter = IntentFilter()
        YueTingConstant.ACTION_BROADCAST.forEach {
            filter.addAction(it)
        }
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

    private fun buildNotification() {
        val intent = Intent(this, YueTingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(YueTingActivity::class.java)
        stackBuilder.addNextIntent(intent)

        val pi = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        //val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        remoteViews = RemoteViews(this.packageName, R.layout.notification_foreground)
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
                .setContentIntent(pi)
                .setContent(remoteViews)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)//必须设置，不然无法显示自定义的View
                .build()

        startForeground(NOTIFICATION_ID, notification)
    }


    private fun makeIntentStack(): Array<Intent?> {
        val intents = arrayOfNulls<Intent>(2)
        intents[0] = Intent.makeRestartActivityTask(ComponentName(this,
                com.aqrlei.graduation.yueting.ui.MainActivity::class.java))
        /* intents[0]?.action = Intent.ACTION_MAIN
         intents[0]?.addCategory(Intent.CATEGORY_LAUNCHER)
         intents[0]?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED*/

        intents[1] = Intent(this,
                com.aqrlei.graduation.yueting.ui.YueTingActivity::class.java)
        return intents
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
                    mPlayer?.start()
                }
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
            }
            PlayState.PLAY -> {
                message.arg1 = 1
            }
            PlayState.COMPLETE -> {
                message.arg1 = 2
            }
            PlayState.PREPARE -> {
                message.arg1 = 3
            }
        }
        message.arg2 = cPosition
        sendMessenger.send(message)
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

    inner class PlayerReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            val ACTION = YueTingConstant.ACTION_BROADCAST
            val action = intent?.action
            when (action) {
                ACTION[YueTingConstant.ACTION_PLAY] -> {
                    cPosition = intent.getIntExtra("position", cPosition)
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


    }

    inner class MusicBinder : IMusicInfo.Stub() {
        override fun setMusicInfo(infoS: MutableList<MusicInfo>?) {
            val musicInfoS = infoS as ArrayList<MusicInfo>
            mMusicInfoShare.setInfoS(musicInfoS)
        }
    }

    enum class PlayType {
        SINGLE, RANDOM, LIST
    }

    enum class PlayState {
        PAUSE, PLAY, COMPLETE, PREPARE
    }

    private enum class PlayDirection {
        NEXT, PREVIOUS
    }

}