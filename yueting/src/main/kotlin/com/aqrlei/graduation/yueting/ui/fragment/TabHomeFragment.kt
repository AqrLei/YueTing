package com.aqrlei.graduation.yueting.ui.fragment

import android.app.Dialog
import android.app.ProgressDialog
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
import com.aqrlei.graduation.yueting.ui.adapter.PopViewListAdapter
import com.aqrlei.graduation.yueting.ui.adapter.YueTingListAdapter
import com.aqrlei.graduation.yueting.ui.uiEt.createListPopView
import com.aqrlei.graduation.yueting.ui.uiEt.createPopView
import com.aqrlei.graduation.yueting.ui.uiEt.createProgressDialog
import com.aqrlei.graduation.yueting.ui.uiEt.sendPlayBroadcast
import kotlinx.android.synthetic.main.main_fragment_home.*
import kotlinx.android.synthetic.main.main_include_lv_content.view.*
import java.io.File

/**
 * @Author: AqrLei
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

    private var modifyPosition: Int = 0
    private var isServiceStart = false
    private var mMusicInfoShared = ShareMusicInfo.MusicInfoTool
    private var mBookInfoShared = ShareBookInfo.BookInfoTool
    private val manageDialog: Dialog
            by lazy {
                createPopView(mContainerActivity, R.layout.manage_pop_view_item).apply {
                    window.decorView?.apply {
                        findViewById(R.id.lookDetailsTv).setOnClickListener(this@TabHomeFragment)
                        findViewById(R.id.manageItemTv).apply {
                            setOnClickListener(this@TabHomeFragment)
                            (this as TextView).text =
                                    if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK) "管理书籍"
                                    else "管理歌曲"
                        }
                        findViewById(R.id.moveItemsTv).setOnClickListener(this@TabHomeFragment)
                    }
                }
            }
    private val detailDialog: Dialog
            by lazy {
                createPopView(mContainerActivity, R.layout.common_item_detail).apply {
                    window.decorView?.apply {
                        (findViewById(R.id.itemNameTitleTv) as TextView).text =
                                if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK) "书名:"
                                else "歌名:"
                        (findViewById(R.id.itemNameTv) as TextView).text =
                                if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK)
                                    mBookInfoShared.getInfo(modifyPosition).name
                                else mMusicInfoShared.getInfo(modifyPosition).title
                        (findViewById(R.id.ownerTitleTv) as TextView).text =
                                if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK) "作者:"
                                else "歌手:"
                        (findViewById(R.id.ownerTv) as TextView).text =
                                if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK) ""
                                else mMusicInfoShared.getInfo(modifyPosition).artist
                        (findViewById(R.id.localPathTv) as TextView).text =
                                if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK)
                                    mBookInfoShared.getInfo(modifyPosition).path
                                else mMusicInfoShared.getInfo(modifyPosition).albumUrl
                        (findViewById(R.id.sizeTitleTv) as TextView).text =
                                if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK) "大小:"
                                else "时长:"
                        (findViewById(R.id.sizeTv) as TextView).text =
                                if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK) {
                                    "${mBookInfoShared.getInfo(modifyPosition).fileLength / (1024.0F * 1024.0F)} M"
                                } else {
                                    DateFormatUtil.simpleTimeFormat(
                                            mMusicInfoShared.getInfo(modifyPosition).duration.toLong())
                                }
                    }
                }
            }
    private val moveItemDialog: Dialog
            by lazy {
                createListPopView(
                        context = mContainerActivity,
                        adapter = popViewListAdapter,
                        listener = this
                )
            }
    private val typeList: ArrayList<String>
            by lazy { ArrayList<String>() }
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
    private val popViewListAdapter: PopViewListAdapter
            by lazy {
                PopViewListAdapter(typeList, mContainerActivity)
            }
    private val type: String
            by lazy {
                arguments.getString(YueTingConstant.FRAGMENT_TITLE_TYPE)
            }
    private val typeName: String
            by lazy {
                arguments.getString(YueTingConstant.FRAGMENT_TITLE_VALUE)
            }
    private val progressDialog: ProgressDialog
            by lazy {
                createProgressDialog(mContainerActivity, "提示", "正在加载中...")
            }
    private var musicInfoChanged: Boolean = false

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
        modifyPosition = position
        manageDialog.show()
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.addFileIv -> {
                FileActivity.jumpToFileActivity(
                        mContainerActivity,
                        YueTingConstant.YUE_TING_FILE_REQ,
                        type,
                        typeName,
                        YueTingConstant.YUE_TING_FILE)
            }
            R.id.backIv -> {
                mContainerActivity.finish()
            }
            R.id.manageItemTv -> {
                ManageListActivity.jumpToManageListActivity(
                        mContainerActivity,
                        YueTingConstant.MANAGE_TYPE_ITEM,
                        type,
                        mPresenter.generateListSelectInfo(type),
                        YueTingConstant.MANAGE_REQ)
            }
            R.id.lookDetailsTv -> {
                manageDialog.dismiss()
                detailDialog.show()
            }
            R.id.moveItemsTv -> {
                manageDialog.dismiss()
                getTypeInfo()
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, convertView: View, position: Int, id: Long) {
        when (convertView.id) {
            R.id.ll_music_item -> {
                val file = File(mMusicInfoShared.getInfo(position).albumUrl)
                when {
                    file.exists() -> {
                        isServiceStart = mMusicInfoShared.isStartService()
                        when {
                            !isServiceStart -> {
                                startMusicService()
                                isServiceStart = true
                                if (musicInfoChanged) {
                                    bindMusicService(position)
                                }
                            }
                            musicInfoChanged -> {
                                bindMusicService(position)
                            }
                            else -> {
                                sendPlayBroadcast(position, mContainerActivity)
                            }
                        }
                    }
                    else -> {
                        AppToast.toastShow(mContainerActivity, "文件不存在", 1000)
                        removeInfo()
                    }
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
            R.id.popViewCl -> {
                if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK) {
                    updateBookInfo(typeList[position])
                } else {
                    updateMusicInfo(typeList[position])
                }
            }
        }
    }

    override fun initComponents(view: View?, savedInstanceState: Bundle?) {
        super.initComponents(view, savedInstanceState)
        initListener()
        initView()

    }

    override fun onResume() {
        super.onResume()
        initData()
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
        progressDialog.dismiss()
        mMusicInfoShared.setInfoS(data)
        mAdapter.notifyDataSetInvalidated()
        musicInfoChanged = true
    }

    fun setBookInfo(data: ArrayList<BookInfo>) {
        progressDialog.dismiss()
        mBookInfoShared.setInfoS(data)
        mAdapter.notifyDataSetInvalidated()

    }

    fun setTypeInfo(data: ArrayList<String>) {
        typeList.clear()
        typeList.addAll(data)
        popViewListAdapter.notifyDataSetInvalidated()
        moveItemDialog.show()
    }

    fun updateBookFinish(success: Boolean) {
        if (success) getBookInfo()
    }

    fun updateMusicFinish(success: Boolean) {
        if (success) getMusicInfo()
    }

    private fun initData() {
        if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_MUSIC) {
            getMusicInfo()
        } else {
            getBookInfo()
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
        titleNameTv.text = typeName
    }

    private fun initListener() {
        mListView.onItemClickListener = this
        mListView.onItemLongClickListener = this
        addFileIv.setOnClickListener(this)
        backIv.setOnClickListener(this)
    }

    private fun getMusicInfo() {
        progressDialog.show()
        mPresenter.fetchMusicInfo(typeName)
    }

    private fun updateMusicInfo(updateTypeName: String) {
        mPresenter.updateMusicTypeName(
                mMusicInfoShared.getInfo(modifyPosition).albumUrl,
                updateTypeName)
    }

    private fun updateBookInfo(updateTypeName: String) {
        mPresenter.updateBookTypeName(
                mBookInfoShared.getInfo(modifyPosition).path,
                updateTypeName)
    }

    private fun getBookInfo() {
        progressDialog.show()
        mPresenter.fetchBookInfo(typeName)
    }

    private fun getTypeInfo() {
        mPresenter.fetchTypeInfo(type)
    }

    private fun removeInfo() {
        if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK) {
            val path = mBookInfoShared.getInfo(modifyPosition).path
            mBookInfoShared.removeInfo(modifyPosition)
            mAdapter.notifyDataSetInvalidated()
            mPresenter.deleteBookItem(path)
        } else {
            val path = mMusicInfoShared.getInfo(modifyPosition).albumUrl
            mMusicInfoShared.removeInfo(modifyPosition)
            mAdapter.notifyDataSetInvalidated()
            mPresenter.deleteMusicItem(path)
        }
    }

    private fun startMusicService() {
        mPresenter.startMusicService(
                mContainerActivity,
                Messenger(mMusicInfoShared.getHandler(mContainerActivity)),
                serviceConn)
    }

    private fun bindMusicService(position: Int) {
        mPresenter.bindService(mContainerActivity, position, serviceConn)
    }

    private fun sendMusicInfoS(binder: IBinder?) {
        if (binder != null) {
            mPresenter.sendMusicInfo(binder)
        }
    }
}