package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.media.audiofx.Visualizer
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.SendType
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
        if (mBundle.getIntArray("init") != null) {//PlayActivity privately-owned
            val initArray = mBundle.getIntArray("init")
            mMusicShareInfo.isStartService(true)
            mMusicShareInfo.setPosition(initArray[0])
            mMusicShareInfo.setAudioSessionId(initArray[1])
            changePlayType(initArray[2])
            changePlayState(initArray[3])//0 or 1
        }
        (mPlayView.findViewById(R.id.tv_play_type) as TextView).text =
                mMusicShareInfo.getPlayType()// can be shared
        initPlayView(mMusicShareInfo.getPosition(), mMusicShareInfo.getDuration())
        setVisualizer(mMusicShareInfo.getAudioSessionId())
    }

    private fun initPlayView(position: Int, duration: Int = 0) {
        mMusicShareInfo.shareViewInit(mPlayView, position, duration)
    }

    private fun changePlayType(type: Int) {
        val tv = (mPlayView.findViewById(R.id.tv_play_type) as TextView)
        mMusicShareInfo.changePlayType(type, tv)
    }

    private fun changePlayState(state: Int = 0) {
        mMusicShareInfo.changePlayState(state, mPlayView)
    }


    fun getMPlayView() = mPlayView

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