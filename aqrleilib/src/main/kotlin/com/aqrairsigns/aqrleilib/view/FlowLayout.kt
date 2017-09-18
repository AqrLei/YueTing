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
class FlowLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null) :
        ViewGroup(context, attrs) {
    private var mViews = HashMap<Int, List<View>>()
    //测量
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        measureChildBeforeLayout(widthMeasureSpec, heightMeasureSpec)

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

        setMeasuredDimension(wResult, hResult)

    }

    //测量每个子控件
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

    //获取所有子控件的总宽
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

    //获取所有子控件的总高
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

            //如果当前的行宽在加上一个childView的宽度大于此行最大宽度
            if (rowWidth + childWidth > maxWidth) {
                if (colIndex == 0) {// 首次进来时位于第一列
                    totalHeight += childHeight//此时childHeight 即为此行高度
                    rowWidth = paddingLeft + paddingRight//重置行宽
                    rowHeight = 0//重置行高
                } else {//未位于第一列（状态：换到当前行，且当前行colIndex(0)占据）
                    totalHeight += rowHeight// 加上上一行的行高
                    rowWidth = paddingLeft + paddingRight + childWidth//重置行宽为当前行的宽度值
                    rowHeight = childHeight//高度为当前的childHeight
                    colIndex = 1// 重置列索引为1
                }
            } else {//未大于最大宽度
                colIndex++
                rowWidth += childWidth// 行宽增加一个当前的childWidth
                rowHeight = Math.max(rowHeight, childHeight)//取高度最大为行高
            }
        }
        //加上最后一个rowHeight
        totalHeight += rowHeight
        return totalHeight

    }

    //布局
    override fun onLayout(b: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        mViews.clear()// 清除历史残留数据
        layoutChild()// 将布局信息存入 mViews
        var rowTop = paddingTop// parent 的Top， 布局第一行时用
        for (i in 0 until mViews.size) {
            val lineViews = mViews[i]
            var rowLeft = paddingLeft
            lineViews?.forEach {
                val lp = it.layoutParams as MarginLayoutParams
                val l = rowLeft + lp.leftMargin
                val t = rowTop + lp.topMargin
                //(left, top, right, bottom) 布局子控件
                it.layout(l, t, l + it.measuredWidth, t + it.measuredHeight)
                // 更新rowLeft，将当前已布局的子控件加上
                rowLeft += it.measuredWidth + lp.leftMargin + lp.rightMargin
            }
            /*根据子View的最大行高, 递增rowTop*/
            rowTop += getRowMaxHeight(i)
        }

    }

    //布局子控件
    private fun layoutChild() {
        var rowIndex = 0//行索引
        var lineViews = ArrayList<View>()// 某行上的控件集合
        var colIndex = 0//列索引
        var rowWidth = paddingLeft + paddingRight//行宽

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as MarginLayoutParams

            if (View.GONE == child.visibility) continue

            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin

            if (childWidth + rowWidth > measuredWidth) {// 大于最大宽度
                if (colIndex == 0) {// 首次进入，当前行只有一个子View
                    lineViews.add(child)
                    mViews.put(rowIndex, lineViews)
                    lineViews = ArrayList() // 当前行已添加完毕，引用一个新的堆内存区
                    rowWidth = paddingLeft + paddingRight//重置宽度
                } else {//当前行不止一个子View(状态：换到当前行，且当前行已有一个子View）
                    mViews.put(rowIndex, lineViews)//将上一行的Views添加
                    lineViews = ArrayList()
                    lineViews.add(child)//引用新的堆内存区， 将当前的子View加入
                    rowWidth = paddingLeft + paddingRight + childWidth//重置宽度
                    colIndex = 1//列索引为1
                }
                rowIndex++// 行上的子View添加完毕，行索引递增
            } else {// 未大于总宽
                colIndex++//列索引递增
                rowWidth += childWidth// 行宽增加
                lineViews.add(child)// 当前行添加子View
            }
        }
        //将最后行的子View添加
        mViews.put(rowIndex, lineViews)
    }

    //获取最大行高
    private fun getRowMaxHeight(rowIndex: Int): Int {
        val views = mViews[rowIndex]
        var maxHeight = 0
        views?.forEach {
            val lp = it.layoutParams as MarginLayoutParams
            maxHeight = Math.max(maxHeight, it.measuredHeight + lp.bottomMargin + lp.topMargin)
        }
        return maxHeight
    }

    //生成该ViewGroup的属性，返回对应的属性类型，否则会报类型转化错误
    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams =
            ViewGroup.MarginLayoutParams(context, attrs)

}