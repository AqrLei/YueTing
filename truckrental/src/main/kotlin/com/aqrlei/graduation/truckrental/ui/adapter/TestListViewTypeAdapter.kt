package com.aqrlei.graduation.truckrental.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.aqrlei.graduation.truckrental.R
import com.aqrlei.graduation.truckrental.baselib.util.adapter.CommonListViewHolder
import com.aqrlei.graduation.truckrental.model.local.ChatMessage

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/25
 */
class TestListViewTypeAdapter(private var mContext: Context, private var titleResId: Int,
                              private var contentResId: Int,
                              private var mContentData: List<ChatMessage>) : BaseAdapter() {
    private val mTitleData = ArrayList<String>()

    init {
        mTitleData.add("标题")

    }


    companion object {
        val TYPE_TITLE: Int = 0
        val TYPE_CONTENT: Int = 1
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var titleViewHolder: CommonListViewHolder? = null
        var contentViewHolder: CommonListViewHolder? = null

        when (getItemViewType(position)) {
            TYPE_TITLE -> {
                titleViewHolder = CommonListViewHolder.getCommonViewHolder(mContext,
                        titleResId, position, convertView, parent)
                convertView = titleViewHolder.convertView
            }
            TYPE_CONTENT -> {
                contentViewHolder = CommonListViewHolder.getCommonViewHolder(mContext,
                        contentResId, position, convertView, parent)
                convertView = contentViewHolder.convertView
            }
        }
        var obj = getItem(position)

        when (getItemViewType(position)) {
            TYPE_TITLE -> bindData(titleViewHolder!!, obj as String)
            TYPE_CONTENT -> bindData(contentViewHolder!!, obj as ChatMessage)
            else -> {

            }
        }
        return convertView!!
    }

    private fun bindData(holder: CommonListViewHolder, data: String) {

        (holder.get(R.id.tv_list_title) as TextView).text = data
    }

    private fun bindData(holder: CommonListViewHolder, data: ChatMessage) {
        var contentView = (holder.get(R.id.tv_list_content) as TextView)
        contentView.text = data.content
        var drawable: Drawable = data.avatar
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        contentView.setCompoundDrawablesRelative(drawable, null, null, null)

    }

    override fun getItem(position: Int): Any
            = when (getItemViewType(position)) {
        TYPE_TITLE -> mTitleData[0]
        TYPE_CONTENT -> mContentData[position - position / 3 - 1]
        else -> {

        }
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = mContentData.size + 1
    override fun getItemViewType(position: Int) = (if (position % 3 == 0) TYPE_TITLE else TYPE_CONTENT)
    override fun getViewTypeCount() = 2

}