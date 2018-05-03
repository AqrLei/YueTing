package com.aqrlei.graduation.yueting.ui.adapter

import android.content.Context
import android.text.TextUtils
import android.widget.TextView
import com.aqrairsigns.aqrleilib.adapter.CommonListAdapter
import com.aqrairsigns.aqrleilib.adapter.CommonListViewHolder
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.model.ChapterInfo

/**
 * @Author: AqrLei
 * @Date: 2017/8/29
 */
class YueTingCatalogListAdapter(
        mData: List<ChapterInfo>,
        mContext: Context,
        mResId: Int)
    : CommonListAdapter<ChapterInfo>(mData, mContext, mResId) {
    override fun bindData(holderList: CommonListViewHolder, data: ChapterInfo) {
        val catalogView = holderList.get(R.id.tv_catalog_name) as TextView
        if (data.flag) {
            catalogView.setLines(1)
            catalogView.ellipsize = TextUtils.TruncateAt.END
            catalogView.textSize = 12F
            catalogView.text = data.chapterName
        } else {
            catalogView.ellipsize = TextUtils.TruncateAt.END
            catalogView.textSize = 12f
            catalogView.text = data.chapterName
        }
    }

    override fun setInternalClick(holder: CommonListViewHolder) {}
}