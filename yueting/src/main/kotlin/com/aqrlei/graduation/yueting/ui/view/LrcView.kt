package com.aqrlei.graduation.yueting.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.model.LrcInfo

/**
 * created by AqrLei at 9:40 on 星期日, 五月 06, 2018
 */
@Suppress("unused")
class LrcView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0) : TextView(context, attrs, defStyleAttr, defStyleRes) {
    companion object {
        val CURRENT_COLOR = Color.argb(210, 251, 248, 29)
        val NOT_CURRENT_COLOR = Color.argb(140, 255, 255, 255)
        const val CURRENT_TEXT_SIZE = 24F
        const val NOT_CURRENT_TEXT_SIZE = 18F
    }

    private var width: Float = 0f
    private var height: Float = 0f
    private var currentColor: Int = 0
    private var noCurrentColor: Int = 0
    private var currentTextSize: Float = 0f
    private var noCurrentTextSize: Float = 0f
    private var index = 0
    private val currentPaint: Paint
            by lazy {
                Paint()
            }
    private val notCurrentPaint: Paint
            by lazy {
                Paint()
            }
    private val lrcList: ArrayList<LrcInfo>
            by lazy {
                ArrayList<LrcInfo>()
            }

    init {
        isFocusable = true
        currentPaint.setLrcPaint()
        notCurrentPaint.setLrcPaint()
        context.obtainStyledAttributes(attrs, R.styleable.LrcView)?.let {
            currentColor = it.getColor(R.styleable.LrcView_currentColor, CURRENT_COLOR)
            noCurrentColor = it.getColor(R.styleable.LrcView_noCurrentColor, NOT_CURRENT_COLOR)
            currentTextSize = it.getDimension(R.styleable.LrcView_currentTextSize, CURRENT_TEXT_SIZE)
            noCurrentTextSize = it.getDimension(R.styleable.LrcView_noCurrentTextSize, NOT_CURRENT_TEXT_SIZE)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            currentPaint.color =currentColor
            notCurrentPaint.color = noCurrentColor

            currentPaint.textSize = currentTextSize
            currentPaint.typeface = Typeface.SERIF

            notCurrentPaint.textSize = noCurrentTextSize
            notCurrentPaint.typeface = Typeface.DEFAULT
            val textHeight = currentTextSize + 1f
            try {
                if(lrcList.isEmpty()){
                    text = "...木有歌词文件，赶紧去下载..."
                    gravity = Gravity.CENTER
                }else {
                    text = ""
                    it.drawText(lrcList[index].lrcContent, width / 2, height / 2, currentPaint)
                    var tempY = height / 2

                    //画出本句之前的句子

                    for (i in index - 1 downTo 0) {//向上推移
                        tempY -= textHeight
                        it.drawText(lrcList[i].lrcContent, width / 2, tempY, notCurrentPaint)
                    }

                    tempY = height / 2

                    //画出本句之后的句子
                    for (i in index + 1 until lrcList.size) {//往下推移
                        tempY += textHeight
                        it.drawText(lrcList[i].lrcContent, width / 2, tempY, notCurrentPaint)
                    }
                }
            } catch (e: Exception) {
                text = "...木有歌词文件，赶紧去下载..."
                gravity = Gravity.CENTER
            }
        } ?: return
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.width = w.toFloat()
        this.height = h.toFloat()
    }

    fun setIndex(index: Int) {
        this.index = index
    }

    fun setLrcList(data: ArrayList<LrcInfo>) {
        lrcList.clear()
        lrcList.addAll(data)
        invalidate()
    }

    private fun Paint.setLrcPaint() {
        this.apply {
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
    }
}