package com.aqrlei.graduation.yueting.ui.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.aqrairsigns.aqrleilib.adapter.CommonListAdapter
import com.aqrairsigns.aqrleilib.adapter.CommonListViewHolder
import com.aqrairsigns.aqrleilib.util.FileUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.FileSelectInfo

/**
 * @Author: AqrLei
 * @Date: 2017/8/29
 */
class FileListAdapter(
        mData: List<FileSelectInfo>,
        mContext: Context,
        mResId: Int = R.layout.file_list_item,
        listener: OnInternalClick) :
        CommonListAdapter<FileSelectInfo>(mData, mContext, mResId, listener) {
    private var dirCallBack: DirCallBack? = null
    override fun bindData(holderList: CommonListViewHolder, data: FileSelectInfo) {
        val mNameTv = holderList.get(R.id.fileNameTv) as TextView
        holderList.get(R.id.selectItemIv).visibility = View.GONE
        mNameTv.text = data.fileInfo.name
        if (!data.fileInfo.isDir) {
            dirCallBack?.dirCallBack()
            holderList.get(R.id.selectItemIv).visibility = View.VISIBLE
            (holderList.get(R.id.selectItemIv) as ImageView).background.level = data.status
            val suffix = FileUtil.getFileSuffix(data.fileInfo)
            val flag = suffix == YueTingConstant.PLAY_SUFFIX_MP3
                    || suffix == YueTingConstant.PLAY_SUFFIX_APE
                    || suffix == YueTingConstant.PLAY_SUFFIX_FLAC
            mNameTv.compoundDrawables[0].level =
                    if (flag) YueTingConstant.FILE_TYPE_MUSIC
                    else YueTingConstant.FILE_TYPE_BOOK
        } else {
            mNameTv.compoundDrawables[0].level = YueTingConstant.FILE_TYPE_FOLDER
        }
    }

    override fun setInternalClick(holder: CommonListViewHolder, position: Int) {
        holder.get(R.id.selectItemIv).also {
            it.tag = position
            it.setOnClickListener(this)
        }
    }

    fun setCallBack(callBack: DirCallBack) {
        dirCallBack = callBack
    }

    interface DirCallBack {
        fun dirCallBack()
    }
}