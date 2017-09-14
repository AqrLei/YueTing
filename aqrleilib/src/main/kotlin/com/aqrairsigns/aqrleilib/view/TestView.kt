package com.aqrairsigns.aqrleilib.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/14 Time: 16:56
 */
class TestView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null) :
        ViewGroup(context, attrs) {
    private var mViews: Map<Int, List<View>>? = null

    init {
        initView()
    }

    private fun initView() {
        mViews = HashMap()
    }

    private fun measureChildBeforeLayout(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        for (i in 0 until childCount) {
            val child: View = getChildAt(i)
            val lp: MarginLayoutParams = child.layoutParams as ViewGroup.MarginLayoutParams
            if (View.GONE == child.visibility) continue
            val widthUsed = lp.leftMargin + lp.rightMargin
            val heightUsed = lp.topMargin + lp.bottomMargin
            measureChildWithMargins(child, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed)
        }

    }

    private fun getChildTotalWidth(): Int {
        var totalWidth = paddingLeft + paddingRight
        for (i in 0 until childCount) {
            val child: View = getChildAt(i)
            val lp: MarginLayoutParams = child.layoutParams as MarginLayoutParams
            if (View.GONE == child.visibility) continue
            totalWidth += (child.measuredWidth + lp.leftMargin + lp.rightMargin)
        }
        return totalWidth
    }

    private fun getChildTotalHeight() {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}