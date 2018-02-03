package com.aqrlei.graduation.yueting.ui.adapter

import android.content.Context
import android.widget.TextView
import com.aqrairsigns.aqrleilib.adapter.CommonListAdapter
import com.aqrairsigns.aqrleilib.adapter.CommonListViewHolder
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.model.local.BookMessage

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/29
 */
class YueTingReadListAdapter(mData: List<BookMessage>, mContext: Context, mResId: Int) :
        CommonListAdapter<BookMessage>(mData, mContext, mResId) {
    override fun bindData(holderList: CommonListViewHolder, data: BookMessage) {
        (holderList.get(R.id.tv_book_name) as TextView).text = data.name
    }

    override fun setInternalClick(holder: CommonListViewHolder) {

    }
}