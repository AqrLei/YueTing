package com.aqrlei.graduation.truckrental.baselib.util.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/24
 */
abstract class CommonRecylerAdapter<T>(protected var mData: List<T>, protected var resId: Int) :
        RecyclerView.Adapter<CommonRecyclerViewHolder>() {
    protected var mLongListener: OnItemLongClickListener? = null
    protected var mListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonRecyclerViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(resId, parent, false)
        return CommonRecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: CommonRecyclerViewHolder, position: Int) {
        if (mLongListener != null) {
            holder.itemView.setOnLongClickListener({
                val position = holder.layoutPosition
                mLongListener!!.onItemLongClickListener(holder.itemView, position)
                true
            })
        }
        if (mListener != null) {
            holder.itemView.setOnClickListener({
                val position = holder.layoutPosition
                mListener!!.onItemClickListener(holder.itemView, position)
            })
        }
        bindData(holder, mData, position)

    }

    override fun getItemCount() = mData.size

    protected abstract fun bindData(holder: CommonRecyclerViewHolder, data: List<T>, position: Int)
    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        mLongListener = listener
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    interface OnItemLongClickListener {
        fun onItemLongClickListener(view: View, position: Int)
    }

    interface OnItemClickListener {
        fun onItemClickListener(view: View, position: Int)
    }

}