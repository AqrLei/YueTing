package com.aqrlei.graduation.yueting.ui.fragment

import android.app.Dialog
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Messenger
import android.support.constraint.ConstraintLayout
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.ui.view.AlphaListView
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrairsigns.aqrleilib.util.DensityUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.aidl.MusicInfo
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.BookInfo
import com.aqrlei.graduation.yueting.model.local.infotool.ShareBookInfo
import com.aqrlei.graduation.yueting.model.local.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.presenter.fragmentpresenter.TabHomePresenter
import com.aqrlei.graduation.yueting.ui.FileActivity
import com.aqrlei.graduation.yueting.ui.PdfReadActivity
import com.aqrlei.graduation.yueting.ui.TxtReadActivity
import com.aqrlei.graduation.yueting.ui.YueTingActivity
import com.aqrlei.graduation.yueting.ui.adapter.YueTingListAdapter
import com.aqrlei.graduation.yueting.ui.uiEt.sendPlayBroadcast
import kotlinx.android.synthetic.main.main_fragment_home.*
import kotlinx.android.synthetic.main.main_include_lv_content.view.*
import kotlinx.android.synthetic.main.music_include_yue_ting_play.view.*
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
                putString(YueTingConstant.FRAGMENT_TITLE_TYPE,type)
                putString(YueTingConstant.FRAGMENT_TITLE_VALUE,name)
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
    private lateinit var mBookAdapter: YueTingListAdapter
    private lateinit var mMusicAdapter: YueTingListAdapter
    private val mListView: AlphaListView by lazy { mView.lv_fragment_home as AlphaListView }
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
        when (view?.id) {
            R.id.ll_read_item -> {
                showDialog(true)
            }
            R.id.ll_music_item -> {
                showDialog(false)
            }
        }
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.titleNameTv -> {
                val flag = titleNameTv.compoundDrawables[2].level == YueTingConstant.TITLE_TYPE_MUSIC
                if (flag) {
                    titleNameTv.compoundDrawables[2].level = YueTingConstant.TITLE_TYPE_BOOK
                } else {
                    titleNameTv.compoundDrawables[2].level = YueTingConstant.TITLE_TYPE_MUSIC
                }
                mListView.adapter = if (!flag) mMusicAdapter else mBookAdapter
                titleNameTv.text = if (!flag) "欣听" else "悦读"
                headerTopCl.background.level = titleNameTv.compoundDrawables[2].level
                mContainerActivity.getMPlayView().expandListIv.visibility = if (!flag) {
                    val playV = mContainerActivity.getMPlayView().playListLv
                    if (playV.visibility == View.VISIBLE) playV.visibility = View.GONE
                    View.GONE
                } else View.VISIBLE
            }
            R.id.addFileIv -> {
                FileActivity.jumpToFileActivity(mContainerActivity, YueTingConstant.YUE_TING_FILE_REQ)
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
                    removeInfo(false)
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
                    removeInfo(true)
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
        changeBookAdapter()
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

    fun changeMusicAdapter() {
        mMusicAdapter.notifyDataSetInvalidated()
    }

    fun changeBookAdapter() {
        mBookAdapter.notifyDataSetInvalidated()
    }

    private fun initData() {
        val type = arguments.getString(YueTingConstant.FRAGMENT_TITLE_TYPE)
        val name = arguments.getString(YueTingConstant.FRAGMENT_TITLE_VALUE)
        if(type == YueTingConstant.FRAGMENT_TITLE_TYPE_MUSIC) {
            getMusicInfoFromDB(name)
        } else {
            getBookInfoFromDB(name)
        }
    }

    private fun initView() {
        mBookAdapter = YueTingListAdapter(
                mBookInfoShared.getInfoS(), mContainerActivity,
                R.layout.read_list_item, YueTingConstant.ADAPTER_TYPE_BOOK)
        mMusicAdapter = YueTingListAdapter(
                mMusicInfoShared.getInfoS(), mContainerActivity,
                R.layout.music_list_item, YueTingConstant.ADAPTER_TYPE_MUSIC)
        mContainerActivity.initPlayListView(mMusicAdapter)
        mListView.adapter = mBookAdapter
        titleNameTv.text = "悦读"
    }

    private fun initListener() {
        mListView.onItemClickListener = this
        mListView.onItemLongClickListener = this
        addFileIv.setOnClickListener(this)
        titleNameTv.setOnClickListener(this)
    }

    private fun getMusicInfoFromDB(name:String) {
        mPresenter.getMusicInfoFromDB(name)
    }

    private fun getBookInfoFromDB(name:String) {
        mPresenter.getBookInfoFromDB(name)
    }

    private fun removeInfo(flag: Boolean) {
        if (flag) {
            val path = mBookInfoShared.getInfo(removePosition).path
            mBookInfoShared.removeInfo(removePosition)
            mBookAdapter.notifyDataSetInvalidated()
            mPresenter.deleteBookItemFromDB(path)
        } else {
            val path = mMusicInfoShared.getInfo(removePosition).albumUrl
            mMusicInfoShared.removeInfo(removePosition)
            mMusicAdapter.notifyDataSetInvalidated()
            mPresenter.deleteMusicItemFromDB(path)
        }
    }

    private fun showDialog(isBook: Boolean) {
        val dialog = Dialog(mContainerActivity, R.style.BottomDialog)
        dialog.setContentView(R.layout.manage_dialog_bottom)
        dialog.window.decorView.findViewById(R.id.tv_remove_items).setOnClickListener({
            removeInfo(isBook)
            dialog.dismiss()
        })
        dialog.window.setGravity(Gravity.BOTTOM)
        dialog.window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT,
                DensityUtil.dipToPx(mContainerActivity, 50f))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
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