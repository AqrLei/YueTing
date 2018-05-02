package com.aqrlei.graduation.yueting.ui.fragment

import android.app.Dialog
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Messenger
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.ui.view.AlphaListView
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrairsigns.aqrleilib.util.DateFormatUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.aidl.MusicInfo
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.BookInfo
import com.aqrlei.graduation.yueting.model.infotool.ShareBookInfo
import com.aqrlei.graduation.yueting.model.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.presenter.TabHomePresenter
import com.aqrlei.graduation.yueting.ui.*
import com.aqrlei.graduation.yueting.ui.adapter.YueTingListAdapter
import com.aqrlei.graduation.yueting.ui.uiEt.createPopView
import com.aqrlei.graduation.yueting.ui.uiEt.sendPlayBroadcast
import kotlinx.android.synthetic.main.main_fragment_home.*
import kotlinx.android.synthetic.main.main_include_lv_content.view.*
import java.io.File

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/23
 */
class TabHomeFragment : MvpContract.MvpFragment<TabHomePresenter, YueTingActivity>(),
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        View.OnClickListener {

    companion object {
        fun newInstance(type: String, name: String): TabHomeFragment {
            val args = Bundle().apply {
                putString(YueTingConstant.FRAGMENT_TITLE_TYPE, type)
                putString(YueTingConstant.FRAGMENT_TITLE_VALUE, name)
            }
            val fragment = TabHomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var removePosition: Int = 0
    private var isServiceStart = false
    private var mMusicInfoShared = ShareMusicInfo.MusicInfoTool
    private var mBookInfoShared = ShareBookInfo.BookInfoTool
    private val dialog: Dialog
            by lazy {
                createPopView(mContainerActivity, R.layout.manage_pop_view_item).apply {
                    window.decorView?.apply {
                        findViewById(R.id.tv_remove_items).setOnClickListener(this@TabHomeFragment)
                        findViewById(R.id.lookDetailsTv).setOnClickListener(this@TabHomeFragment)
                        findViewById(R.id.batchDeleteTv).setOnClickListener(this@TabHomeFragment)
                        findViewById(R.id.moveItemsTv).setOnClickListener(this@TabHomeFragment)
                    }
                }
            }
    private val detailDialog: Dialog
            by lazy {
                createPopView(mContainerActivity, R.layout.common_item_detail).apply {
                    window.decorView?.apply {
                        (findViewById(R.id.itemNameTv) as TextView).text =
                                if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK)
                                    mBookInfoShared.getInfo(removePosition).name
                                else mMusicInfoShared.getInfo(removePosition).title
                        (findViewById(R.id.ownerTv) as TextView).text =
                                if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK)
                                    ""
                                else mMusicInfoShared.getInfo(removePosition).artist
                        (findViewById(R.id.localPathTv) as TextView).text =
                                if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK)
                                    mBookInfoShared.getInfo(removePosition).path
                                else mMusicInfoShared.getInfo(removePosition).albumUrl
                        (findViewById(R.id.itemNameTv) as TextView).text =
                                if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK) {
                                    "${mBookInfoShared.getInfo(removePosition).fileLength / (1024.0F * 1024.0F)} M"
                                } else {
                                    DateFormatUtil.simpleTimeFormat(
                                            mMusicInfoShared.getInfo(removePosition).duration.toLong())

                                }
                    }
                }
            }
    private val mListView: AlphaListView
            by lazy {
                mView.lv_fragment_home as AlphaListView
            }
    private val mAdapter: YueTingListAdapter
            by lazy {
                if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK) {
                    YueTingListAdapter(
                            mBookInfoShared.getInfoS(), mContainerActivity,
                            R.layout.read_list_item, YueTingConstant.ADAPTER_TYPE_BOOK)
                } else {
                    YueTingListAdapter(
                            mMusicInfoShared.getInfoS(), mContainerActivity,
                            R.layout.music_list_item, YueTingConstant.ADAPTER_TYPE_MUSIC)
                }
            }
    private val type: String
            by lazy {
                arguments.getString(YueTingConstant.FRAGMENT_TITLE_TYPE)
            }
    private val name: String
            by lazy {
                arguments.getString(YueTingConstant.FRAGMENT_TITLE_VALUE)
            }

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
        get() = R.layout.main_fragment_home

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        removePosition = position
        dialog.show()
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.addFileIv -> {
                FileActivity.jumpToFileActivity(
                        mContainerActivity,
                        YueTingConstant.YUE_TING_FILE_REQ,
                        type,
                        name,
                        YueTingConstant.YUE_TING_FILE)
            }
            R.id.backIv -> {
                mContainerActivity.finish()
            }
            R.id.tv_remove_items -> {
                removeInfo()
                dialog.dismiss()
            }
            R.id.batchDeleteTv -> {
                ManageListActivity.jumpToManageListActivity(
                        mContainerActivity,
                        YueTingConstant.MANAGE_TYPE_ITEM,
                        mPresenter.generateListString(type),
                        YueTingConstant.MANAGE_REQ)
            }
            R.id.lookDetailsTv -> {
                dialog.dismiss()
                detailDialog.show()
            }
            R.id.moveItemsTv -> {
                //TODO move items to other name List
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, convertView: View, position: Int, id: Long) {
        when (convertView.id) {
            R.id.ll_music_item -> {
                val file = File(mMusicInfoShared.getInfo(position).albumUrl)
                if (file.exists()) {
                    isServiceStart = mMusicInfoShared.isStartService()
                    if (!isServiceStart) {
                        startMusicService(position)
                        isServiceStart = true
                    } else {
                        sendPlayBroadcast(position, mContainerActivity)
                    }
                } else {
                    AppToast.toastShow(mContainerActivity, "文件不存在", 1000)
                    removeInfo()
                }
            }
            R.id.ll_read_item -> {
                val file = File(mBookInfoShared.getInfo(position).path)
                if (file.exists()) {
                    if (mBookInfoShared.getInfo(position).type == "txt") {
                        TxtReadActivity.jumpToTxtReadActivity(mContainerActivity,
                                mBookInfoShared.getInfo(position))
                    }
                    if (mBookInfoShared.getInfo(position).type == "pdf") {
                        PdfReadActivity.jumpToPdfReadActivity(mContainerActivity,
                                mBookInfoShared.getInfo(position))
                    }
                } else {
                    AppToast.toastShow(mContainerActivity, "文件不存在", 1000)
                    removeInfo()
                }
            }
        }
    }

    override fun initComponents(view: View?, savedInstanceState: Bundle?) {
        super.initComponents(view, savedInstanceState)
        initData()
        initListener()
        initView()

    }

    override fun onResume() {
        super.onResume()
        if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK) {
            changeBookAdapter()
        }
    }

    fun setMusicTitle(musicName: String) {

        titleNameTv.text = musicName
        if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK) {
            titleNameTv.text = "悦读"
        }
    }

    fun unbindMusicService() {
        mContainerActivity.unbindService(serviceConn)
    }

    fun setMusicInfo(data: ArrayList<MusicInfo>) {
        mMusicInfoShared.setInfoS(data)
        mAdapter.notifyDataSetChanged()
    }

    fun setBookInfo(data: ArrayList<BookInfo>) {
        mBookInfoShared.setInfoS(data)
        mAdapter.notifyDataSetChanged()

    }

    fun changeMusicAdapter() {
        mAdapter.notifyDataSetInvalidated()
    }

    fun changeBookAdapter() {
        mAdapter.notifyDataSetInvalidated()
    }

    private fun initData() {
        if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_MUSIC) {
            getMusicInfoFromDB(name)
        } else {
            getBookInfoFromDB(name)
        }
    }

    private fun initView() {
        if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_MUSIC) {
            mContainerActivity.initPlayListView(mAdapter)
            titleNameTv.compoundDrawables[2].level = YueTingConstant.TITLE_TYPE_MUSIC
        } else {
            mContainerActivity.initPlayListView(YueTingListAdapter(
                    mMusicInfoShared.getInfoS(), mContainerActivity,
                    R.layout.music_list_item, YueTingConstant.ADAPTER_TYPE_MUSIC))
        }
        mListView.adapter = mAdapter
        titleNameTv.text = name
    }

    private fun initListener() {
        mListView.onItemClickListener = this
        mListView.onItemLongClickListener = this
        addFileIv.setOnClickListener(this)
        backIv.setOnClickListener(this)
    }

    private fun getMusicInfoFromDB(name: String) {
        mPresenter.getMusicInfoFromDB(name)
    }

    private fun getBookInfoFromDB(name: String) {
        mPresenter.getBookInfoFromDB(name)
    }

    private fun removeInfo() {
        if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK) {
            val path = mBookInfoShared.getInfo(removePosition).path
            mBookInfoShared.removeInfo(removePosition)
            mAdapter.notifyDataSetInvalidated()
            mPresenter.deleteBookItemFromDB(path)
        } else {
            val path = mMusicInfoShared.getInfo(removePosition).albumUrl
            mMusicInfoShared.removeInfo(removePosition)
            mAdapter.notifyDataSetInvalidated()
            mPresenter.deleteMusicItemFromDB(path)
        }
    }


    private fun startMusicService(position: Int) {
        mPresenter.startMusicService(mContainerActivity, position, Messenger(mMusicInfoShared.getHandler(mContainerActivity)), serviceConn)
    }

    private fun sendMusicInfoS(binder: IBinder?) {
        if (binder != null) {
            mPresenter.sendMusicInfo(binder)
        }
    }
}