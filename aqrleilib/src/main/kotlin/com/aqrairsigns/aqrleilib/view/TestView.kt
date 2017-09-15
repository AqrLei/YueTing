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
    private var mViews = HashMap<Int, List<View>>()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {


        val wSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val wSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        var wResult = wSpecSize

        when (wSpecMode) {
        /*wrap_content, 其它默认*/
            MeasureSpec.AT_MOST -> {
                wResult = Math.min(wResult, getChildTotalWidth())
            }
        }
        val hSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val hSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        var hResult = hSpecSize

        when (hSpecMode) {
        /*wrap_content, 其它默认*/
            MeasureSpec.AT_MOST -> {
                hResult = Math.min(hResult, getChildTotalHeight(wResult))
            }
        }

        measureChildBeforeLayout(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(wResult, hResult)

    }

    private fun measureChildBeforeLayout(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as ViewGroup.MarginLayoutParams

            if (View.GONE == child.visibility) continue

            val widthUsed = lp.leftMargin + lp.rightMargin + paddingRight + paddingLeft
            val heightUsed = lp.topMargin + lp.bottomMargin + paddingTop + paddingBottom

            measureChildWithMargins(child, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed)
        }
    }

    private fun getChildTotalWidth(): Int {
        var totalWidth = paddingLeft + paddingRight
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as MarginLayoutParams
            if (View.GONE == child.visibility) continue
            totalWidth += (child.measuredWidth + lp.leftMargin + lp.rightMargin)
        }
        return totalWidth
    }

    /*需要修改*/
    private fun getChildTotalHeight(maxWidth: Int): Int {
        var totalHeight = paddingTop + paddingBottom// 总高度，先加上内边距
        var colIndex = 0 //第几列的子控件
        var rowWidth = paddingLeft + paddingRight//行宽
        var rowHeight = 0//行高

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as MarginLayoutParams

            if (View.GONE == child.visibility) continue

            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin
            if (rowWidth + childWidth > maxWidth) {//如果当前的行宽在加上一个childView的宽度大于此行最大宽度
                if (colIndex == 0) {// 位于第一列
                    totalHeight += childHeight
                    rowWidth = paddingLeft + paddingRight
                    rowHeight = 0
                } else {
                    totalHeight += rowHeight
                    rowWidth = paddingLeft + paddingRight + childWidth
                    rowHeight = childHeight
                    colIndex = 1
                }
            } else {
                colIndex++
                rowWidth += childWidth
                rowHeight = Math.max(rowHeight, childHeight)
            }
        }
        totalHeight += rowHeight
        return totalHeight

    }


    override fun onLayout(b: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        mViews.clear()
        layoutChild()
        var rowTop = paddingTop
        for (i in 0 until mViews.size) {
            val lineViews = mViews[i]
            var rowLeft = paddingLeft
            lineViews?.forEach {
                val lp = it.layoutParams as MarginLayoutParams
                val l = rowLeft + lp.leftMargin
                val t = rowTop + lp.topMargin
                it.layout(l, t, l + it.measuredWidth, t + it.measuredHeight)
                rowLeft += it.measuredWidth + lp.leftMargin + lp.rightMargin
            }
            rowTop += getRowMaxHeight(i)
        }

    }

    private fun layoutChild() {
        var rowIndex = 0
        var lineViews = ArrayList<View>()
        var colIndex = 0
        var rowWidth = paddingLeft + paddingRight

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as MarginLayoutParams
            if (View.GONE == child.visibility) continue
            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            if (childWidth + rowWidth > measuredWidth) {
                if (colIndex == 0) {
                    lineViews.add(child)
                    mViews.put(rowIndex, lineViews)
                    lineViews = ArrayList()
                    rowWidth = paddingLeft + paddingRight
                } else {
                    mViews.put(rowIndex, lineViews)
                    lineViews = ArrayList()
                    lineViews.add(child)
                    rowWidth = paddingLeft + paddingRight + childWidth
                    colIndex = 1
                }
                rowIndex++
            } else {
                colIndex++
                rowWidth += childWidth
                lineViews.add(child)
            }
        }
        mViews.put(rowIndex, lineViews)
    }

    private fun getRowMaxHeight(rowIndex: Int): Int {
        val views = mViews[rowIndex]
        var maxHeight = 0
        views?.forEach {
            val lp = it.layoutParams as MarginLayoutParams
            maxHeight = Math.max(maxHeight, it.measuredHeight + lp.bottomMargin + lp.topMargin)
        }
        return maxHeight
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams =
            ViewGroup.MarginLayoutParams(context, attrs)

}