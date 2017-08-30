package com.aqrlei.graduation.yueting.baselib.util.adapter

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
/*
* @param mData 用于显示的数据
* @param mContext 上下文关系
* @param mResId 布局的Id
* */
abstract class CommonListAdapter<T>() : BaseAdapter(), View.OnClickListener {
    protected lateinit var mData: List<T>
    protected lateinit var mContext: Context
    protected var mResId: Int = 0
    protected var mListener: OnInternalClick? = null

    internal constructor(mData: List<T>,
                         mContext: Context,
                         mResId: Int) : this() {
        this.mData = mData
        this.mContext = mContext
        this.mResId = mResId
    }

    internal constructor(mData: List<T>,
                         mContext: Context,
                         mResId: Int,
                         listener: OnInternalClick) : this() {
        this.mData = mData
        this.mContext = mContext
        this.mResId = mResId
        this.mListener = listener

    }


    override fun getCount() = mData.size
    override fun getItemId(position: Int) = position.toLong()
    /*将具体数据绑定到对应的位置上，一般在点击事件中保证对应的位置返回的是正确的数据*/
    override fun getItem(position: Int) = mData[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder = CommonListViewHolder.getCommonViewHolder(mContext, mResId, position,
                convertView, parent)
        bindData(holder, mData[position])
        if (mListener != null) {
            setInternalClick(holder)
        }
        return holder.convertView
    }

    /*绑定具体的数据到相应的布局上，由具体的类来实现*/
    protected abstract fun bindData(holderList: CommonListViewHolder, t: T)

    protected abstract fun setInternalClick(holder: CommonListViewHolder)

    override fun onClick(v: View) {
        mListener?.onInternalClick(v)
    }

    interface OnInternalClick {
        fun onInternalClick(v: View)
    }
}