package com.aqrlei.graduation.yueting.ui.fragment

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Messenger
import android.view.View
import android.widget.AdapterView
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.ui.view.AlphaListView
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.aidl.MusicInfo
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.BookInfo
import com.aqrlei.graduation.yueting.model.local.infotool.ShareBookInfo
import com.aqrlei.graduation.yueting.model.local.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.presenter.fragmentpresenter.TabHomePresenter
import com.aqrlei.graduation.yueting.ui.PdfReadActivity
import com.aqrlei.graduation.yueting.ui.TxtReadActivity
import com.aqrlei.graduation.yueting.ui.YueTingActivity
import com.aqrlei.graduation.yueting.ui.adapter.YueTingListAdapter
import kotlinx.android.synthetic.main.home_top_layout.*
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
        AdapterView.OnItemClickListener,
        View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_right_listen -> {
                tv_right_listen.visibility = View.INVISIBLE
                mListView.adapter = mMusicAdapter
                tv_left_read.visibility = View.VISIBLE
                tv_title_name.text = "欣听"
            }
            R.id.tv_left_read -> {
                tv_left_read.visibility = View.INVISIBLE
                mListView.adapter = mBookAdapter
                tv_right_listen.visibility = View.VISIBLE
                tv_title_name.text = "悦读"
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, convertView: View, position: Int, id: Long) {
        when (convertView.id) {
            R.id.ll_music_item -> {
                isServiceStart = mMusicInfoShared.isStartService()
                if (!isServiceStart) {
                    startMusicService(position)
                    isServiceStart = true
                } else {
                    sendPlayBroadcast(position)
                }
            }
            R.id.ll_read_item -> {
                if (mBookInfoShared.getInfo(position).type == "txt") {
                    TxtReadActivity.jumpToTxtReadActivity(mContainerActivity,
                            mBookInfoShared.getInfo(position))
                }
                if (mBookInfoShared.getInfo(position).type == "pdf") {
                    PdfReadActivity.jumpToPdfReadActivity(mContainerActivity,
                            mBookInfoShared.getInfo(position))
                }
            }
        }

    }

    private var isServiceStart = false
    private var mMusicInfoShared = ShareMusicInfo.MusicInfoTool
    private var mBookInfoShared = ShareBookInfo.BookInfoTool
    private lateinit var mBookAdapter: YueTingListAdapter
    private lateinit var mMusicAdapter: YueTingListAdapter
    private val mListView: AlphaListView
        get() = mView.lv_fragment_home as AlphaListView

    private val serviceConn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            sendMusicInfoS(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {

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

    override fun initComponents(view: View?, savedInstanceState: Bundle?) {
        super.initComponents(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        getMusicInfoFromDB()
        getBookInfoFromDB()

    }

    private fun initView() {

        mBookAdapter = YueTingListAdapter(mBookInfoShared.getInfoS(), mContainerActivity, R.layout.listitem_read, 0)
        mMusicAdapter = YueTingListAdapter(mMusicInfoShared.getInfoS(), mContainerActivity, R.layout.listitem_music, 1)

        mListView.adapter = mBookAdapter
        mListView.onItemClickListener = this
        tv_left_read.visibility = View.INVISIBLE
        tv_title_name.text = "悦读"
        tv_left_read.setOnClickListener(this)
        tv_right_listen.setOnClickListener(this)

    }

    private fun getMusicInfoFromDB() {
        mPresenter.getMusicInfoFromDB()
    }

    private fun getBookInfoFromDB() {
        mPresenter.getBookInfoFromDB()

    }

    private fun startMusicService(position: Int) {
        mPresenter.startMusicService(mContainerActivity, position, Messenger(mMusicInfoShared.getHandler(mContainerActivity)), serviceConn)
    }

    private fun sendPlayBroadcast(position: Int) {
        val ACTION_PLAY = YueTingConstant.ACTION_BROADCAST[YueTingConstant.ACTION_PLAY]
        val playIntent = Intent(ACTION_PLAY)
        playIntent.putExtra("position", position)
        mContainerActivity.sendOrderedBroadcast(playIntent, null)
    }

    private fun sendMusicInfoS(binder: IBinder?) {
        if (binder != null) {
            mPresenter.sendMusicInfo(binder)
        }
    }

    fun unbindMusicService() {
        mContainerActivity.unbindService(serviceConn)
    }

    fun setMusicInfo(data: ArrayList<MusicInfo>) {
        mMusicInfoShared.setInfoS(data)
        mMusicAdapter.notifyDataSetChanged()
    }

    fun setBookInfo(data: ArrayList<BookInfo>) {
        mBookInfoShared.setInfoS(data)
        mBookAdapter.notifyDataSetChanged()

    }


}