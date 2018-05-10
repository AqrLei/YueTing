package com.aqrlei.graduation.yueting.ui.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.aqrlei.graduation.yueting.R
import java.util.*
import java.util.concurrent.Semaphore

/**
 * @author  aqrLei on 2018/4/25
 */
@Suppress("unused")
class PathView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : SurfaceView(context, attrs, defStyleAttr, defStyleRes),
        SurfaceHolder.Callback, Runnable {


    companion object {
        private const val DEFAULT_LINE_WIDTH = 5F
    }

    /**
     * if mode is TRAIN , should call setMode(PathMode)
     * */
    enum class PathMode {
        AIRPLANE, TRAIN
    }

    interface OnPathAnimationCallBack {
        fun callBack(id: Int)
    }

    internal class Keyframes(path: Path) {
        companion object {
            const val PRECISION = 1F
        }

        private var numPoints: Int = 0
        private lateinit var data: FloatArray

        init {
            init(path)
        }

        private fun init(path: Path) {
            val pathMeasure = PathMeasure(path, false)
            val pathLength = pathMeasure.length
            numPoints = (pathLength / PRECISION).toInt() + 1
            data = FloatArray(numPoints * 2)
            val position = FloatArray(2)
            var index = 0
            for (i in 0 until numPoints) {
                val distance = i * pathLength / (numPoints - 1)
                pathMeasure.getPosTan(distance, position, null)
                data[index] = position[0]
                data[index + 1] = position[1]
                index += 2
            }
            numPoints = data.size
        }

        /**
         * 拿到start和end之间的x,y数据
         *
         * @param start 开始百分比
         * @param end   结束百分比
         * @return 裁剪后的数据
         */
        internal fun getRangeValue(start: Float, end: Float): FloatArray? {
            var startIndex = (numPoints * start).toInt()
            var endIndex = (numPoints * end).toInt()

            //必须是偶数，因为需要float[]{x,y}这样x和y要配对的
            if (startIndex % 2 != 0) {
                //直接减，不用担心 < 0  因为0是偶数，哈哈
                --startIndex
            }
            if (endIndex % 2 != 0) {
                //不用检查越界
                ++endIndex
            }
            //根据起止点裁剪
            return if (startIndex > endIndex) Arrays.copyOfRange(data, endIndex, startIndex) else null
        }
    }

    /**
     * java并发编程 变量关键字
     */
    @Volatile
    private var isDrawing: Boolean = false
    private val surfaceHolder: SurfaceHolder
    private val paint: Paint
    private var mode: PathMode = PathMode.AIRPLANE
    /**
     *Semaphore 控制线程并发的数量
     * 此处为单线程
     * */
    private val lightLineSemaphore: Semaphore
            by lazy { Semaphore(1) }
    private val darkLineSemaphore: Semaphore
            by lazy { Semaphore(1) }
    private var animationDuration: Long = 1000L
    private var progressAnimator: ValueAnimator? = null
    private var alphaAnimator: ValueAnimator? = null
    private var lightPoints: FloatArray? = null
    private var darkPoints: FloatArray? = null
    private var keyFrames: Keyframes? = null
    private var lightLineColor: Int = Color.parseColor("#F17F94")
    private var darkLineColor: Int = Color.parseColor("#D8D5D7")
    private var alpha: Int = 0
    private var callBack: OnPathAnimationCallBack? = null

    init {
        setZOrderOnTop(true)
        surfaceHolder = holder
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT)
        surfaceHolder.addCallback(this)
        paint = Paint().apply {
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        context.obtainStyledAttributes(attrs, R.styleable.PathView)?.run {
            setAnimationDuration(getInteger(
                    R.styleable.PathView_animationDuration,
                    1000).toLong())
            setLightLineColor(getColor(R.styleable.PathView_lightLineColor, lightLineColor))
            setDarkLineColor(getColor(R.styleable.PathView_darkLineColor, darkLineColor))
            setLineWidth(getDimension(R.styleable.PathView_lineWidth, DEFAULT_LINE_WIDTH))
            recycle()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder?) {}

    override fun surfaceCreated(holder: SurfaceHolder?) {
        restart()
    }

    override fun run() {
        while (isDrawing) {
            surfaceHolder.lockCanvas()?.let {
                /**
                 * 画布颜色和合成模式
                 * */
                it.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                startDraw(it)
                surfaceHolder.unlockCanvasAndPost(it)
            } ?: return
        }
    }

    fun prepare(path: Path,
                width: Float = 5F,
                mode: PathMode = PathMode.AIRPLANE,
                duration: Long = 1000L) {
        setPath(path)
        setLineWidth(width)
        setMode(mode)
        setAnimationDuration(duration)
    }

    fun setLightLineColor(color: Int) {
        lightLineColor = color
    }

    fun setDarkLineColor(color: Int) {
        darkLineColor = color
    }

    fun setCallBack(callBack: OnPathAnimationCallBack) {
        this.callBack = callBack

    }

    fun startAnimation() {
        alphaAnimator?.run { if (isRunning) cancel() }
        progressAnimator?.run { if (isRunning) cancel() }
        alphaAnimator = ValueAnimator.ofInt(0, 255)?.apply {
            duration = animationDuration / 10
            addUpdateListener { alpha = it.animatedValue as Int }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    startUpdateProgress()
                }
            })
            start()
        }
    }

    private fun setMode(mode: PathMode) {
        this.mode = mode
        if ((alphaAnimator?.isRunning == true) || (progressAnimator?.isRunning == true))
            throw IllegalStateException("animation has been started!")
        if (mode == PathMode.TRAIN) {
            setDarkLineProgress(1f, 0f)
        } else {
            setDarkLineProgress(0f, 0f)
        }
    }

    private fun setPath(path: Path) {
        keyFrames = Keyframes(path)
        alpha = 0
    }

    private fun setAnimationDuration(duration: Long) {
        animationDuration = duration
    }

    private fun setLineWidth(width: Float) {
        paint.strokeWidth = width
    }

    private fun startUpdateProgress() {
        alphaAnimator = null
        progressAnimator = ValueAnimator.ofFloat(-.6f, 1f)?.apply {
            duration = animationDuration
            addUpdateListener {
                it?.run {
                    val currentProgress = animatedValue as Float
                    var lightLineStartProgress: Float
                    var lightLineEndProgress: Float
                    var darkLineStartProgress: Float
                    var darkLineEndProgress: Float
                    darkLineEndProgress = currentProgress
                    darkLineStartProgress = (.6f + currentProgress) * 2
                    lightLineStartProgress = darkLineStartProgress
                    lightLineEndProgress = .35f + currentProgress
                    if (lightLineEndProgress > .3f) {
                        lightLineEndProgress = (.35f + currentProgress - .3f) * 2 + .3f
                    }
                    if (darkLineStartProgress > .65f) {
                        lightLineStartProgress = ((.6f + currentProgress) * 2 - .65f) * .35f + .65f
                        darkLineStartProgress = lightLineStartProgress

                    }
                    if (lightLineEndProgress < 0) {
                        lightLineEndProgress = 0f
                    }
                    if (darkLineEndProgress < 0) {
                        darkLineEndProgress = 0f
                    }
                    if (lightLineEndProgress > .9f) {
                        if (alphaAnimator == null) {
                            alphaAnimator = ValueAnimator.ofInt(255, 0)?.apply {
                                duration = (animationDuration * .2).toLong()// 时长是总时长的20%
                                addUpdateListener { alpha = it?.animatedValue as Int }
                                start()
                            }
                        }
                    }
                    if (lightLineStartProgress > 1) {
                        lightLineStartProgress = 1f
                        darkLineStartProgress = lightLineStartProgress
                    }
                    setLightLineProgress(lightLineStartProgress, lightLineEndProgress)
                    if (mode == PathMode.AIRPLANE)
                        setDarkLineProgress(darkLineStartProgress, darkLineEndProgress)
                }
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    callBack?.callBack(this@PathView.id)
                }
            })
            start()
        }
    }

    private fun setLightLineProgress(start: Float, end: Float) {
        setLineProgress(start, end, true)
    }

    private fun setDarkLineProgress(start: Float, end: Float) {
        setLineProgress(start, end, false)
    }

    private fun setLineProgress(start: Float, end: Float, isLight: Boolean) {
        keyFrames?.let {
            if (isLight) {
                try {
                    lightLineSemaphore.acquire()
                } catch (e: InterruptedException) {
                    return
                }
                lightPoints = it.getRangeValue(start, end)
                lightLineSemaphore.release()
            } else {
                try {
                    darkLineSemaphore.acquire()
                } catch (e: InterruptedException) {
                    return
                }
                darkPoints = it.getRangeValue(start, end)
                darkLineSemaphore.release()
            }
        } ?: throw IllegalStateException("path not set yet")
    }

    private fun restart() {
        isDrawing = true
        Thread(this).start()
    }

    private fun startDraw(canvas: Canvas) {
        try {
            /**
             * 从信号量获取一个允许机会
             */
            darkLineSemaphore.acquire()
        } catch (e: InterruptedException) {
            /**
             * 遇到阻塞异常，直接返回
             */
            return
        }
        darkPoints?.let {
            paint.color = darkLineColor
            paint.alpha = alpha
            canvas.drawPoints(darkPoints, paint)
        }
        /**
         * 操作执行完毕，释放对应的信号量
         * */
        darkLineSemaphore.release()

        try {
            lightLineSemaphore.acquire()
        } catch (e: InterruptedException) {
            return
        }
        lightPoints?.let {
            paint.color = lightLineColor
            paint.alpha = alpha
            canvas.drawPoints(lightPoints, paint)
        }
        lightLineSemaphore.release()
    }

    private fun stop() {
        isDrawing = false
        try {
            darkLineSemaphore.acquire()
        } catch (e: InterruptedException) {
            return
        }
        darkPoints = null
        darkLineSemaphore.release()

        try {
            lightLineSemaphore.acquire()
        } catch (e: InterruptedException) {
            return
        }
        lightPoints = null
        lightLineSemaphore.release()
        alphaAnimator?.apply {
            if (isRunning) cancel()
        }
        progressAnimator?.apply {
            if (isRunning) cancel()
        }
    }
}