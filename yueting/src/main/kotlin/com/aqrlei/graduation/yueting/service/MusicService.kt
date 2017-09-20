package com.aqrlei.graduation.yueting.service

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import com.aqrairsigns.aqrleilib.basemvp.BaseService
import com.aqrairsigns.aqrleilib.util.ImageUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.ui.YueTingActivity
import java.io.IOException

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
        val musicInfo = mMusicInfoShare.getInfo(position)
        remoteViews.setTextViewText(R.id.tv_title, musicInfo.title)
        remoteViews.setTextViewText(R.id.tv_artist_album, "${musicInfo.artist} - ${musicInfo.album}")
        val bitmap = ImageUtil.byteArrayToBitmap(musicInfo.picture)
        remoteViews.setImageViewBitmap(R.id.iv_album_picture, bitmap)
        startForeground(NOTIFICATION_ID, notification)
        mPlayer?.start()

    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        return true
    }

    override fun onCompletion(mp: MediaPlayer?) {

    }

    override fun onAudioFocusChange(focusChange: Int) {

    }


    companion object {
        private val mMusicInfoShare = ShareMusicInfo.MusicInfoTool
        private var mPlayer: MediaPlayer? = null
    }


    private var position: Int = 0
    private var playerReceiver: PlayerReceiver? = null
    private lateinit var remoteViews: RemoteViews
    private lateinit var notification: Notification
    private val NOTIFICATION_ID = 1
    override fun onBind(p0: Intent?): IBinder? {
        return super.onBind(p0)
    }

    override fun onCreate() {
        initPlayer()
        regReceiver()
        buildNotification()
        super.onCreate()
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val bundle = intent.extras as Bundle
        position = bundle.getInt("position")
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
        val pi = PendingIntent.getActivity(this, 0, intent, 0)
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
                .setContent(remoteViews)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pi)
                .setSmallIcon(R.mipmap.ic_launcher_round)//必须设置，不然无法显示自定义的View
                .build()

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
        if (mPlayer != null && mMusicInfoShare.getInfoS().size > 0) {
            val musicInfo = mMusicInfoShare.getInfoS()[position]
            mPlayer?.reset()
            try {
                mPlayer?.setDataSource(musicInfo.albumUrl)
                mPlayer?.prepareAsync()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun pause() {
        mPlayer?.pause()
    }


    inner class PlayerReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            val ACTION = YueTingConstant.ACTION_BROADCAST
            val action = intent?.action
            when (action) {
                ACTION[YueTingConstant.ACTION_PLAY] -> {
                    if (mPlayer != null && mPlayer!!.isPlaying) {
                        pause()
                    } else {
                        play()
                    }

                }
                ACTION[YueTingConstant.ACTION_NEXT] -> {

                }
                ACTION[YueTingConstant.ACTION_PREVIOUS] -> {

                }
                ACTION[YueTingConstant.ACTION_FINISH] -> {

                }
                ACTION[YueTingConstant.ACTION_SINGLE] -> {

                }
                ACTION[YueTingConstant.ACTION_LIST] -> {

                }
                ACTION[YueTingConstant.ACTION_RANDOM] -> {

                }
            }
        }

    }


}