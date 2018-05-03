package com.aqrlei.graduation.yueting.ui.adapter

import android.content.Context
import android.widget.TextView
import com.aqrairsigns.aqrleilib.adapter.CommonListAdapter
import com.aqrairsigns.aqrleilib.adapter.CommonListViewHolder
import com.aqrairsigns.aqrleilib.info.FileInfo
import com.aqrairsigns.aqrleilib.util.FileUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.YueTingConstant

/**
 * @Author: AqrLei
 * @Date: 2017/8/29
 */
class FileListAdapter(mData: List<FileInfo>, mContext: Context, mResId: Int) :
        CommonListAdapter<FileInfo>(mData, mContext, mResId) {
    override fun bindData(holderList: CommonListViewHolder, data: FileInfo) {
        val mNameTv = holderList.get(R.id.tv_book_name) as TextView
        mNameTv.text = data.name
        if (!data.isDir) {
            val suffix = FileUtil.getFileSuffix(data)
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

    override fun setInternalClick(holder: CommonListViewHolder) {}
}