package com.aqrlei.graduation.yueting.ui.adapter

import android.content.Context
import android.widget.TextView
import com.aqrairsigns.aqrleilib.adapter.CommonListAdapter
import com.aqrairsigns.aqrleilib.adapter.CommonListViewHolder
import com.aqrairsigns.aqrleilib.info.Info.FileInfo
import com.aqrlei.graduation.yueting.R

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/29
 */
class FileListAdapter(mData: List<FileInfo>, mContext: Context, mResId: Int) :
        CommonListAdapter<FileInfo>(mData, mContext, mResId) {
    override fun bindData(holderList: CommonListViewHolder, data: FileInfo) {
        val mNameTv = holderList.get(R.id.tv_book_name) as TextView
        mNameTv.text = data.name
        if (!data.isDir) {
            val drawable = mContext.getDrawable(R.mipmap.ic_launcher_round)
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            mNameTv.setCompoundDrawables(drawable, null, null, null)
        } else {
            val drawable = mContext.getDrawable(R.mipmap.ic_launcher)
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            mNameTv.setCompoundDrawables(drawable, null, null, null)
        }
    }

    override fun setInternalClick(holder: CommonListViewHolder) {

    }
}