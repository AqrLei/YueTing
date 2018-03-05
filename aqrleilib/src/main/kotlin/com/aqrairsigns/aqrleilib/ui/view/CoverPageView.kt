package com.aqrairsigns.aqrleilib.ui.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.Scroller


/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2018/2/3.
 */
class CoverPageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) :
        ImageView(context, attrs, defStyleAttr) {
    private var mBitmap: Bitmap? = null
    private lateinit var nextPage: Bitmap
    private lateinit var currentPage: Bitmap
    private lateinit var previousPage: Bitmap
    private val shadowDrawable: GradientDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(0x66000000, 0x00000000))
    private var mPageListener: OnPageTouchListener? = null
    private var pageState = PageState.PAGEMOVE
    private var touchStyle = TouchStyle.TOUCH_MIDDLE
    private var viewHeight: Int = 0
    private var viewWidth: Int = 0
    private var defaultHeight: Int = 0
    private var defaultWidth: Int = 0
    private val mScroller = Scroller(context, LinearInterpolator())
    /**
     * 当前界面的左边界值
     */
    private var scrollPageLeft: Float = 0f
    private var xDown: Float = -1f
    private var touchPoint = MyPoint(-1f, -1f)
    private var scrollTime: Int = 300

    init {
        shadowDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
    }

    fun setBitmap(pPage: Bitmap, cPage: Bitmap, nPage: Bitmap) {
        previousPage = pPage
        currentPage = cPage
        nextPage = nPage
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            val x = mScroller.currX
            val y = mScroller.currY
            scrollPageLeft = -(viewWidth - x).toFloat()
            if (mScroller.finalX == x && mScroller.finalY == y) {
                resetView()
            }
            postInvalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = measureSize(defaultHeight, heightMeasureSpec)
        val width = measureSize(defaultHeight, widthMeasureSpec)
        setMeasuredDimension(width, height)
        viewHeight = height
        viewWidth = width
        mPageListener?.onMeasureListener(viewWidth)
    }

    private fun measureSize(defaultSize: Int, measureSpec: Int): Int {
        var result = defaultSize
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)
        result = specSize

        /*  if (specMode == View.MeasureSpec.EXACTLY) {
              result = specSize
          } else if (specMode == View.MeasureSpec.AT_MOST) {
              result = Math.min(result, specSize)
          }*/
        return result
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (touchPoint.x == -1f && touchPoint.y == -1f) {
            drawCurrentPage(canvas)
            pageState = PageState.PAGESTAY
        } else {
            if (touchStyle == TouchStyle.TOUCH_RIGHT) {
                drawCurrentPage(canvas)
                drawPreviousPage(canvas)
                drawShadow(canvas)
            } else if (touchStyle == TouchStyle.TOUCH_LEFT) {
                drawNextPage(canvas)
                drawCurrentPage(canvas)
                drawShadow(canvas)
            }
        }
        canvas.save()
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0f, 0f, null)
        }
        canvas.restore()
    }

    private fun drawPreviousPage(canvas: Canvas) {
        canvas.drawBitmap(previousPage, scrollPageLeft, 0f, null)

    }

    private fun drawCurrentPage(canvas: Canvas) {
        if (touchStyle == TouchStyle.TOUCH_RIGHT) {
            canvas.drawBitmap(currentPage, 0f, 0f, null)
        } else if (touchStyle == TouchStyle.TOUCH_LEFT) {
            canvas.drawBitmap(currentPage, scrollPageLeft, 0f, null)
        }
    }

    private fun drawNextPage(canvas: Canvas) {
        canvas.drawBitmap(nextPage, 0f, 0f, null)

    }

    private fun drawShadow(canvas: Canvas) {
        val left = (viewWidth + scrollPageLeft).toInt()
        shadowDrawable.setBounds(left, 0, left + 30, viewHeight)
        shadowDrawable.draw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        val x = event.x
        val y = event.y
        if (pageState == PageState.PAGEMOVE) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    xDown = x
                    when {
                        x < viewWidth / 3 -> {
                            touchStyle = TouchStyle.TOUCH_LEFT
                            mPageListener?.onLeftScroll()
                        }
                        x > viewWidth * 2 / 3 -> {
                            touchStyle = TouchStyle.TOUCH_RIGHT
                            mPageListener?.onRightScroll()
                        }
                        else -> {
                            touchStyle = TouchStyle.TOUCH_MIDDLE
                            mPageListener?.onMiddleClick()
                        }
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    scrollPage(x, y)
                }
                MotionEvent.ACTION_UP -> {
                    autoScroll()
                }
            }
        }
        return true
    }

    private fun scrollPage(x: Float, y: Float) {
        touchPoint.x = x
        touchPoint.y = y
        if (touchStyle == TouchStyle.TOUCH_RIGHT) {
            scrollPageLeft = touchPoint.x - xDown
        } else if (touchStyle == TouchStyle.TOUCH_LEFT) {
            scrollPageLeft = touchPoint.x - xDown - viewWidth
        }
        if (scrollPageLeft > 0) {
            scrollPageLeft = 0f
        }
        postInvalidate()
    }

    private fun autoScroll() {
        if (touchStyle == TouchStyle.TOUCH_RIGHT) {
            autoScrollToNextPage()

        } else if (touchStyle == TouchStyle.TOUCH_LEFT) {
            autoScrollToPreviousPage()
        }
    }

    private fun autoScrollToNextPage() {
        pageState = PageState.PAGEMOVE
        val dx = (-(viewWidth + scrollPageLeft)).toInt()
        val dy = (touchPoint.y).toInt()
        val time = ((1 + scrollPageLeft / viewWidth) * scrollTime).toInt()
        mScroller.startScroll((viewWidth + scrollPageLeft).toInt(), touchPoint.y as Int, dx, dy, time)
    }

    private fun autoScrollToPreviousPage() {
        pageState = PageState.PAGEMOVE
        val dx = (-scrollPageLeft).toInt()
        val dy = (touchPoint.y).toInt()
        val time = (-scrollPageLeft / viewWidth * scrollTime).toInt()
        mScroller.startScroll((viewWidth + scrollPageLeft).toInt(), touchPoint.y as Int, dx, dy, time)
    }

    private fun resetView() {
        scrollPageLeft = 0f
        touchPoint.x = -1f
        touchPoint.y = -1f
    }

    fun setOnPageListener(listener: OnPageTouchListener) {
        mPageListener = listener
    }

    private inner class MyPoint(var x: Float, var y: Float)
    interface OnPageTouchListener {
        fun onLeftScroll()
        fun onMiddleClick()
        fun onRightScroll()
        fun onMeasureListener(width: Int)
        fun onFinalScroll()
        fun onPageListener(touchStyle: TouchStyle): Boolean
    }

    private enum class PageState {
        PAGESTAY, PAGEMOVE
    }

    enum class TouchStyle {
        TOUCH_LEFT, TOUCH_RIGHT, TOUCH_MIDDLE
    }

}