package com.aqrlei.graduation.truckrental.baselib.util.adapter

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/24
 */
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