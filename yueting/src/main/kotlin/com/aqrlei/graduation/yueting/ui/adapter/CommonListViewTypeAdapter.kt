package com.aqrlei.graduation.yueting.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.aqrairsigns.aqrleilib.adapter.CommonListViewHolder
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.model.local.ChatMessage

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/25
 */

/*
* 多(2)个ViewType的Adapter实现
* ViewHolder采用通用的CommonListViewHolder
* @param mContext 上下文关系
* @param titleResId 标题布局的Id
* @param contentResId 内容布局的Id
* @param mContentData 内容数据
* @param mTitleData 标题数据（也可在构造器中传入）
* */
class CommonListViewTypeAdapter(private var mContext: Context, private var titleResId: Int,
                                private var contentResId: Int,
                                private var mContentData: List<ChatMessage>) : BaseAdapter() {
    private val mTitleData = ArrayList<String>()

    init {
        mTitleData.add("标题")
    }

    /*设置两种布局类型的标志*/
    companion object {
        val TYPE_TITLE: Int = 0
        val TYPE_CONTENT: Int = 1
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var mConvertView = convertView
        var titleViewHolder: CommonListViewHolder? = null
        var contentViewHolder: CommonListViewHolder? = null
        when (getItemViewType(position)) {
            TYPE_TITLE -> {
                titleViewHolder = CommonListViewHolder.getCommonViewHolder(mContext,
                        titleResId, position, mConvertView, parent)
                mConvertView = titleViewHolder.convertView
            }
            TYPE_CONTENT -> {
                contentViewHolder = CommonListViewHolder.getCommonViewHolder(mContext,
                        contentResId, position, mConvertView, parent)
                mConvertView = contentViewHolder.convertView
            }
        }
        var obj = getItem(position)

        when (getItemViewType(position)) {
            TYPE_TITLE -> bindData(titleViewHolder!!, obj as String)
            TYPE_CONTENT -> bindData(contentViewHolder!!, obj as ChatMessage)
            else -> {

            }
        }
        return mConvertView!!
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

    override fun getItem(position: Int): Any = when (getItemViewType(position)) {
        TYPE_TITLE -> mTitleData[0]
        TYPE_CONTENT -> mContentData[position - position / 3 - 1]
        else -> {

        }
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = mContentData.size + 1

    /*
    * @return -1 表示忽视类型
    * @return  >0&&<typeCount - 1 正常返回
    * @return >= typeCount 越界，在AbsListView.getScrapView中会返回null
    * */
    override fun getItemViewType(position: Int) = (if (position % 3 == 0) TYPE_TITLE else TYPE_CONTENT)

    /*返回ViewType的数量*/
    override fun getViewTypeCount() = 2

}