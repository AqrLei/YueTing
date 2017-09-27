package com.aqrairsigns.aqrleilib.drawable

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.view.MotionEvent
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/9/16.
 */
class TestDrawable : Drawable() {

    private val mPaint: Paint = Paint()
    private var mRippleColor: Int = 0

    private var mRipplePointX: Float = 0F
    private var mRipplePointY: Float = 0F
    private var mRippleRadius: Float = 0F

    private var mAlpha: Int = 200
    private var mCircleAlpha: Int = 0
    private var mBgAlpha: Int = 0

    private var mCenterPointX: Float = 0F
    private var mCenterPointY: Float = 0F
    private var mClickPointX: Float = 0F
    private var mClickPointY: Float = 0F

    private var maxRadius: Float = 0F
    private var startRadius: Float = 0F
    private var endRadius: Float = 0F

    private var mUpDone: Boolean = false
    private var mEnterDone: Boolean = false

    private var mEnterProgress: Float = 0F
    private val mEnterIncrement: Float = 16F / 360
    private val mEnterInterpolator = DecelerateInterpolator(2F)

    private var mExitProgress: Float = 0F
    private val mExitIncrement: Float = 16F / 280
    private val mExitInterpolator = AccelerateInterpolator(2F)

    private val enterRunnable = Runnable {
        mEnterDone = false
        mCircleAlpha = 255
        mEnterProgress += mEnterIncrement
        if (mEnterProgress > 1) {
            //TODO onEnterProgress(1)
            //TODO enterDone()
            //TODO return
        }
        val interpolation = mEnterInterpolator.getInterpolation(mEnterProgress)
        //TODO onEnterProgress(interpolation)
        //TODO scheduleSelf(this, SystemClock.uptimeMillis() + 16)
    }
    private val exitRunnable = Runnable {
        if (!mEnterDone) {
            return@Runnable
        }
        mExitProgress += mExitIncrement
        if (mExitProgress > 1) {
            //TODO onExitPrograss(1)
            //TODO exitDone()
            //TODO return@Runnable
        }
    }

    init {
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        setRippleColor(0x60000000)
    }

    private fun startEnterRunnable() {
        mEnterProgress = 0F
        unscheduleSelf(exitRunnable)
        unscheduleSelf(enterRunnable)
        scheduleSelf(enterRunnable, SystemClock.uptimeMillis())
    }

    private fun onEnterProgress(realProgress: Float) {
        mRippleRadius = getCenter(startRadius, endRadius, realProgress)
        mRipplePointX = getCenter(mClickPointX, mCenterPointX, realProgress)
        mRipplePointY = getCenter(mClickPointY, mCenterPointY, realProgress)
        mBgAlpha = getCenter(0F, 182F, realProgress).toInt()
        invalidateSelf()
    }

    private fun enterDone() {
        mEnterDone = true
        if (mUpDone) {
            startExitRunnable()
        }
    }

    private fun startExitRunnable() {
        mExitProgress = 0F
        unscheduleSelf(enterRunnable)
        unscheduleSelf(exitRunnable)
        scheduleSelf(exitRunnable, SystemClock.uptimeMillis())
    }

    private fun onExitProgress(realProgress: Float) {
        mBgAlpha = getCenter(182F, 0F, realProgress).toInt()
        mCircleAlpha = getCenter(255F, 0F, realProgress).toInt()
        invalidateSelf()
    }

    private fun exitDone() {
        mEnterDone = false
    }

    private fun getCenter(start: Float, end: Float, progress: Float) =
            start + (end - start) * progress

    private fun getCircleAlpha(preAlpha: Int, bgAlpha: Int): Int {
        val dAlpha = preAlpha - bgAlpha
        return ((dAlpha * 255F) / (255F - bgAlpha)).toInt()
    }

    fun setRippleColor(rippleColor: Int) {
        mRippleColor = rippleColor
        onColorOrAlphaChange()
    }

    private fun onColorOrAlphaChange() {
        mPaint.color = mRippleColor
        if (mAlpha != 255) {
            val pAlpha = mPaint.alpha
            val realAlpha = (pAlpha * (mAlpha / 255F))
            mPaint.alpha = realAlpha.toInt()
        }
        invalidateSelf()
    }

    fun onTouch(event: MotionEvent) {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mClickPointX = event.x
                mClickPointY = event.y
                onTouchDown(mClickPointX, mClickPointY)
            }
            MotionEvent.ACTION_UP -> {
                onTouchUp()
            }
        }
    }

    private fun onTouchDown(x: Float, y: Float) {
        mUpDone = false
        mRipplePointX = x
        mRipplePointY = y
        mRippleRadius = 0F
        startEnterRunnable()
    }

    private fun onTouchUp() {
        mUpDone = true
        if (mEnterDone) {
            startExitRunnable()
        }
    }

    override fun draw(canvas: Canvas?) {
        val preAlpha = mPaint.alpha
        val bgAlpha = preAlpha * (mBgAlpha / 255f).toInt()
        val maxCircleAlpha = getCircleAlpha(preAlpha, bgAlpha)
        val circleAlpha = (maxCircleAlpha * (mCircleAlpha / 255F)).toInt()

        mPaint.alpha = bgAlpha
        canvas?.drawColor(mPaint.color)

        mPaint.alpha = circleAlpha
        canvas?.drawCircle(mRipplePointX, mRipplePointY, mRippleRadius, mPaint)

        mPaint.alpha = preAlpha


    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        if (bounds != null) {
            mCenterPointX = bounds.centerX().toFloat()
            mCenterPointY = bounds.centerY().toFloat()
            maxRadius = Math.max(mCenterPointX, mCenterPointY)
            startRadius = maxRadius * 0.1F
            endRadius = maxRadius * 0.8F
        }

    }

    override fun getAlpha() = mAlpha

    override fun setAlpha(alpha: Int) {
        mAlpha = alpha
        onColorOrAlphaChange()
    }

    override fun getOpacity() = when (mAlpha) {
        255 -> {
            PixelFormat.OPAQUE
        }
        0 -> {
            PixelFormat.TRANSPARENT
        }
        else -> {
            PixelFormat.TRANSLUCENT
        }
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        if (mPaint.colorFilter != colorFilter) {
            mPaint.colorFilter = colorFilter
            invalidateSelf()
        }
    }
}