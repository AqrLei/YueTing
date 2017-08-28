package com.aqrlei.graduation.yueting.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.TextView
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.baselib.util.adapter.CommonExpandListAdapter
import com.aqrlei.graduation.yueting.baselib.util.adapter.CommonListViewHolder
import com.aqrlei.graduation.yueting.model.local.ChatMessage

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/25
 */
class TestExpandableListAdapter(mContext: Context,
                                mData: List<ChatMessage>,
                                childResId: Int,
                                groupResId: Int) :
        CommonExpandListAdapter<ChatMessage>(mContext, mData, childResId, groupResId) {

    override fun getChildrenCount(groupPosition: Int) = mData[groupPosition].child?.size ?: 0
    override fun bindData(holder: CommonListViewHolder, data: List<ChatMessage>, groupPosition: Int,
                          isGroup: Boolean) {
        if (isGroup) (holder.get(R.id.tv_list_title) as TextView).text = data[groupPosition].content
        else {
            val contentView = (holder.get(R.id.tv_list_content) as TextView)
            contentView.text = data[groupPosition].child?.get(holder.position)?.name ?: "--"
            val drawable: Drawable = data[groupPosition].child?.get(holder.position)?.childDrawable
                    ?: mContext.getDrawable(R.mipmap.ic_launcher_round)
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            contentView.setCompoundDrawablesRelative(drawable, null, null, null)
        }
    }

    override fun getGroup(groupPosition: Int) = mData[groupPosition].content ?: "--"
    override fun getChild(groupPosition: Int, childPosition: Int) =
            mData[groupPosition].child?.get(childPosition)
}