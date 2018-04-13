package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.ActivityCollector
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.enumtype.SendType
import com.aqrlei.graduation.yueting.model.local.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.presenter.activitypresenter.YueTingActivityPresenter
import com.aqrlei.graduation.yueting.ui.adapter.YueTingListAdapter
import com.aqrlei.graduation.yueting.ui.fragment.TabHomeFragment
import com.aqrlei.graduation.yueting.ui.uiEt.initPlayView
import com.aqrlei.graduation.yueting.ui.uiEt.sendMusicBroadcast
import com.aqrlei.graduation.yueting.ui.uiEt.sendPlayBroadcast
import kotlinx.android.synthetic.main.music_include_yueting_play.*


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
        , View.OnClickListener, AdapterView.OnItemClickListener {

    companion object {
        fun jumpToYueTingActivity(context: Context) {
            val intent = Intent(context, YueTingActivity::class.java)
            if (IntentUtil.queryActivities(context, intent)) context.startActivity(intent)
        }
    }


    override val mPresenter: YueTingActivityPresenter
        get() = YueTingActivityPresenter(this)
    override val layoutRes: Int
        get() = R.layout.main_activity_yueting
    private val mMusicShareInfo = ShareMusicInfo.MusicInfoTool
    private lateinit var mHandler: Handler
    private lateinit var mPlayView: ViewGroup
    private lateinit var mTabHomeFragment: TabHomeFragment


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        sendPlayBroadcast(position, this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_play_control -> {
                sendMusicBroadcast(SendType.PLAY, this)
            }
            R.id.tv_next -> {
                sendMusicBroadcast(SendType.NEXT, this)
            }
            R.id.tv_previous -> {
                sendMusicBroadcast(SendType.PREVIOUS, this)
            }
            R.id.tv_music_info -> {
                PlayActivity.jumpToPlayActivity(this@YueTingActivity)
            }
            R.id.popUpWinTv -> {
                playListLv.visibility = if (playListLv.visibility == View.GONE) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityCollector.killApp()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == YueTingConstant.YUE_TING_FILE_RES) {
            if (requestCode == YueTingConstant.YUE_TING_FILE_REQ) {
                if (data?.extras?.getBoolean(YueTingConstant.FILE_BOOK_CHANGE) == true) {
                    mTabHomeFragment.changeBookAdapter()
                }
                if (data?.extras?.getBoolean(YueTingConstant.FILE_MUSIC_CHANGE) == true) {
                    mTabHomeFragment.changeMusicAdapter()
                }
            }
        }
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        popUpWinTv.setOnClickListener(this)
        mHandler = mMusicShareInfo.getHandler(this)
        mPlayView = this.window.decorView
                .findViewById(R.id.ll_play_control) as ViewGroup
        initFragments(savedInstanceState)
        if (mMusicShareInfo.getSize() > 0) {
            if (mMusicShareInfo.isStartService()) {
                mPlayView.visibility = View.VISIBLE
            }

            initPlayView(mPlayView, mMusicShareInfo.getPosition(), mMusicShareInfo.getDuration())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        DBManager.releaseCursor()
    }

    private fun initFragments(savedInstanceState: Bundle?) {
        mTabHomeFragment = if (savedInstanceState != null) {
            (supportFragmentManager
                    .findFragmentByTag(
                            YueTingConstant.TAB_FRAGMENT_HOME)
                    ?: TabHomeFragment.newInstance()) as TabHomeFragment
        } else {
            TabHomeFragment.newInstance()
        }

        if (!mTabHomeFragment.isAdded &&
                supportFragmentManager.findFragmentByTag(
                        YueTingConstant.TAB_FRAGMENT_HOME)
                == null) {
            supportFragmentManager.beginTransaction().add(
                    R.id.fl_fragment,
                    mTabHomeFragment,
                    YueTingConstant.TAB_FRAGMENT_HOME)
                    .commit()
        }
    }

    fun initPlayListView(adapter: YueTingListAdapter) {
        playListLv.adapter = adapter
        playListLv.onItemClickListener = this
    }

    fun getMPlayView() = mPlayView
}