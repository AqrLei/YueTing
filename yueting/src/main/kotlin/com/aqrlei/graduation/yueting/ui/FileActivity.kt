package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.info.Info.FileInfo
import com.aqrairsigns.aqrleilib.util.AppLog
import com.aqrairsigns.aqrleilib.util.FileUtil
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
        AdapterView.OnItemClickListener {
    override fun onItemClick(parent: AdapterView<*>?, convertView: View?,
                             position: Int, clickId: Long) {
        if (mData[position].isDir) {
            AppLog.logDebug("item", "path:\t ${mData[position].parentPath}")

            changeFileInfo(mData[position].path)
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
        fileInfoList = FileUtil.createFileInfoS("/storage")
        mData = ArrayList()
        mData.addAll(fileInfoList.subList(1, fileInfoList.size - 1))
        mAdapter = FileListAdapter(mData, this, R.layout.listitem_read)
        tv_file_parent.text = fileInfoList[0].path
        tv_file_parent.setOnClickListener {

            changeFileInfo(fileInfoList[0].parentPath)
        }
        lv_file.adapter = mAdapter
        lv_file.onItemClickListener = this
    }

    private fun changeFileInfo(path: String) {
        fileInfoList = FileUtil.createFileInfoS(path)
        mData.clear()
        mData.addAll(fileInfoList.subList(1, fileInfoList.size))
        tv_file_parent.text = fileInfoList[0].path
        mAdapter.notifyDataSetChanged()
    }

    companion object {
        fun jumpToFileActivity(context: Context, titleName: String = " ") {
            val intent = Intent(context, FileActivity::class.java)
            val bundle = Bundle()
            bundle.putString("path", titleName)
            if (IntentUtil.queryActivities(context, intent)) context.startActivity(intent)

        }
    }
}