package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import android.widget.LinearLayout
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.SendType
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.presenter.activitypresenter.YueTingActivityPresenter
import com.aqrlei.graduation.yueting.ui.fragment.TabHomeFragment
import android.os.Build.VERSION.SDK_INT




/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/23
 */
/*
* @param mPresenter 访问对应的Presenter
* */
class YueTingActivity : MvpContract.MvpActivity<YueTingActivityPresenter>()
        , View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_play_control -> {
                sendMusicBroadcast(SendType.PLAY)

            }
            R.id.tv_next -> {
                sendMusicBroadcast(SendType.NEXT)
            }
            R.id.tv_previous -> {
                sendMusicBroadcast(SendType.PREVIOUS)
            }
            R.id.tv_music_info -> {
                PlayActivity.jumpToPlayActivity(this@YueTingActivity)
            }
        }
    }

    override val mPresenter: YueTingActivityPresenter
        get() = YueTingActivityPresenter(this)
    override val layoutRes: Int
        get() = R.layout.activity_yueting

    private val mMusicShareInfo = ShareMusicInfo.MusicInfoTool
    private lateinit var mHandler: Handler
    private lateinit var mPlayView: LinearLayout
    private lateinit var mTabHomeFragment: TabHomeFragment

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        mHandler = mMusicShareInfo.getHandler(this)
        mPlayView = this.window.decorView
                .findViewById(R.id.ll_play_control) as LinearLayout
        initFragments(savedInstanceState)
        if (mMusicShareInfo.getSize() > 0) {
            if (mMusicShareInfo.isStartService()) {
                mPlayView.visibility = View.VISIBLE
            }
            initPlayView(mMusicShareInfo.getPosition(), mMusicShareInfo.getDuration())
        }
    }

    private fun initFragments(savedInstanceState: Bundle?) {
        mTabHomeFragment = if (savedInstanceState != null) {
            (supportFragmentManager
                    .findFragmentByTag(
                            YueTingConstant.TAB_FRAGMENT_TAGS[YueTingConstant.TAG_FRAGMENT_HOME])
                    ?: TabHomeFragment.newInstance()) as TabHomeFragment
        } else {
            TabHomeFragment.newInstance()
        }
        supportFragmentManager.beginTransaction().add(
                R.id.fl_fragment,
                mTabHomeFragment,
                YueTingConstant.TAB_FRAGMENT_TAGS[YueTingConstant.TAG_FRAGMENT_HOME])
                .commit()
    }
    private fun sendMusicBroadcast(type: SendType) {
        ShareMusicInfo.MusicInfoTool.sendBroadcast(this, type)
    }

    private fun initPlayView(position: Int, duration: Int = 0) {
        mMusicShareInfo.shareViewInit(mPlayView, position, duration)
    }

    override fun onDestroy() {
        super.onDestroy()
        DBManager.releaseCursor()
    }

    fun getMPlayView() = mPlayView

    companion object {
        fun jumpToYueTingActivity(context: Context, data: Int) {
            val intent = Intent(context, YueTingActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("code", data)
            if (IntentUtil.queryActivities(context, intent)) context.startActivity(intent)
        }
    }
}