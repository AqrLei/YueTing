package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.ImageUtil
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrairsigns.aqrleilib.util.StringChangeUtil
import com.aqrairsigns.aqrleilib.view.RoundBar
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.PlayState
import com.aqrlei.graduation.yueting.constant.SendType
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.presenter.activitypresenter.PlayActivityPresenter
import kotlinx.android.synthetic.main.activity_play.*

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/9/24.
 */
class PlayActivity : MvpContract.MvpActivity<PlayActivityPresenter>(),
        View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_play_control -> {
                sendMusicBroadcast(SendType.PLAY)

            }
            R.id.tv_next -> {
                sendMusicBroadcast(SendType.NEXT)
            }
            R.id.tv_previous -> {
                sendMusicBroadcast(SendType.PREVIOUS)
            }
            R.id.tv_play_type -> {
                val tv = tv_play_type as TextView
                when (tv.text.toString()) {
                    "单" -> {
                        sendMusicBroadcast(SendType.PLAY_TYPE, 1)
                    }
                    "表" -> {
                        sendMusicBroadcast(SendType.PLAY_TYPE, 2)
                    }
                    "变" -> {
                        sendMusicBroadcast(SendType.PLAY_TYPE, 0)
                    }
                }

            }
        }
    }

    override val mPresenter: PlayActivityPresenter
        get() = PlayActivityPresenter(this)
    override val layoutRes: Int
        get() = R.layout.activity_play

    private lateinit var mHandler: Handler
    private lateinit var mPlayView: LinearLayout
    private val mMusicShareInfo = ShareMusicInfo.MusicInfoTool

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        init()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish()
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun init() {
        mHandler = mMusicShareInfo.getHandler(this)
        mPlayView = this.window.decorView.findViewById(R.id.ll_play_control) as LinearLayout
        initPlayView(mMusicShareInfo.getPosition(), mMusicShareInfo.getDuration())
    }

    private fun initPlayView(position: Int, duration: Int = 0) {

        val musicInfo = mMusicShareInfo.getInfo(position)
        val roundBar = mPlayView.findViewById(R.id.rb_progress_play) as RoundBar
        val playState = mMusicShareInfo.getPlayState()
        val bitmap = ImageUtil.byteArrayToBitmap(musicInfo.picture)
        val maxProgress = musicInfo.duration.toFloat()

        roundBar.setMaxProgress(maxProgress)
        roundBar.setProgress(duration.toFloat())
        (mPlayView.findViewById(R.id.tv_play_type) as TextView).text = mMusicShareInfo.getPlayType()
        (mPlayView.findViewById(R.id.tv_play_control) as TextView).text =
                if (PlayState.PAUSE == playState) "播" else "停"
        (mPlayView.findViewById(R.id.iv_album_picture) as ImageView).setImageBitmap(bitmap)
        (mPlayView.findViewById(R.id.tv_music_info) as TextView).text =
                StringChangeUtil.SPANNABLE.clear()
                        .foregroundColorChange("#1c4243", musicInfo.title)
                        .relativeSizeChange(2 / 3F, "\n${musicInfo.artist} - ${musicInfo.album}")
                        .complete()
    }

    fun refreshPlayView(msg: Message) {
        if (msg.what == YueTingConstant.CURRENT_DURATION) {
            (mPlayView.findViewById(R.id.rb_progress_play) as RoundBar).setProgress(msg.arg1.toFloat())
        }
        if (msg.what == YueTingConstant.PLAY_STATE) {
            when (msg.arg1) {
                0 -> {//PAUSE
                    (mPlayView.findViewById(R.id.tv_play_control) as TextView).text = "播"
                    mMusicShareInfo.setPlayState(PlayState.PAUSE)
                }
                1 -> {//PLAY
                    (mPlayView.findViewById(R.id.tv_play_control) as TextView).text = "停"
                    mMusicShareInfo.setPlayState(PlayState.PLAY)
                }
                2 -> {//COMPLETE
                    //(mPlayView.findViewById(R.id.rb_progress_play) as RoundBar).setProgress(0F)
                }
                3 -> {//PREPARE
                    // position, cDuration
                    val position = msg.arg2
                    initPlayView(position)
                }
            }
        }

        if (msg.what == YueTingConstant.PLAY_TYPE) {
            val tv = (mPlayView.findViewById(R.id.tv_play_type) as TextView)
            when (msg.arg1) {
                YueTingConstant.ACTION_SINGLE -> {
                    tv.text = "单"
                    mMusicShareInfo.setPlayType("单")
                }
                YueTingConstant.ACTION_LIST -> {
                    tv.text = "表"
                    mMusicShareInfo.setPlayType("表")
                }
                YueTingConstant.ACTION_RANDOM -> {
                    tv.text = "变"
                    mMusicShareInfo.setPlayType("变")
                }
            }
        }
    }

    private fun sendMusicBroadcast(type: SendType, code: Int = 1) {
        mMusicShareInfo.sendBroadcast(this, type, code)
    }

    companion object {
        fun jumpToPlayActivity(context: Context) {
            val intent = Intent(context, PlayActivity::class.java)
            if (IntentUtil.queryActivities(context, intent)) context.startActivity(intent)
        }
    }
}