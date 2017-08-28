package com.aqrlei.graduation.yueting.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.widget.AdapterView
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.baselib.mvp.MvpContract
import com.aqrlei.graduation.yueting.baselib.util.AppLog
import com.aqrlei.graduation.yueting.baselib.util.AppToast
import com.aqrlei.graduation.yueting.baselib.util.view.AlphaListView
import com.aqrlei.graduation.yueting.model.local.MusicMessage
import com.aqrlei.graduation.yueting.model.local.ReadMessage
import com.aqrlei.graduation.yueting.presenter.fragmentpresenter.TabHomePresenter
import com.aqrlei.graduation.yueting.ui.YueTingActivity
import com.aqrlei.graduation.yueting.ui.adapter.YueTingHomeListAdapter
import kotlinx.android.synthetic.main.activity_animation.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
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
        TabLayout.OnTabSelectedListener, AlphaListView.OnAlphaChangeListener {
    override fun onAlphaChanged(percent: Float) {
        mContainerActivity.rg_anim_tab.setBackgroundColor(Color.argb((175 * percent).toInt(), 113, 204, 180))
    }

    private var mReadData = ArrayList<ReadMessage>()
    private var mMusicData = ArrayList<MusicMessage>()
    override fun onTabReselected(tab: TabLayout.Tab?) {
        AppToast.toastShow(mContainerActivity, tab?.text.toString(), 1000)
        tv_test_tab.text = tab?.text.toString()
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        //AppToast.toastShow(mContainerActivity, tab?.text.toString(), 1000)
        AppLog.logDebug(AppLog.LOG_TAG_FRAGMENT, "Unselected: \t" + tab?.text.toString())
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        AppToast.toastShow(mContainerActivity, tab?.text.toString(), 1000)
        tv_test_tab.text = tab?.text.toString()
    }

    override val mPresenter: TabHomePresenter
        get() = TabHomePresenter(this)
    override val layoutRes: Int
        get() = R.layout.yueting_fragment_home //activity_animation

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
        /*initTabAndFragment()*/
        initView()

    }

    private fun initView() {
        var mRecommendLv = mView.lv_fragment_home
        mMusicData.add(MusicMessage("", "这是歌名吧", "歌手名吧", 100))
        mMusicData.add(MusicMessage("", "这是歌名吧", "歌手名吧", 200))
        mMusicData.add(MusicMessage("", "这是歌名吧", "歌手名吧", 300))
        mMusicData.add(MusicMessage("", "这是歌名吧", "歌手名吧", 400))
        mReadData.add(ReadMessage("这是书名吧1", null))
        mReadData.add(ReadMessage("这是书名吧2", null))
        mReadData.add(ReadMessage("这是书名吧3", null))
        mRecommendLv.addHeaderView(LayoutInflater.from(mContainerActivity).
                inflate(R.layout.yueting_listheader_home, null))
        mRecommendLv.adapter = YueTingHomeListAdapter(mContainerActivity,
                R.layout.listitem_title, R.layout.listitem_read, R.layout.listitem_music,
                mReadData, mMusicData)
        mRecommendLv.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            AppToast.toastShow(mContainerActivity, " " + position, 1000)
        }
        mRecommendLv.setAlphaChangeListener(this)
    }

    private fun initTabAndFragment() {
        val mTabTitle = ArrayList<String>()
        mTabTitle.add("推荐")
        mTabTitle.add("悦读")
        mTabTitle.add("聆听")
        val mTabLayout = mView.tl_tab_header
        mTabLayout.addTab(mTabLayout.newTab().setText("推荐"), true)
        mTabLayout.addTab(mTabLayout.newTab().setText("电影"))
        mTabLayout.addTab(mTabLayout.newTab().setText("悦读"))
        mTabLayout.addTab(mTabLayout.newTab().setText("音乐"))
        mTabLayout.addTab(mTabLayout.newTab().setText("技术"))
        mTabLayout.addTab(mTabLayout.newTab().setText("消息"))
        mTabLayout.addTab(mTabLayout.newTab().setText("关注"))
        mTabLayout.addOnTabSelectedListener(this)

    }
}