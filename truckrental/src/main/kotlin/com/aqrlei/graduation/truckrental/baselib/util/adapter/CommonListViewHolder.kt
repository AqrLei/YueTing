package com.aqrlei.graduation.truckrental.baselib.util.adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/24
 */
class CommonListViewHolder constructor(context: Context, resId: Int, position: Int, parent: ViewGroup) {
    private val mViews: SparseArray<View> = SparseArray()
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    var convertView: View
    var position: Int = 0
        private set
    init {
        convertView = mLayoutInflater.inflate(resId, parent, false)
        convertView.tag = this
        this.position = position
    }

    fun  get(viewId: Int): View {
        var v: View? = mViews.get(viewId)
        if (v == null) {
            v = convertView.findViewById(viewId)
            mViews.put(viewId, v)
        }
        return v!!
    }

    companion object {
        fun getCommonViewHolder(context: Context, resId: Int, position: Int,
                                convertView: View?, parent: ViewGroup): CommonListViewHolder {
            val holderList: CommonListViewHolder
            if (convertView == null) {
                holderList = CommonListViewHolder(context, resId, position, parent)
            } else {
                holderList = convertView.tag as CommonListViewHolder
                holderList.position = position
            }
            return holderList
        }
    }


}