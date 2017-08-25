package com.aqrlei.graduation.truckrental.baselib.util.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/25
 */
abstract class CommonExpandListAdapter<T>(protected var mContext: Context,
                                          protected var mData: List<T>,
                                          protected var childResId: Int,
                                          protected var groupResId: Int) :
        BaseExpandableListAdapter() {
    companion object {
        private val TYPE_GROUP: Boolean = true
        private val TYPE_CHILD: Boolean = false
    }
    override fun isChildSelectable(p0: Int, p1: Int) = true
    override fun hasStableIds() = false
    override fun getGroupCount() = mData.size
    override fun getGroupId(groupPosition: Int) = groupPosition.toLong()
    override fun getChildId(groupPosition: Int, childPosition: Int) = childPosition.toLong()
    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean,
                              convertView: View?, parent: ViewGroup): View {
        val childViewHolder = CommonListViewHolder.getCommonViewHolder(mContext, childResId,
                childPosition, convertView, parent)
        bindData(childViewHolder, mData, TYPE_CHILD)
        return childViewHolder.convertView
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?,
                              parent: ViewGroup): View {
        val groupViewHolder = CommonListViewHolder.getCommonViewHolder(mContext, groupResId,
                groupPosition, convertView, parent)
        /*设置groupView不可点击*/
        bindData(groupViewHolder, mData, TYPE_GROUP)
        groupViewHolder.convertView.isClickable = true
        return groupViewHolder.convertView
    }

    protected abstract fun bindData(holder: CommonListViewHolder, data: List<T>, isGroup: Boolean)


}