package com.aqrairsigns.aqrleilib.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.SweepGradient
import android.os.Bundle
import android.os.Parcelable
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
    private var mGradientListColor: ArrayList<Int>? = null//渐变色
    private var mRadius: Float = 0F//圆半径
    private var mMaxProgress: Float = 0F//最大的进度
    private var mCurrentProgress: Float = 0f//当前进度
    private var mIsOpenAnimation: Boolean = false//是否开启动画
    private var mIsUseGradientColor: Boolean = false//是否使用渐变
    private var mStartDegree: Int = 0// 画圆开始的角度
    private var mSweepDegree: Int = 0//画圆扫过的角度
    private var mRotateDegree: Int = 0// 画布旋转的角度
    private var mProgressDegree: Int = 0//当前进度该画的角度
    private var mDrawDegree: Int = 0//画的起始角度
    private var mAnimationV: Int = 0//画的速度

    private var mListener: OnDrawProgressListener? = null//当前进度监听器

    /*标明画的时候是当前进度开始还是从头开始*/
    companion object {
        /*cap类型*/
        private val BUTT = 0
        private val ROUND = 1
        private val SQUARE = 2

        /*异常销毁需要保存的数据的key*/
        private val INSTANCE = "instance"
        private val INSTANCE_BOOLEAN = "boolean"
        private val INSTANCE_DEGREE = "degree"
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
        mMaxProgress = typedArray.getFloat(R.styleable.RoundBar_maxProgress, 10f)
        mCurrentProgress = typedArray.getFloat(R.styleable.RoundBar_progress, 5f)
        mIsOpenAnimation = typedArray.getBoolean(R.styleable.RoundBar_openAnimation, false)
        mIsUseGradientColor = typedArray.getBoolean(R.styleable.RoundBar_useGradientGolor, false)
        val mRoundWidth = typedArray.getDimension(R.styleable.RoundBar_roundWidth, 10f)
        val capStyle = typedArray.getInteger(R.styleable.RoundBar_capStyle, 0)
        mStartDegree = typedArray.getInteger(R.styleable.RoundBar_startDegree, 0)
        mSweepDegree = typedArray.getInteger(R.styleable.RoundBar_sweepDegree, 360)
        mRotateDegree = typedArray.getInteger(R.styleable.RoundBar_rotateDegree, 0)
        mAnimationV = typedArray.getInteger(R.styleable.RoundBar_animationVelocity, 1)
        mDrawDegree = 0
        mGradientListColor = ArrayList()
        mGradientListColor?.add(Color.parseColor("#21ADF1"))
        mGradientListColor?.add(Color.parseColor("#2287EE"))
        mPaint.strokeWidth = mRoundWidth
        mPaint.style = Paint.Style.STROKE
        when (capStyle) {
            BUTT -> mPaint.strokeCap = Paint.Cap.BUTT
            ROUND -> mPaint.strokeCap = Paint.Cap.ROUND
            SQUARE -> mPaint.strokeCap = Paint.Cap.SQUARE
        }
        setProgress(mCurrentProgress)
        typedArray.recycle()
    }

    fun setGradientColor(gradientColor: ArrayList<Int>?) {
        if (gradientColor != null && gradientColor.size < 2) {
            throw IllegalArgumentException("needs >= 2 number of colors")
        }
        mGradientListColor = gradientColor
    }

    fun getGradientColor(): ArrayList<Int>? {
        return mGradientListColor
    }

    @Synchronized
    fun setProgress(progress: Float) {
        mCurrentProgress = progress
        mProgressDegree = (mSweepDegree * (mCurrentProgress / mMaxProgress)).toInt()
        mProgressDegree = if (mProgressDegree > mSweepDegree) mSweepDegree else mProgressDegree
        mDrawDegree = 0
        postInvalidate()
    }

    @Synchronized
    fun setMaxProgress(maxProgress: Float) {
        mMaxProgress = maxProgress
        setProgress(mCurrentProgress)
    }

    @Synchronized
    fun getProgress(): Float {
        return mCurrentProgress
    }

    @Synchronized
    fun getMaxProgress(): Float {
        return mMaxProgress
    }

    fun setProgressColor(color: Int) {
        mProgressColor = color
    }

    override fun setBackgroundColor(color: Int) {
        mBackgroundColor = color
    }

    fun setUseGradientColor(useGradientColor: Boolean) {
        mIsUseGradientColor = useGradientColor
    }

    fun setOpenAnimation(openAnimation: Boolean) {
        mIsOpenAnimation = openAnimation
    }

    fun setOnDrawProgressListener(listener: OnDrawProgressListener) {
        mListener = listener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
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
        val rStartDegree = mStartDegree - mRotateDegree
        canvas?.save()
        canvas?.rotate(mRotateDegree.toFloat(), centerX, centerY)
        mPaint.shader = null
        mPaint.color = mBackgroundColor
        canvas?.drawArc(left, top, right, bottom, rStartDegree.toFloat(),
                mSweepDegree.toFloat(), false, mPaint)
        if (mIsUseGradientColor && mGradientListColor != null) {
            mPaint.shader = SweepGradient(centerX, centerY,
                    mGradientListColor!![0], mGradientListColor!![1])
        } else {
            mPaint.color = mProgressColor
        }
        if (mIsOpenAnimation) {
            val ratio = mDrawDegree.toFloat() / mProgressDegree
            mListener?.onDrawProgressRatio(ratio)
            val drawArc = canvas?.drawArc(left, top, right, bottom, mStartDegree.toFloat(),
                    mDrawDegree.toFloat(), false, mPaint)


            if (mDrawDegree < mProgressDegree) {
                mDrawDegree += mAnimationV
                mDrawDegree = Math.min(mDrawDegree, mProgressDegree)
                invalidate()
            }
        } else {
            canvas?.drawArc(left, top, right, bottom, mStartDegree.toFloat(),
                    mProgressDegree.toFloat(), false, mPaint)
        }
        canvas?.restore()
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState())
        val bool = booleanArrayOf(mIsOpenAnimation, mIsUseGradientColor)
        val degree = intArrayOf(mStartDegree, mSweepDegree, mProgressDegree, mRotateDegree)
        degree as ArrayList<Int>
        with(bundle) {
            putBooleanArray(INSTANCE_BOOLEAN, bool)
            putIntegerArrayList(INSTANCE_DEGREE, degree)
        }
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            with(state) {
                val bool = getBooleanArray(INSTANCE_BOOLEAN)
                val degree = getIntegerArrayList(INSTANCE_DEGREE)
                mIsOpenAnimation = bool[0]
                mIsUseGradientColor = bool[1]
                mStartDegree = degree[0]
                mSweepDegree = degree[1]
                mProgressDegree = degree[2]
                mRotateDegree = degree[3]
                super.onRestoreInstanceState(getParcelable(INSTANCE))
            }
            return
        }
        super.onRestoreInstanceState(state)
    }

    /*回调接口，传递 当前进度与已完成进度的比值*/
    interface OnDrawProgressListener {
        fun onDrawProgressRatio(ratio: Float)
    }
}


