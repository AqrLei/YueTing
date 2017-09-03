package com.aqrairsigns.aqrleilib.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.aqrairsigns.aqrleilib.R

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/30
 */

/*
* @param context 上下文关系
* @param attrs 属性
* @param defStyleAttr 默认属性
* @param defStyleRes 默认资源ID
* @description: 画一个圆形或半圆的简单进度条
* */
class RoundBar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0) :
        View(context, attrs, defStyleAttr, defStyleRes) {
    private lateinit var mPaint: Paint//画笔
    private lateinit var mContext: Context//上下文
    private var mBackgroundColor: Int = 0//背景颜色
    private var mProgressColor: Int = 0//进度颜色
    private var mRadius: Float = 0F//圆半径
    private var mMaxProgress: Float = 0F//最大的进度
    private var mIsOpenAnimation: Boolean = false//是否开启动画
    private var mStartDegree: Int = 0// 画圆开始的角度
    private var mSweepDegree: Int = 0//画圆扫过的角度
    private var mProgressDegree: Int = 0//当前进度该画的角度
    private var mDrawDegree: Int = 0//画的起始角度

    /*标明画的时候是当前进度开始还是从头开始*/
    companion object {
        @JvmStatic
        val TYPE_CONTINUE: Int = 1
        @JvmStatic
        val TYPE_RESTART: Int = 0
    }

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        mContext = context
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        /*与对应的values文件夹中attrs.xml中相关内容关联*/
        val typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.RoundBar)
        /*val typedArray = TintTypedArray.obtainStyledAttributes(
                mContext,
                attrs,
                R.styleable.RoundBar)*/
        mBackgroundColor = typedArray.getColor(
                R.styleable.RoundBar_backgroundColor,
                Color.parseColor("#bbbbbb"))
        mProgressColor = typedArray.getColor(
                R.styleable.RoundBar_progressColor,
                Color.parseColor("#41a9f8"))
        mRadius = typedArray.getDimension(R.styleable.RoundBar_radius, 100f)
        mMaxProgress = typedArray.getFloat(R.styleable.RoundBar_max, 0f)
        val curProgress = typedArray.getFloat(R.styleable.RoundBar_progress, 0f)
        mIsOpenAnimation = typedArray.getBoolean(R.styleable.RoundBar_openAnimation, false)
        val mRoundWidth = typedArray.getDimension(R.styleable.RoundBar_roundWidth, 10f)
        val capStyle = typedArray.getInteger(R.styleable.RoundBar_capStyle, 0)
        mStartDegree = typedArray.getInteger(R.styleable.RoundBar_startDegree, 0)
        mSweepDegree = typedArray.getInteger(R.styleable.RoundBar_sweepDegree, 360)
        mDrawDegree = 0
        mPaint.strokeWidth = mRoundWidth
        mPaint.style = Paint.Style.STROKE
        when (capStyle) {
            0 -> mPaint.strokeCap = Paint.Cap.BUTT
            1 -> mPaint.strokeCap = Paint.Cap.ROUND
            2 -> mPaint.strokeCap = Paint.Cap.SQUARE
        }
        setProgress(curProgress)
        typedArray.recycle()
    }

    fun setProgress(progress: Float) {
        mProgressDegree = (mSweepDegree * (progress / mMaxProgress)).toInt()
    }

    fun setOpenAnimation(openAnimation: Boolean) {
        mIsOpenAnimation = openAnimation
    }

    fun setMaxProgress(maxProgress: Float) {
        mMaxProgress = maxProgress
    }

    fun update(type: Int) {
        when (type) {
            TYPE_CONTINUE -> {
                mDrawDegree = mProgressDegree - mDrawDegree
            }
            TYPE_RESTART -> {
                mDrawDegree = 0
            }
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val width = measuredWidth
        val height = measuredHeight
        val centerX = (width / 2).toFloat()
        val centerY = (height / 2).toFloat()
        val left = centerX - mRadius
        val top = centerY - mRadius
        val right = centerX + mRadius
        val bottom = centerY + mRadius

        mPaint.color = mBackgroundColor
        canvas?.drawArc(left, top, right, bottom, mStartDegree.toFloat(),
                mSweepDegree.toFloat(), false, mPaint)
        mPaint.color = mProgressColor
        if (mIsOpenAnimation) {
            canvas?.drawArc(left, top, right, bottom, mStartDegree.toFloat(),
                    mDrawDegree.toFloat(), false, mPaint)
            if (++mDrawDegree <= mProgressDegree) {
                invalidate()
            }
        } else {
            canvas?.drawArc(left, top, right, bottom, mStartDegree.toFloat(),
                    mProgressDegree.toFloat(), false, mPaint)
        }
    }
}
