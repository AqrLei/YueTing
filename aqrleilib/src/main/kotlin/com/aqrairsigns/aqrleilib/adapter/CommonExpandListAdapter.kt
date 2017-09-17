package com.aqrairsigns.aqrleilib.adapter

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
/*
* @param mContext 上下文
* @param mData 传入的数据
* @param mChildResId 子布局Id
* @param mGroupResId 组布局Id
* */
/*等价与四个构造器重载*/
abstract class CommonExpandListAdapter<T> @JvmOverloads constructor(
        context: Context,
        data: List<T>,
        childResId: Int = 0,
        groupResId: Int = 0,
        listener: OnExpandListInternalClick? = null) :
        BaseExpandableListAdapter(), View.OnClickListener {
    protected var mContext: Context = context
    protected var mData: List<T> = data
    protected var mChildResId: Int = childResId
    protected var mGroupResId: Int = groupResId
    protected var mListenerm: OnExpandListInternalClick? = listener


    companion object {
        @JvmStatic protected val TYPE_GROUP: Int = 1
        @JvmStatic protected val TYPE_CHILD: Int = 0
    }

    /*设置是否可以选中*/
    override fun isChildSelectable(groupPosition: Int, childPosition: Int) = true

    /*返回未为false时，在调用notifyDataSetChanged时只会调用那些getItem变化的getView方法*/
    override fun hasStableIds() = false

    override fun getGroupCount() = mData.size
    override fun getGroupId(groupPosition: Int) = groupPosition.toLong()
    override fun getChildId(groupPosition: Int, childPosition: Int) = childPosition.toLong()
    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean,
                              convertView: View?, parent: ViewGroup): View {
        val childViewHolder = CommonListViewHolder.getCommonViewHolder(mContext, mChildResId,
                childPosition, convertView, parent)
        bindData(childViewHolder, mData, groupPosition, childPosition, TYPE_CHILD)
        if (mListenerm != null) {
            setExpandListInternalClick(childViewHolder, TYPE_CHILD)
        }
        return childViewHolder.convertView
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?,
                              parent: ViewGroup): View {
        val groupViewHolder = CommonListViewHolder.getCommonViewHolder(mContext, mGroupResId,
                groupPosition, convertView, parent)
        bindData(groupViewHolder, mData, groupPosition, 0, TYPE_GROUP)
        /*为true时设置groupView不可点击*/
        // groupViewHolder.convertView.isClickable = true
        if (mListenerm != null) {
            setExpandListInternalClick(groupViewHolder, TYPE_GROUP)
        }
        return groupViewHolder.convertView
    }

    protected abstract fun bindData(holder: CommonListViewHolder, data: List<T>, groupPosition: Int,
                                    childPosition: Int, type: Int)

    /*监听事件绑定哪个子项的View由具体实现类确定*/
    protected abstract fun setExpandListInternalClick(holder: CommonListViewHolder, type: Int)

    override fun onClick(v: View) {
        mListenerm?.onExpandListInternalClick(v)
    }

    /*回调接口，用于子项控件点击事件*/
    interface OnExpandListInternalClick {
        fun onExpandListInternalClick(v: View)
    }
}