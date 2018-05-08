package com.aqrlei.graduation.yueting.ui

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.adapter.CommonListAdapter
import com.aqrlei.graduation.yueting.basemvp.MvpContract
import com.aqrlei.graduation.yueting.constant.CacheConstant
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.FileInfo
import com.aqrlei.graduation.yueting.model.FileSelectInfo
import com.aqrlei.graduation.yueting.presenter.FilePresenter
import com.aqrlei.graduation.yueting.ui.adapter.FileListAdapter
import com.aqrlei.graduation.yueting.util.AppCache
import com.aqrlei.graduation.yueting.util.AppToast
import com.aqrlei.graduation.yueting.util.IntentUtil
import com.aqrlei.graduation.yueting.util.createPopView
import kotlinx.android.synthetic.main.file_activity_file.*

/**
 * @Author: AqrLei
 * @CreateTime: Date: 2017/9/11 Time: 13:40
 */
class FileActivity : MvpContract.MvpActivity<FilePresenter>(),
        AdapterView.OnItemClickListener,
        View.OnClickListener,
        CommonListAdapter.OnInternalClick,
        FileListAdapter.DirCallBack {
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

    private val fileInfoList: ArrayList<FileInfo>
            by lazy {
                ArrayList<FileInfo>()
            }
    private val mData: ArrayList<FileSelectInfo>
            by lazy {
                ArrayList<FileSelectInfo>()
            }
    private val type: String
            by lazy {
                intent.extras.getString(YueTingConstant.FRAGMENT_TITLE_TYPE)
            }
    private val mAdapter: FileListAdapter
            by lazy {
                FileListAdapter(
                        mData = mData,
                        mContext = this,
                        listener = this).apply {
                    setCallBack(this@FileActivity)
                }
            }
    private val progressDialog: Dialog
            by lazy {
                createPopView(this, R.layout.common_progress_bar, Gravity.CENTER).apply {
                    setCancelable(false)
                }
            }

    override val mPresenter: FilePresenter
        get() = FilePresenter(this)
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
                addFileIv.background.level = if (addFileIv.background.level == 0) {
                    selectAll(true)
                    1
                } else {
                    selectAll(false)
                    0
                }
            }
            R.id.sureTv -> {
                progressDialog.show()
                addToDatabase()
            }
        }

    }

    override fun onItemClick(parent: AdapterView<*>?, convertView: View?,
                             position: Int, clickId: Long) {
        Log.d("file", "$position")
        if (mData[position].fileInfo.isDir) {
            getFileInfo(mData[position].fileInfo.path)
        }
    }

    override fun onInternalClick(v: View, position: Int) {
        Log.d("file", "$position")
        val iv = v as ImageView
        iv.background.level = if (iv.background.level == 1) {
            if (addFileIv.background.level == 1) {
                addFileIv.background.level = 0
            }
            mData[position].status = FileSelectInfo.UNSELECTED
            0
        } else {
            mData[position].status = FileSelectInfo.SELECTED
            1
        }
    }

    override fun dirCallBack() {
        addFileIv.visibility = View.VISIBLE
        sureTv.visibility = View.VISIBLE
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
        fileInfoList.clear()
        fileInfoList.addAll(data)
        if (!fileInfoList.isEmpty() && fileInfoList.size > 1) {
            fileInfoList.subList(1, fileInfoList.size).forEach {
                mData.add(FileSelectInfo(fileInfo = it))
            }
        }
        tv_file_parent.text = fileInfoList[0].path
        addFileIv.visibility = View.GONE
        sureTv.visibility = View.GONE
        mAdapter.notifyDataSetChanged()
    }

    fun finishActivity(result: Boolean, bookChange: Boolean, musicChange: Boolean) {
        progressDialog.dismiss()
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

    private fun selectAll(isSelect: Boolean) {
        mData.forEach {
            if (isSelect) {
                it.status = FileSelectInfo.SELECTED
            } else {
                it.status = FileSelectInfo.UNSELECTED
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    private fun init() {
        lv_file.adapter = mAdapter
        lv_file.onItemClickListener = this
        getFileInfo(AppCache.APPCACHE.getString(CacheConstant.FILE_PATH_KEY, CacheConstant.FILE_PATH_DEFAULT))
    }

    private fun initListener() {
        backIv.setOnClickListener(this)
        tv_file_parent.setOnClickListener(this)
        addFileIv.setOnClickListener(this)
        sureTv.setOnClickListener(this)
    }

    private fun getFileInfo(path: String) {
        mPresenter.getFileInfo(path, type)
    }

    private fun addToDatabase() {
        mPresenter.addFileInfoToDB(type, mData, intent.extras.getString(YueTingConstant.FRAGMENT_TITLE_VALUE))
    }
}