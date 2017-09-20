package com.aqrlei.graduation.yueting.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.view.AlphaListView
import com.aqrlei.graduation.yueting.R
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
        startMusicService(position - 3)
    }

    override fun onAlphaChanged(percent: Float) {
        mContainerActivity.ll_tab_title.setBackgroundColor(
                Color.argb((175 * percent).toInt(), 113, 204, 180)
        )
    }

    private var mReadData = ArrayList<ReadMessage>()
    private var mMusicInfoShared = ShareMusicInfo.MusicInfoTool
    private lateinit var mAdapter: YueTingHomeListAdapter

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

    override fun onResume() {
        super.onResume()
        getMusicInfoFromDB()
    }

    private fun getMusicInfoFromDB() {
        mPresenter.getMusicInfoFromDB()
    }

    private fun startMusicService(position: Int) {
        mPresenter.startMusicService(mContainerActivity, position)
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
    }
}