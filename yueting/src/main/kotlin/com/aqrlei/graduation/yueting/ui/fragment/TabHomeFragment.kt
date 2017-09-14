package com.aqrlei.graduation.yueting.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.AdapterView
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrairsigns.aqrleilib.view.AlphaListView
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.model.local.MusicMessage
import com.aqrlei.graduation.yueting.model.local.ReadMessage
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
        AlphaListView.OnAlphaChangeListener {
    override fun onAlphaChanged(percent: Float) {
        mContainerActivity.ll_tab_title.setBackgroundColor(
                Color.argb((175 * percent).toInt(), 113, 204, 180)
        )
    }

    private var mReadData = ArrayList<ReadMessage>()
    private var mMusicData = ArrayList<MusicMessage>()

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
        initView()

    }

    private fun initView() {
        var mRecommendLv = mView.lv_fragment_home as AlphaListView
        mMusicData.add(MusicMessage("", "这是歌名吧", "歌手名吧", 100))
        mMusicData.add(MusicMessage("", "这是歌名吧", "歌手名吧", 200))
        mMusicData.add(MusicMessage("", "这是歌名吧", "歌手名吧", 300))
        mMusicData.add(MusicMessage("", "这是歌名吧", "歌手名吧", 400))
        mReadData.add(ReadMessage("这是书名吧1", null))
        mReadData.add(ReadMessage("这是书名吧2", null))
        mReadData.add(ReadMessage("这是书名吧3", null))
        mRecommendLv.addHeaderView(LayoutInflater.from(mContainerActivity).
                inflate(R.layout.listheader_home, null))
        mRecommendLv.adapter = YueTingHomeListAdapter(mContainerActivity,
                R.layout.listitem_title, R.layout.listitem_read, R.layout.listitem_music,
                mReadData, mMusicData)
        mRecommendLv.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            AppToast.toastShow(mContainerActivity, " " + position, 1000)
        }
        mRecommendLv.setAlphaChangeListener(this)
    }
}