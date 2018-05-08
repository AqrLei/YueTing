package com.aqrlei.graduation.yueting.ui.adapter

import android.content.Context
import android.widget.TextView
import com.aqrlei.graduation.yueting.adapter.CommonListAdapter
import com.aqrlei.graduation.yueting.adapter.CommonListViewHolder
import com.aqrlei.graduation.yueting.R

/**
 * @author  aqrLei on 2018/5/4
 */
class PopViewListAdapter(
        data: List<String>,
        context: Context,
        resId: Int = R.layout.title_list_item) :
        CommonListAdapter<String>(data, context, resId) {
    override fun bindData(holderList: CommonListViewHolder, t: String) {
        (holderList.get(R.id.titleNameTv) as TextView).text = t

    }

    override fun setInternalClick(holder: CommonListViewHolder, position: Int) {}
}