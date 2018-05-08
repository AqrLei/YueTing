package com.aqrlei.graduation.yueting.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

/**
 * @Author: AqrLei
 * @Date: 2017/8/24
 */
/*
* @param mData 用于显示的数据
* @param mContext 上下文关系
* @param mResId 布局的Id
* @param listener 子项上的控件监听器
* */
/*等价于3个构造器重载*/
abstract class CommonListAdapter<T> @JvmOverloads constructor(
        data: List<T>,
        context: Context,
        resId: Int = 0,
        listener: OnInternalClick? = null) :
        BaseAdapter(), View.OnClickListener {
    protected var mData: List<T> = data
    protected var mContext: Context = context
    protected var mResId: Int = resId
    protected var mListener: OnInternalClick? = listener
    protected lateinit var holder: CommonListViewHolder

    override fun getCount() = mData.size
    override fun getItemId(position: Int) = position.toLong()
    /*将具体数据绑定到对应的位置上，一般在点击事件中保证对应的位置返回的是正确的数据*/
    override fun getItem(position: Int) = mData[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        holder = CommonListViewHolder.getCommonViewHolder(mContext, mResId, position,
                convertView, parent)
        bindData(holder, mData[position])
        if (mListener != null) {
            setInternalClick(holder,position)
        }
        return holder.convertView
    }

    /*绑定具体的数据到相应的布局上，由具体的类来实现*/
    protected abstract fun bindData(holderList: CommonListViewHolder, t: T)

    /*监听事件绑定哪个子项的View由具体实现类确定*/
    protected abstract fun setInternalClick(holder: CommonListViewHolder,position: Int)

    override fun onClick(v: View) {
        val position = v.tag as? Int
        position?.let {
            mListener?.onInternalClick(v, position)
        }

    }

    /*回调接口，用于子项控件点击事件*/
    interface OnInternalClick {
        fun onInternalClick(v: View, position: Int)
    }
}