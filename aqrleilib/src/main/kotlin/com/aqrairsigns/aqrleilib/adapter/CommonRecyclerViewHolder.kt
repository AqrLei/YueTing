package com.aqrairsigns.aqrleilib.adapter

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/24
 */
/*
* @param itemView 创建ViewHolder时传入的View
* @param mViews 保存布局View中的组件
* */
class CommonRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val mViews: SparseArray<View> = SparseArray()
    fun <T : View> get(viewId: Int): T {
        var v: View? = mViews.get(viewId)
        if (v == null) {
            v = itemView.findViewById(viewId)
            mViews.put(viewId, v)
        }

        return v as T
    }
}