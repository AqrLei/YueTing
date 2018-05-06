package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.enumtype.SendType
import com.aqrlei.graduation.yueting.model.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.presenter.YueTingPresenter
import com.aqrlei.graduation.yueting.ui.adapter.YueTingListAdapter
import com.aqrlei.graduation.yueting.ui.fragment.TabHomeFragment
import com.aqrlei.graduation.yueting.util.BackStackHolder
import com.aqrlei.graduation.yueting.util.initPlayView
import com.aqrlei.graduation.yueting.util.sendMusicBroadcast
import com.aqrlei.graduation.yueting.util.sendPlayBroadcast
import kotlinx.android.synthetic.main.music_include_yue_ting_play.*


/**
 * @Author: AqrLei
 * @Date: 2017/8/23
 */
class YueTingActivity : MvpContract.MvpActivity<YueTingPresenter>()
        , View.OnClickListener, AdapterView.OnItemClickListener {
    companion object {
        fun jumpToYueTingActivity(context: Context, type: String, name: String) {
            val bundle = Bundle()
            bundle.apply {
                putString(YueTingConstant.FRAGMENT_TITLE_TYPE, type)
                putString(YueTingConstant.FRAGMENT_TITLE_VALUE, name)
            }
            val intent = Intent(context, YueTingActivity::class.java)
            intent.putExtras(bundle)
            if (IntentUtil.queryActivities(context, intent)) context.startActivity(intent)
        }
    }

    override val mPresenter: YueTingPresenter
        get() = YueTingPresenter(this)
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
            R.id.playControlIv -> {
                sendMusicBroadcast(SendType.PLAY, this)
            }
            R.id.nextIv -> {
                sendMusicBroadcast(SendType.NEXT, this)
            }
            R.id.previousIv -> {
                sendMusicBroadcast(SendType.PREVIOUS, this)
            }
            R.id.musicInfoLl -> {
                PlayActivity.jumpToPlayActivity(this@YueTingActivity)
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
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == YueTingConstant.YUE_TING_FILE_RES) {
            if (requestCode == YueTingConstant.YUE_TING_FILE_REQ) {
                if (data?.extras?.getBoolean(YueTingConstant.FILE_BOOK_CHANGE) == true) {
                    mTabHomeFragment.updateBookFinish(true)
                }
                if (data?.extras?.getBoolean(YueTingConstant.FILE_MUSIC_CHANGE) == true) {
                    mTabHomeFragment.updateMusicFinish(true)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        mHandler = mMusicShareInfo.getHandler(this)
        mPlayView = this.window.decorView
                .findViewById(R.id.ll_play_control) as ViewGroup
        initFragments(savedInstanceState)
        initListener()
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

    fun initPlayListView(adapter: YueTingListAdapter) {
        playListLv.adapter = adapter
        playListLv.onItemClickListener = this
    }

    fun getMPlayView() = mPlayView

    fun setMusicTitle(musicName: String) {
        mTabHomeFragment.setMusicTitle(musicName)
    }
    fun dismissDialog(){
        mTabHomeFragment.dismissDialog()
    }


    private fun initListener() {
        nextIv.setOnClickListener(this)
        previousIv.setOnClickListener(this)
        playControlIv.setOnClickListener(this)
        musicInfoLl.setOnClickListener(this)
        expandListIv.setOnClickListener(this)
    }

    private fun initFragments(savedInstanceState: Bundle?) {
        val type = intent.extras?.getString(YueTingConstant.FRAGMENT_TITLE_TYPE)
                ?: YueTingConstant.FRAGMENT_TITLE_TYPE_MUSIC
        val name = intent.extras?.getString(YueTingConstant.FRAGMENT_TITLE_VALUE) ?: BackStackHolder.typeName
        mTabHomeFragment = if (savedInstanceState != null) {
            (supportFragmentManager
                    .findFragmentByTag(
                            YueTingConstant.TAB_FRAGMENT_HOME)
                    ?: TabHomeFragment.newInstance(type, name)) as TabHomeFragment
        } else {
            TabHomeFragment.newInstance(type, name)
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


}