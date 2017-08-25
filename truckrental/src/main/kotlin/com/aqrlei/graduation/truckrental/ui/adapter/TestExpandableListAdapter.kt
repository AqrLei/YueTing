package com.aqrlei.graduation.truckrental.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.aqrlei.graduation.truckrental.R
import com.aqrlei.graduation.truckrental.baselib.util.adapter.CommonListViewHolder
import com.aqrlei.graduation.truckrental.model.local.ChatMessage
import com.aqrlei.graduation.truckrental.model.local.ChildMessage

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/25
 */
class TestExpandableListAdapter(private var mContext: Context,
                                private var mData: List<ChatMessage>,
                                private var childResId: Int,
                                private var groupResId: Int) : BaseExpandableListAdapter() {
    override fun getGroup(groupPosition: Int)=mData[groupPosition].content ?: "--"

    override fun getChild(groupPosition: Int, childPosition: Int)=
            mData[groupPosition].child?.get(childPosition)

    private fun bindData(holder: CommonListViewHolder, data: String) {

        (holder.get(R.id.tv_list_title) as TextView).text = data
    }

    private fun bindData(holder: CommonListViewHolder, data: ChildMessage?) {
        val contentView = (holder.get(R.id.tv_list_content) as TextView)
        contentView.text = data!!.name
        val drawable: Drawable = data.childDrawable
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        contentView.setCompoundDrawablesRelative(drawable, null, null, null)

    }
    override fun getChildrenCount(groupPosition: Int) = mData[groupPosition].child?.size ?: 0





    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean,
                              convertView: View?, parent: ViewGroup): View {
        val childViewHolder = CommonListViewHolder.getCommonViewHolder(mContext, childResId,
                childPosition, convertView, parent)
        bindData(childViewHolder, getChild(groupPosition, childPosition))
        return childViewHolder.convertView

    }


    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?,
                              parent: ViewGroup): View {
        val groupViewHolder = CommonListViewHolder.getCommonViewHolder(mContext, groupResId,
                groupPosition, convertView, parent)
        //(groupViewHolder.get(R.id.tv_list_title) as TextView).text =
        bindData(groupViewHolder, getGroup(groupPosition))
        groupViewHolder.convertView.isClickable = true
        return groupViewHolder.convertView

    }
    override fun isChildSelectable(p0: Int, p1: Int) = true
    override fun hasStableIds() = false
    override fun getGroupId(groupPosition: Int) = groupPosition.toLong()
    override fun getChildId(groupPosition: Int, childPosition: Int) = childPosition.toLong()
    override fun getGroupCount() = mData.size

}