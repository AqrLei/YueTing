package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.media.audiofx.Visualizer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.IntentUtil
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
class PlayActivity :
        MvpContract.MvpActivity<PlayActivityPresenter>(),
        View.OnClickListener,
        Visualizer.OnDataCaptureListener {
    override fun onFftDataCapture(visualizer: Visualizer?, fft: ByteArray?, samplingRate: Int) {

    }

    override fun onWaveFormDataCapture(visualizer: Visualizer?, waveform: ByteArray?, samplingRate: Int) {
        vv_play.bytes = waveform
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                this.finish()
            }
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
    private var mVisualizer: Visualizer? = null
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

    private fun setVisualizer(audioSessionId: Int) {

        mVisualizer = Visualizer(audioSessionId)
        if (mVisualizer!!.enabled) {
            mVisualizer?.enabled = false
        }
        /*
        * getCaptureSizeRange()
        * [0] 128
        * [1] 1024
        * Size: 2
        * */
        mVisualizer?.captureSize = Visualizer.getCaptureSizeRange()[1]
        /*
        * getMaxCaptureRate()
        * 20000
        * */
        mVisualizer?.setDataCaptureListener(this, Visualizer.getMaxCaptureRate(), true, true)

        mVisualizer?.enabled = true
    }

    private fun init() {
        mHandler = mMusicShareInfo.getHandler(this)
        val mBundle = intent.getBundleExtra("init_bundle")

        mPlayView = this.window.decorView.findViewById(R.id.ll_play_control) as LinearLayout
        if (mBundle.getIntArray("init") != null) {
            val initArray = mBundle.getIntArray("init")
            mMusicShareInfo.setPosition(initArray[0])
            mMusicShareInfo.setAudioSessionId(initArray[1])
            changePlayType(initArray[2])
            changePlayState(initArray[3])
        }
        (mPlayView.findViewById(R.id.tv_play_type) as TextView).text =
                mMusicShareInfo.getPlayType()
        initPlayView(mMusicShareInfo.getPosition(), mMusicShareInfo.getDuration())
        setVisualizer(mMusicShareInfo.getAudioSessionId())
    }

    private fun initPlayView(position: Int, duration: Int = 0) {
        mMusicShareInfo.shareViewInit(mPlayView, position, duration)


    }

    private fun changePlayType(type: Int) {
        val tv = (mPlayView.findViewById(R.id.tv_play_type) as TextView)
        when (type) {
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

    private fun changePlayState(state: Int = 0, msg: Message = Message()) {
        when (state) {
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
                (mPlayView.findViewById(R.id.tv_play_type) as TextView).text =
                        mMusicShareInfo.getPlayType()
                initPlayView(position)
                val audioSessionId = msg.data["audioSessionId"] as Int
                setVisualizer(audioSessionId)
                mMusicShareInfo.setAudioSessionId(audioSessionId)
            }
        }
    }

    fun refreshPlayView(msg: Message) {
        if (msg.what == YueTingConstant.CURRENT_DURATION) {//can be shared
            (mPlayView.findViewById(R.id.rb_progress_play) as RoundBar).setProgress(msg.arg1.toFloat())
        }
        if (msg.what == YueTingConstant.PLAY_STATE) {//can be shared
            changePlayState(msg.arg1, msg)
        }
        if (msg.what == YueTingConstant.PLAY_TYPE) {//PlayActivity privately-owned
            changePlayType(msg.arg1)
        }
    }

    private fun sendMusicBroadcast(type: SendType, code: Int = 1) {
        mMusicShareInfo.sendBroadcast(this, type, code)
    }

    companion object {
        fun jumpToPlayActivity(context: Context) {
            val bundle = Bundle()
            val intent = Intent(context, PlayActivity::class.java)
            intent.putExtra("init_bundle", bundle)
            if (IntentUtil.queryActivities(context, intent)) context.startActivity(intent)
        }
    }
}