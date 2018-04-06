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
        fun jumpToFileActivity(context: Activity, reqCode: Int) {
            val intent = Intent(context, FileActivity::class.java)
            if (IntentUtil.queryActivities(context, intent)) {
                context.startActivityForResult(intent, reqCode)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                this@FileActivity.finish()
            }
            R.id.tv_file_parent -> {
                getFileInfo(fileInfoList[0].parentPath)
            }
            R.id.tv_add_file -> {
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

    private lateinit var fileInfoList: ArrayList<FileInfo>
    private lateinit var mData: ArrayList<FileInfo>
    private lateinit var mAdapter: FileListAdapter
    private lateinit var mProgressDialog: ProgressDialog

    override val mPresenter: FileActivityPresenter
        get() = FileActivityPresenter(this)
    override val layoutRes: Int
        get() = R.layout.file_activity_file

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        init()

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish()
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        AppCache.APPCACHE.putString("path", fileInfoList[0].path)
                .commit()
        fileInfoList.clear()
    }

    private fun init() {
        mData = ArrayList()
        mAdapter = FileListAdapter(mData, this, R.layout.read_module_list_item)
        lv_file.adapter = mAdapter
        lv_file.onItemClickListener = this
        getFileInfo(AppCache.APPCACHE.getString("path", "/storage/emulated/0"))
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
        mPresenter.getFileInfo(path)
    }

    private fun addToDatabase() {
        mPresenter.addToDataBase(mData)
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
        val intent = Intent().putExtra("bookChange", bookChange).putExtra("musicChange", musicChange)
        setResult(YueTingConstant.FILERECODE, intent)
        this@FileActivity.finish()
    }
}