package com.aqrlei.graduation.yueting.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.info.FileInfo
import com.aqrairsigns.aqrleilib.util.AppCache
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.CacheConstant
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.presenter.activitypresenter.FileActivityPresenter
import com.aqrlei.graduation.yueting.ui.adapter.FileListAdapter
import kotlinx.android.synthetic.main.file_activity_file.*

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/11 Time: 13:40
 */
class FileActivity : MvpContract.MvpActivity<FileActivityPresenter>(),
        AdapterView.OnItemClickListener,
        View.OnClickListener {
    companion object {
        fun jumpToFileActivity(context: Activity,
                               reqCode: Int,
                               type: String,
                               typeName: String,
                               which: Int) {
            val intent = Intent(context, FileActivity::class.java).apply {
                val bundle = Bundle().apply {
                    putString(YueTingConstant.FRAGMENT_TITLE_TYPE, type)
                    putString(YueTingConstant.FRAGMENT_TITLE_VALUE, typeName)
                    putInt(YueTingConstant.WHICH_JUMP_TO_FILE, which)
                }
                putExtras(bundle)
            }
            if (IntentUtil.queryActivities(context, intent)) {
                context.startActivityForResult(intent, reqCode)
            }
        }
    }

    private lateinit var fileInfoList: ArrayList<FileInfo>
    private lateinit var mData: ArrayList<FileInfo>
    private lateinit var mAdapter: FileListAdapter
    private lateinit var mProgressDialog: ProgressDialog

    override val mPresenter: FileActivityPresenter
        get() = FileActivityPresenter(this)
    override val layoutRes: Int
        get() = R.layout.file_activity_file

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backIv -> {
                this@FileActivity.finish()
            }
            R.id.tv_file_parent -> {
                getFileInfo(fileInfoList[0].parentPath)
            }
            R.id.addFileIv -> {
                setProgressDialog()
                addToDatabase()
            }
        }

    }

    override fun onItemClick(parent: AdapterView<*>?, convertView: View?,
                             position: Int, clickId: Long) {
        if (mData[position].isDir) {
            getFileInfo(mData[position].path)
        }
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        init()
        initListener()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish()
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        AppCache.APPCACHE.putString(CacheConstant.FILE_PATH_KEY, fileInfoList[0].path)
                .commit()
        fileInfoList.clear()
    }

    fun changeFileInfo(data: ArrayList<FileInfo>) {
        mData.clear()
        fileInfoList = data
        if (!fileInfoList.isEmpty() && fileInfoList.size > 1) {
            mData.addAll(fileInfoList.subList(1, fileInfoList.size))
        }
        tv_file_parent.text = fileInfoList[0].path
        mAdapter.notifyDataSetChanged()
    }

    fun finishActivity(result: Boolean, bookChange: Boolean, musicChange: Boolean) {
        mProgressDialog.dismiss()
        AppToast.toastShow(this@FileActivity, if (result) "添加完毕" else "添加失败", 1000)
        if (intent.extras.getInt(YueTingConstant.WHICH_JUMP_TO_FILE)
                == YueTingConstant.YUE_TING_FILE) {
            val intent = Intent()
                    .putExtra(YueTingConstant.FILE_BOOK_CHANGE, bookChange)
                    .putExtra(YueTingConstant.FILE_MUSIC_CHANGE, musicChange)
            setResult(YueTingConstant.YUE_TING_FILE_RES, intent)
        }
        this@FileActivity.finish()
    }

    private fun init() {
        mData = ArrayList()
        mAdapter = FileListAdapter(mData, this, R.layout.read_list_item)
        lv_file.adapter = mAdapter
        lv_file.onItemClickListener = this
        getFileInfo(AppCache.APPCACHE.getString(CacheConstant.FILE_PATH_KEY, CacheConstant.FILE_PATH_DEFAULT))
    }

    private fun initListener() {
        backIv.setOnClickListener(this)
        tv_file_parent.setOnClickListener(this)
        addFileIv.setOnClickListener(this)
    }

    private fun setProgressDialog() {
        mProgressDialog = ProgressDialog(this)
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setTitle("提示")
        mProgressDialog.setMessage("正在添加中...")
        mProgressDialog.show()
    }

    private fun getFileInfo(path: String) {
        val type = intent.extras.getString(YueTingConstant.FRAGMENT_TITLE_TYPE)
        mPresenter.getFileInfo(path, type)
    }

    private fun addToDatabase() {
        mPresenter.addToDataBase(mData, intent.extras.getString(YueTingConstant.FRAGMENT_TITLE_VALUE))
    }
}