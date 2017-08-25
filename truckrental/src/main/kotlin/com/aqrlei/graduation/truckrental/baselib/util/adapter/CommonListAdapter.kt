package com.aqrlei.graduation.truckrental.baselib.util.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/24
 */
abstract class CommonListAdapter<T>(protected var mData: List<T>, protected var mContext: Context,
                                    protected var mGroupResId: Int) : BaseAdapter() {
    override fun getCount() = mData.size

    override fun getItemId(position: Int) = position.toLong()
    override fun getItem(position: Int) = mData[position]


    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        val holder = CommonListViewHolder.getCommonViewHolder(mContext, mGroupResId, position,
                convertView, parent)
        bindData(holder, mData[position])
        return holder.convertView

    }

    protected abstract fun bindData(holderList: CommonListViewHolder, t: T)
}