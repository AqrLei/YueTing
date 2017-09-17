package com.aqrairsigns.aqrleilib.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ExpandableListView
import com.aqrairsigns.aqrleilib.R
import com.aqrairsigns.aqrleilib.util.DensityUtil

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/30
 */

/*
* @param context 上下文
* @param attrs 属性
* @param defStyleAttr 默认属性
* @param defStyleRes: 默认资源
* @description: 声明一个回调接口，用于根据返回的数据更改相应视图的透明度
* */

/*等价与重写父类的四个构造方法*/
class AlphaExpandListView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0) :
        ExpandableListView(context, attrs, defStyleAttr, defStyleRes) {
    private var mContext: Context = context
    private var mListener: OnAlphaChangeListener? = null
    private var mMinusHeight: Float = 0F
    private var mMinHeight: Float = 0F

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        getScrollHeight()

    }

    init {
        init(attrs)
    }

    fun init(attrs: AttributeSet?) {
        val typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.AlphaExpandListView)
        mMinusHeight = typedArray.getFloat(R.styleable.AlphaExpandListView_expandMinusHeight, 0F)
        mMinHeight = typedArray.getFloat(R.styleable.AlphaExpandListView_expandMinHeight, 40F)
        typedArray.recycle()
    }

    private fun getScrollHeight() {
        if (childCount == 0) {
            return
        }
        val first = getChildAt(0)
        var height = if (first is ViewGroup) {
            first.getChildAt(0).height
        } else {
            first.height
        }
        // height -= DensityUtil.dipToPx(mContext, 20F)
        /*第一个item为40dp,需要改进*/
        val top = first.top
        height -= DensityUtil.dipToPx(mContext, mMinusHeight)
        if (height <= DensityUtil.dipToPx(mContext, mMinHeight)) return

        val absTop = Math.abs(top)
        val alpha: Float = absTop * 1.0f / height
        if (mListener != null) {
            mListener!!.onAlphaChanged(if (alpha >= 1) 1F else alpha)
        }
    }

    fun setAlphaChangeListener(listener: OnAlphaChangeListener) {
        mListener = listener
    }

    interface OnAlphaChangeListener {
        fun onAlphaChanged(percent: Float)
    }
}