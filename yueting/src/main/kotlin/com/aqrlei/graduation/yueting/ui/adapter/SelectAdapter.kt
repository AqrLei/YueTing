package com.aqrlei.graduation.yueting.ui.adapter

import android.content.Context
import android.support.annotation.LayoutRes
import android.widget.ImageView
import android.widget.TextView
import com.aqrairsigns.aqrleilib.adapter.CommonListAdapter
import com.aqrairsigns.aqrleilib.adapter.CommonListViewHolder
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.SelectInfo

/**
 * @author  aqrLei on 2018/5/3
 */
class SelectAdapter constructor(
        data: List<SelectInfo>,
        context: Context,
        val type: String,
        @LayoutRes resId: Int = R.layout.common_manage_list_item)
    : CommonListAdapter<SelectInfo>(data, context, resId) {
    override fun bindData(holderList: CommonListViewHolder, t: SelectInfo) {
        (holderList.get(R.id.itemNameTv) as TextView).text =
                if (type == YueTingConstant.MANAGE_TYPE_LIST) t.name
                else {
                    t.name.substring(t.name.lastIndexOf("/") + 1, t.name.lastIndexOf("."))
                }
        (holderList.get(R.id.selectIv) as ImageView).background.level = t.status
    }

    override fun setInternalClick(holder: CommonListViewHolder) {
        holder.get(R.id.selectItemIv).setOnClickListener(this)
    }
}