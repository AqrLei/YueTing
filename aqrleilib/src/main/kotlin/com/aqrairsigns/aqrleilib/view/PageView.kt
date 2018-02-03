package com.aqrairsigns.aqrleilib.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2018/2/3.
 */
class PageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) :
        AppCompatTextView(context, attrs, defStyleAttr) {
    private var mBitmap: Bitmap? = null
    private var mScrollListener: OnScrollListener? = null
    private var clickX: Int = 0
    private var currentX: Int = 0
    private val touchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop
    private var moved = false

    fun setBitmap(bitmap: Bitmap) {
        mBitmap = bitmap
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0f, 0f, null)
        }
        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> clickX = event.x.toInt()
            MotionEvent.ACTION_MOVE -> if (Math.abs(event.x - clickX) > touchSlop) {
                moved = true
            }
            MotionEvent.ACTION_UP -> {
                currentX = event.x.toInt()
                if (moved) {
                    if (clickX > currentX) {
                        mScrollListener?.onLeftScroll()
                    } else {
                        mScrollListener?.onRightScroll()
                    }
                }
                moved = false
            }
        }
        return super.onTouchEvent(event)
    }

    fun setOnScrollListener(listener: OnScrollListener) {
        mScrollListener = listener
    }

    interface OnScrollListener {
        fun onLeftScroll()
        fun onRightScroll()
    }

}