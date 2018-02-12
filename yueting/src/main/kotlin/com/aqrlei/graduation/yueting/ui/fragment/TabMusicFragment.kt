package com.aqrlei.graduation.yueting.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.aqrairsigns.aqrleilib.adapter.CommonExpandListAdapter
import com.aqrlei.graduation.yueting.R
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrairsigns.aqrleilib.ui.view.AlphaExpandListView
import com.aqrlei.graduation.yueting.model.local.ExpandMusicMessage
import com.aqrlei.graduation.yueting.model.local.MusicMessage
import com.aqrlei.graduation.yueting.presenter.fragmentpresenter.TabMusicPresenter
import com.aqrlei.graduation.yueting.ui.YueTingActivity
import com.aqrlei.graduation.yueting.ui.adapter.YueTingMusicExpandListAdapter
import kotlinx.android.synthetic.main.layout_yueting_header.*
import kotlinx.android.synthetic.main.yueting_fragment_music.view.*

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/24
 */
class TabMusicFragment : MvpContract.MvpFragment<TabMusicPresenter, YueTingActivity>(),
        CommonExpandListAdapter.OnExpandListInternalClick,
        AlphaExpandListView.OnAlphaChangeListener {


    override val layoutRes: Int
        get() = R.layout.yueting_fragment_music
    override val mPresenter: TabMusicPresenter
        get() = TabMusicPresenter(this)

    private var mChildMusic = ArrayList<MusicMessage>()
    private var mGroupMusic = ArrayList<ExpandMusicMessage>()

    companion object {
        fun newInstance(): TabMusicFragment {
            val args = Bundle()
            val fragment = TabMusicFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initComponents() {
        super.initComponents()
        initView()

    }

    private fun initView() {
        val mMusicElv = mView.elv_fragment_music as AlphaExpandListView
        mChildMusic.add(MusicMessage("", "这是歌单吧", "2017-08-31", 100))
        mChildMusic.add(MusicMessage("", "这是歌单吧", "2017-08-31", 200))
        mChildMusic.add(MusicMessage("", "这是歌单吧", "2017-08-31", 300))
        mChildMusic.add(MusicMessage("", "这是歌单吧", "2017-08-31", 400))
        mGroupMusic.add(ExpandMusicMessage("我的歌单", mChildMusic))
        mGroupMusic.add(ExpandMusicMessage("最近播放"))
        mMusicElv.addHeaderView(LayoutInflater.from(mContainerActivity).
                inflate(R.layout.listheader_music, null))
        mMusicElv.setAdapter(YueTingMusicExpandListAdapter(
                mContainerActivity, mGroupMusic, R.layout.listitem_child_music,
                R.layout.listitem_group_music, this
        )
        )

    }

    override fun onExpandListInternalClick(v: View) {
        when (v.id) {
            R.id.iv_music_group_setting -> {
                AppToast.toastShow(mContainerActivity, "Group", 1000)
            }
            R.id.tv_music_manager -> {
                AppToast.toastShow(mContainerActivity, "Child", 1000)
            }
        }
    }

    override fun onAlphaChanged(percent: Float) {
        mContainerActivity.rg_anim_tab.setBackgroundColor(
                Color.argb((175 * percent).toInt(), 113, 204, 180)
        )
    }
}