package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.SendType
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.presenter.activitypresenter.YueTingActivityPresenter
import kotlinx.android.synthetic.main.layout_yueting_header.*


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
        , RadioGroup.OnCheckedChangeListener
        , View.OnClickListener {
    override fun onCheckedChanged(radioGroup: RadioGroup?, checkedId: Int) {
        (0 until radioGroup!!.childCount)
                .filter { radioGroup.getChildAt(it).id == checkedId }
                .forEach {
                    changeFragment(it)
                    titleName = (radioGroup.getChildAt(it) as RadioButton).text.toString()
                }
    }

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
            R.id.tv_file_local -> {
                FileActivity.jumpToFileActivity(this@YueTingActivity)
            }
        }
    }

    override val mPresenter: YueTingActivityPresenter
        get() = YueTingActivityPresenter(this)
    override val layoutRes: Int
        get() = R.layout.activity_yueting


    private var mFragments = ArrayList<Fragment>()
    private val mMusicShareInfo = ShareMusicInfo.MusicInfoTool
    private var titleName: String = ""
    private lateinit var mHandler: Handler
    private lateinit var mPlayView: LinearLayout
    private lateinit var mFragmentManager: FragmentManager

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        mHandler = mMusicShareInfo.getHandler(this)
        mFragmentManager = supportFragmentManager
        mPlayView = this.window.decorView
                .findViewById(R.id.ll_play_control) as LinearLayout
        initFragments(savedInstanceState)
        if (mMusicShareInfo.getSize() > 0) {
            initPlayView(mMusicShareInfo.getPosition(), mMusicShareInfo.getDuration())
        }
        rg_anim_tab.setOnCheckedChangeListener(this)
    }

    private fun initFragments(savedInstanceState: Bundle?) {
        mPresenter.initFragments(savedInstanceState, mFragmentManager)
        changeFragment(YueTingConstant.TAG_FRAGMENT_HOME)
    }

    private fun changeFragment(position: Int) {
        for (i in mFragments.indices) {
            val currentFragment = mFragments[i]
            val ft = mFragmentManager.beginTransaction()
            if (i == position) {
                if (!currentFragment.isAdded) {
                    ft.add(R.id.fl_fragment, currentFragment, YueTingConstant.TAB_FRAGMENT_TAGS[i])
                }
                ft.show(currentFragment)
            } else {
                ft.hide(currentFragment)
            }
            ft.commit()
        }
    }

    private fun sendMusicBroadcast(type: SendType) {
        ShareMusicInfo.MusicInfoTool.sendBroadcast(this, type)
    }

    fun setFragments(fragments: ArrayList<Fragment>) {
        mFragments = fragments
    }


    private fun initPlayView(position: Int, duration: Int = 0) {
        mMusicShareInfo.shareViewInit(mPlayView, position, duration)
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