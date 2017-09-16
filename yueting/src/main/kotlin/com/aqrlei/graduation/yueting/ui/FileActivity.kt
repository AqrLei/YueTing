package com.aqrlei.graduation.yueting.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.info.FileInfo
import com.aqrairsigns.aqrleilib.util.AppCache
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.presenter.activitypresenter.FileActivityPresenter
import com.aqrlei.graduation.yueting.ui.adapter.FileListAdapter
import kotlinx.android.synthetic.main.activity_file.*

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/11 Time: 13:40
 */
class FileActivity : MvpContract.MvpActivity<FileActivityPresenter>(),
        AdapterView.OnItemClickListener,
        View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                this@FileActivity.finish()
            }
            R.id.tv_file_parent -> {
                getFileInfo(fileInfoList[0].parentPath)
            }
            R.id.tv_add_file -> {
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

    override val mPresenter: FileActivityPresenter
        get() = FileActivityPresenter(this)
    override val layoutRes: Int
        get() = R.layout.activity_file

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        init()

    }

    private fun init() {
        mData = ArrayList()
        mAdapter = FileListAdapter(mData, this, R.layout.listitem_read)
        lv_file.adapter = mAdapter
        lv_file.onItemClickListener = this
        getFileInfo(AppCache.APPCACHE.getString("path", "/storage"))
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

    override fun onDestroy() {
        super.onDestroy()
        AppCache.APPCACHE.putString("path", fileInfoList[0].path)
                .commit()
        fileInfoList.clear()
    }

    fun finishActivity() {
        AppToast.toastShow(this@FileActivity, "添加完毕", 1000)
        this@FileActivity.finish()
    }

    companion object {
        fun jumpToFileActivity(context: Activity) {
            val intent = Intent(context, FileActivity::class.java)
            val bundle = Bundle()
            intent.putExtras(bundle)
            if (IntentUtil.queryActivities(context, intent)) {
                context.startActivity(intent)
            }

        }
    }
}