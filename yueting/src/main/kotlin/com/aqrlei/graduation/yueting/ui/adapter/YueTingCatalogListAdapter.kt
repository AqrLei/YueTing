package com.aqrlei.graduation.yueting.ui.adapter

import android.content.Context
import android.widget.TextView
import com.aqrairsigns.aqrleilib.adapter.CommonListAdapter
import com.aqrairsigns.aqrleilib.adapter.CommonListViewHolder
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.model.local.ChapterInfo

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/29
 */
class YueTingCatalogListAdapter(
        mData: List<ChapterInfo>,
        mContext: Context,
        mResId: Int,
        isCatalog: Boolean)
    : CommonListAdapter<ChapterInfo>(mData, mContext, mResId) {
    private var flag = isCatalog
    override fun bindData(holderList: CommonListViewHolder, data: ChapterInfo) {
        if (flag) {
            (holderList.get(R.id.tv_catalog_name) as TextView).text = data.chapterName
        } else {

        }
    }

    override fun setInternalClick(holder: CommonListViewHolder) {

    }
}