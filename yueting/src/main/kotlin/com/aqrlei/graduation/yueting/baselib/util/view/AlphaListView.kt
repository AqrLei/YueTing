package com.aqrlei.graduation.yueting.baselib.util.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ListView
import com.aqrlei.graduation.yueting.baselib.util.AppLog
import com.aqrlei.graduation.yueting.baselib.util.DensityUtil

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/28
 */
class AlphaListView : ListView {
    private lateinit var mContext: Context
    private var mListener: OnAlphaChangeListener? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        mContext = context
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)

        getScrollHeight()
    }

    private fun getScrollHeight() {
        if (childCount == 0) {
            return
        }
        var first = getChildAt(0)
        var height = if (first is ViewGroup) {
            first.getChildAt(0).height
        } else {
            first.height
        }
        AppLog.logDebug("view", " Front Height: \t"+height)
       // height -= DensityUtil.dipToPx(mContext, 20F)
        /*第一个item为40dp*/
        if (height <= DensityUtil.dipToPx(mContext, 40F)) return
        var top = Math.abs(first.top)
        AppLog.logDebug("view", "Height: \t"+height+"Top: \t"+top)
        var alpha: Float = top * 1.0f / height
        if(mListener != null) {
            mListener!!.onAlphaChanged(if (alpha >= 1) 1F else alpha)
        }
    }
    fun setAlphaChangeListener(listener: OnAlphaChangeListener) {
        mListener = listener
    }
     interface  OnAlphaChangeListener{
        fun onAlphaChanged(percent : Float)
    }
}