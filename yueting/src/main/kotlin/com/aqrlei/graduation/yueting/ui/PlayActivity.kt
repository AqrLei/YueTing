package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.media.audiofx.Visualizer
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ImageView
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.basemvp.MvpContract
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.enumtype.SendType
import com.aqrlei.graduation.yueting.model.LrcInfo
import com.aqrlei.graduation.yueting.model.infotool.LrcInfoProcess
import com.aqrlei.graduation.yueting.model.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.presenter.PlayPresenter
import com.aqrlei.graduation.yueting.ui.adapter.YueTingListAdapter
import com.aqrlei.graduation.yueting.util.*
import kotlinx.android.synthetic.main.music_activity_play.*
import kotlinx.android.synthetic.main.music_include_yue_ting_play.*

/**
 * Author : AqrLei
 * Date : 2017/9/24.
 */
class PlayActivity :
        MvpContract.MvpActivity<PlayPresenter>(),
        View.OnClickListener,
        AdapterView.OnItemClickListener,
        Visualizer.OnDataCaptureListener {
    companion object {
        fun jumpToPlayActivity(context: Context) {
            val bundle = Bundle()
            val intent = Intent(context, PlayActivity::class.java)
            intent.putExtra(YueTingConstant.SERVICE_PLAY_STATUS_B, bundle)
            if (IntentUtil.queryActivities(context, intent)) context.startActivity(intent)
        }
    }

    override val mPresenter: PlayPresenter
        get() = PlayPresenter(this)
    override val layoutRes: Int
        get() = R.layout.music_activity_play
    private val mBundle: Bundle
            by lazy { intent.getBundleExtra(YueTingConstant.SERVICE_PLAY_STATUS_B) }
    private var mVisualizer: Visualizer? = null
    private lateinit var mPlayView: ViewGroup
    private val mMusicShareInfo = ShareMusicInfo.MusicInfoTool

    override fun onFftDataCapture(visualizer: Visualizer?, fft: ByteArray?, samplingRate: Int) {}

    override fun onWaveFormDataCapture(visualizer: Visualizer?, waveform: ByteArray?, samplingRate: Int) {
        vv_play.bytes = waveform
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backIv -> {
                this.finish()
            }
            R.id.playControlIv -> {
                sendMusicBroadcast(SendType.PLAY, this)

            }
            R.id.nextIv -> {
                sendMusicBroadcast(SendType.NEXT, this)
            }
            R.id.previousIv -> {
                sendMusicBroadcast(SendType.PREVIOUS, this)
            }
            R.id.playTypeIv -> {
                when (playTypeIv.drawable.level) {
                    YueTingConstant.PLAY_TYPE_REPEAT_ONE -> {
                        sendMusicBroadcast(SendType.PLAY_TYPE, this, YueTingConstant.PLAY_LIST)
                    }
                    YueTingConstant.PLAY_TYPE_REPEAT -> {
                        sendMusicBroadcast(SendType.PLAY_TYPE, this, YueTingConstant.PLAY_RANDOM)
                    }
                    YueTingConstant.PLAY_TYPE_RANDOM -> {
                        sendMusicBroadcast(SendType.PLAY_TYPE, this, YueTingConstant.PLAY_SINGLE)
                    }
                }
            }
            R.id.expandListIv -> {
                playListLv.visibility =
                        if (playListLv.visibility == View.GONE) {
                            expandListIv.setImageLevel(YueTingConstant.PLAY_EXPAND_CLOSE)
                            View.VISIBLE
                        } else {
                            expandListIv.setImageLevel(YueTingConstant.PLAY_EXPAND)
                            View.GONE
                        }
            }
            R.id.switchFl -> {
                lrcLv.visibility = if (lrcLv.visibility == View.VISIBLE) {
                    vv_play.visibility = View.VISIBLE
                    View.GONE
                } else {
                    vv_play.visibility = View.GONE
                    View.VISIBLE
                }
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        sendPlayBroadcast(position, this)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish()
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        init()
    }

    fun getMPlayView() = mPlayView

    fun refreshLrc(index: Int) {
        runOnUiThread {
            lrcLv.setIndex(index)
        }
    }

    fun refresh() {
        refreshVisualizer()
        refreshLrcView()
    }

    private fun init() {
        ll_play_control.visibility = View.VISIBLE
        playTypeIv.visibility = View.VISIBLE
        mMusicShareInfo.getHandler(this)
        mPlayView = this.window.decorView.findViewById(R.id.ll_play_control) as ViewGroup
        mBundle.getIntArray(YueTingConstant.SERVICE_PLAY_STATUS)?.let {
            //PlayActivity privately-owned
            val initArray = it
            mMusicShareInfo.isStartService(true)
            mMusicShareInfo.setPosition(initArray[0])
            mMusicShareInfo.setAudioSessionId(initArray[1])
            changePlayType(initArray[2])
            changePlayState(initArray[3])//0 or 1
        }
        mBundle.getString(YueTingConstant.FRAGMENT_TITLE_VALUE)?.let {
            BackStackHolder.typeName = it
        }
        (mPlayView.findViewById(R.id.playTypeIv) as ImageView).setImageLevel(mMusicShareInfo.getPlayType())
        initPlayView(mPlayView, mMusicShareInfo.getPosition(), mMusicShareInfo.getDuration())
        initPopView()
        initListener()
        lrcLv.animation = AnimationUtils.loadAnimation(this@PlayActivity, R.anim.lrc_show_alpha)
        refreshLrcView()
        refreshVisualizer(mMusicShareInfo.getAudioSessionId())
    }

    private fun initPopView() {
        playListLv.adapter = YueTingListAdapter(ShareMusicInfo.MusicInfoTool.getInfoS(), this, R.layout.music_list_item, 1)
        playListLv.onItemClickListener = this

    }

    private fun initListener() {
        backIv.setOnClickListener(this)
        playControlIv.setOnClickListener(this)
        nextIv.setOnClickListener(this)
        previousIv.setOnClickListener(this)
        playTypeIv.setOnClickListener(this)
        expandListIv.setOnClickListener(this)
        switchFl.setOnClickListener(this)
    }

    private fun refreshLrcView() {
        val path = mMusicShareInfo.getInfo(mMusicShareInfo.getPosition()).albumUrl
        val lrcList = ArrayList<LrcInfo>().apply {
            addAll(LrcInfoProcess.readLRC(path))
        }
        lrcLv?.setLrcList(lrcList)
    }

    private fun refreshVisualizer(audioSessionId: Int = mMusicShareInfo.getAudioSessionId()) {
        mVisualizer = Visualizer(audioSessionId)
        mVisualizer?.let {
            if (it.enabled) {
                it.enabled = false
            }
            /**
             * getCaptureSizeRange()
             * [0] 128
             * [1] 1024
             * Size: 2
             * */
            it.captureSize = Visualizer.getCaptureSizeRange()[1]
            /**
             * getMaxCaptureRate()
             * 20000
             * */
            mVisualizer?.setDataCaptureListener(this, Visualizer.getMaxCaptureRate(), true, true)
            it.enabled = true
        }

    }

    private fun changePlayType(type: Int) {
        val iv = (mPlayView.findViewById(R.id.playTypeIv) as ImageView)
        mMusicShareInfo.changePlayType(type, iv)
    }

    private fun changePlayState(state: Int = 0) {
        mMusicShareInfo.changePlayState(state, mPlayView)
    }
}