package com.aqrlei.graduation.yueting.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.Messenger
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.view.AlphaListView
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.MusicInfo
import com.aqrlei.graduation.yueting.model.local.ReadMessage
import com.aqrlei.graduation.yueting.model.local.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.presenter.fragmentpresenter.TabHomePresenter
import com.aqrlei.graduation.yueting.ui.YueTingActivity
import com.aqrlei.graduation.yueting.ui.adapter.YueTingHomeListAdapter
import kotlinx.android.synthetic.main.activity_yueting.*
import kotlinx.android.synthetic.main.yueting_fragment_home.view.*

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/23
 */
/*
* @param mPresenter 访问Presenter
* @param mView 访问对应的布局
* @param mContainerActivity 访问对应的Activity
* */
class TabHomeFragment : MvpContract.MvpFragment<TabHomePresenter, YueTingActivity>(),
        AlphaListView.OnAlphaChangeListener, AdapterView.OnItemClickListener {
    override fun onItemClick(parent: AdapterView<*>?, convertView: View, position: Int, id: Long) {
        if (position < 3) {
            return
        }
        val realPosition = position - 3
        if (!isServiceStart) {
            startMusicService(realPosition)
            isServiceStart = true
        } else {
            sendPlayBroadcast(realPosition)
        }
    }

    override fun onAlphaChanged(percent: Float) {
        mContainerActivity.ll_tab_title.setBackgroundColor(
                Color.argb((175 * percent).toInt(), 113, 204, 180)
        )
    }

    private var mReadData = ArrayList<ReadMessage>()
    private var isServiceStart = false
    private var mMusicInfoShared = ShareMusicInfo.MusicInfoTool
    private lateinit var mAdapter: YueTingHomeListAdapter
    private lateinit var mLocalBroadcast: LocalBroadcastManager
    private lateinit var mPlayView: LinearLayout

    private val mHandler =
            object : Handler(
            ) {
                override fun handleMessage(msg: Message) {
                    if (msg.what == YueTingConstant.PLAY_STATE && msg.arg1 == 2) {
                        val temp = msg.obj
                    }
                    refreshPlayView(msg)
                }
            }


    override val mPresenter: TabHomePresenter
        get() = TabHomePresenter(this)
    override val layoutRes: Int
        get() = R.layout.yueting_fragment_home //activity_yueting

    companion object {
        fun newInstance(): TabHomeFragment {
            val args = Bundle()
            val fragment = TabHomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initComponents() {
        super.initComponents()
        initBroadcast()
        initView()


    }

    override fun onResume() {
        super.onResume()
        getMusicInfoFromDB()
    }

    private fun refreshPlayView(message: Message) {
        mPresenter.refreshPlayView(mPlayView, message)
    }

    private fun getMusicInfoFromDB() {
        mPresenter.getMusicInfoFromDB()
    }

    private fun initBroadcast() {
        mLocalBroadcast = LocalBroadcastManager.getInstance(mContainerActivity)
        mMusicInfoShared.setBroadcastManager(mLocalBroadcast)
    }

    private fun startMusicService(position: Int) {
        mPresenter.startMusicService(mContainerActivity, position, Messenger(mHandler))
    }

    private fun sendPlayBroadcast(position: Int) {
        val ACTION_PLAY = YueTingConstant.ACTION_BROADCAST[YueTingConstant.ACTION_PLAY]
        val playIntent = Intent(ACTION_PLAY)
        playIntent.putExtra("position", position)
        mLocalBroadcast.sendBroadcast(playIntent)

    }

    fun setMusicInfo(data: ArrayList<MusicInfo>) {
        mMusicInfoShared.setInfoS(data)
        mAdapter.notifyDataSetChanged()
    }

    private fun initView() {
        val mRecommendLv = mView.lv_fragment_home as AlphaListView

        mAdapter = YueTingHomeListAdapter(mContainerActivity,
                R.layout.listitem_title, R.layout.listitem_read, R.layout.listitem_music,
                mReadData, mMusicInfoShared.getInfoS())

        mRecommendLv.addHeaderView(LayoutInflater.from(mContainerActivity).
                inflate(R.layout.listheader_home, null))
        mRecommendLv.adapter = mAdapter
        mRecommendLv.onItemClickListener = this
        mRecommendLv.setAlphaChangeListener(this)
        mPlayView = mContainerActivity.window.decorView
                .findViewById(R.id.ll_play_control) as LinearLayout
    }
}