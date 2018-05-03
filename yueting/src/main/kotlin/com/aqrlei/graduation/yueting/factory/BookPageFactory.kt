package com.aqrlei.graduation.yueting.factory

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.DisplayMetrics
import com.aqrairsigns.aqrleilib.ui.view.BookPageView
import com.aqrairsigns.aqrleilib.util.AppCache
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.DensityUtil
import com.aqrlei.graduation.yueting.constant.CacheConstant
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.BookInfo
import java.io.File
import java.io.RandomAccessFile
import java.io.UnsupportedEncodingException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.Charset

/**
 * Author : AqrLei
 * Date : 2017/11/9.
 */

enum class BookPageFactory {
    BOOKPAGEFACTORY;

    private var screenHeight: Int = 0
    private var screenWidth: Int = 0
    private var pageHeight: Int = 0
    private var pageWidth: Int = 0
    private var lineNumber: Int = 0
    private lateinit var mPaint: Paint
    private lateinit var mPaintC: Paint
    private lateinit var mContext: Context
    private var margin: Int = 0
    private var lineSpace: Int = 0
    private lateinit var mCanvasA: Canvas
    private lateinit var mCanvasB: Canvas
    private lateinit var mCanvasC: Canvas
    private lateinit var mView: BookPageView
    private var fileLength: Int = 0
    private var end: Int = 0
    private var begin: Int = 0
    private var fontSize = 45
    private lateinit var encoding: String
    private var mappedFile: MappedByteBuffer? = null
    private var randomFile: RandomAccessFile? = null
    private var refreshPage: Boolean = true
    private var bgColor: Int = Color.parseColor(YueTingConstant.READ_BACKGROUND_COLOR_DEFAULT)
    private var bPosition: Int = 0
    private lateinit var mBookInfo: BookInfo
    private var isNext = true
    private var progress: Int = 0
    private val content = ArrayList<String>()
    private val tempContent = ArrayList<String>()
    private val colorFilter = ColorMatrixColorFilter(floatArrayOf(
            1F, 0F, 0F, 0F, 0F,
            0F, 1F, 0F, 0F, 0F,
            0F, 0F, 1F, 0F, 0F,
            0F, 0F, 0F, 0.5F, 0F))


    fun getCurrentBegin() = begin
    fun getCurrentEnd() = end
    fun getBookByteArray(position: Int) = readParagraphForward(position)

    fun setBookInfo(view: BookPageView, bookInfo: BookInfo) {
        val metrics = DisplayMetrics()
        mView = view

        mContext = mView.context
        (mContext as Activity).windowManager.defaultDisplay.getMetrics(metrics)
        screenHeight = metrics.heightPixels
        screenWidth = metrics.widthPixels
        pageHeight = screenHeight - 2 * margin
        pageWidth = screenWidth - 2 * margin
        lineNumber = pageHeight / (fontSize + lineSpace) - 3

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintC = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintC.colorFilter = colorFilter
        mPaint.textSize = fontSize.toFloat()
        mPaintC.textSize = fontSize.toFloat()
        mPaint.color = Color.BLACK
        mPaintC.color = Color.BLACK
        mPaint.typeface = Typeface.DEFAULT
        mPaintC.typeface = Typeface.DEFAULT
        margin = DensityUtil.pxToDip(mContext, 5f)
        lineSpace = DensityUtil.pxToDip(mContext, 5F)

        val bitmapA = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888)
        val bitmapB = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888)
        val bitmapC = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888)
        mView.setBitmap(bitmapA, bitmapB, bitmapC)
        mCanvasA = Canvas(bitmapA)
        mCanvasB = Canvas(bitmapB)
        mCanvasC = Canvas(bitmapC)
        mCanvasA.drawColor(bgColor)
        mCanvasB.drawColor(bgColor)
        mCanvasC.drawColor(bgColor)
        mCanvasC.drawFilter
        openBook(bookInfo)
    }

    fun changeFontSize(dpSize: Float) {
        fontSize = DensityUtil.dipToPx(mContext, dpSize)
        mPaint.textSize = fontSize.toFloat()
        lineNumber = pageHeight / (fontSize + lineSpace) - 3
        refreshPage = false
        nextPage()
    }

    fun changeFontStyle(style: Int) {
        when (style) {
            0 -> {
                mPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                mPaintC.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            }
            1 -> {
                mPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
                mPaintC.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            }
            2 -> {
                mPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                mPaintC.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            3 -> {
                mPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC)
                mPaintC.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC)
            }
        }
        refreshPage = false
        nextPage()
    }

    fun nextPage(isProgress: Int = 0, pBegin: Int = 0) {//进度条或目录跳转控制参数：isProgress:标志；pBegin:起始位
        isNext = true
        progress = isProgress
        if (isProgress == 1) {
            refreshPage = false
            begin = pBegin
        }
        if (refreshPage) {
            begin = end
            if (end >= fileLength) {
                return
            } else {
                content.clear()
                pageDown()
            }
        } else {
            end = begin
            if (end >= fileLength) {
                return
            } else {
                content.clear()
                pageDown()
            }
            refreshPage = true
        }
        printPage()

    }

    fun prePage() {
        isNext = false
        end = begin
        if (begin <= 0) {
            return
        } else {
            content.clear()
            pageUp()
            end = begin
            pageDown()
        }
        printPage()

    }

    fun setMCanvasAContent() {
        var y = margin
        if (isNext) {
            mCanvasA.drawColor(bgColor)
            for (line in content) {
                y += fontSize + lineSpace
                mCanvasA.drawText(line, margin.toFloat(), y.toFloat(), mPaint)
            }
            mView.postInvalidate()
        }
    }

    fun setPageBackground(color: Int, position: Int) {
        bPosition = position
        bgColor = color
        mView.setBgColor(bgColor)
        refreshPage = false
        putCache()
        nextPage()


    }

    private fun openBook(bookInfo: BookInfo) {
        mBookInfo = bookInfo
        encoding = bookInfo.encoding

        val file = File(bookInfo.path)
        getCache()
        if (begin != end) {
            refreshPage = false
        }
        fileLength = bookInfo.fileLength
        try {
            randomFile = RandomAccessFile(file, "r")
            mappedFile = randomFile!!.channel.map(FileChannel.MapMode.READ_ONLY, 0, fileLength.toLong())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun pageDown() {
        var strParagraph = ""
        while (content.size < lineNumber && end < fileLength) {
            val byteTemp = readParagraphForward(end)
            end += byteTemp.size
            try {
                strParagraph = String(byteTemp, Charset.forName(encoding))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            strParagraph = strParagraph.replace("\r\n".toRegex(), "  ")
            strParagraph = strParagraph.replace("\n".toRegex(), " ")
            while (strParagraph.isNotEmpty()) {
                val size = mPaint.breakText(strParagraph, true, pageWidth.toFloat(), null)
                content.add(strParagraph.substring(0, size))
                strParagraph = strParagraph.substring(size)
                if (content.size >= lineNumber) {
                    break
                }
            }
            if (strParagraph.isNotEmpty()) {
                try {
                    end -= strParagraph.toByteArray(charset(encoding)).size
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun readParagraphForward(end: Int): ByteArray {
        var b0: Byte
        var before = 0
        var i = end
        while (i < fileLength) {
            b0 = mappedFile!!.get(i)
            if (encoding == YueTingConstant.ENCODING) {
                if (b0.toInt() == 0 && before == 10) {
                    break
                }
            } else {
                if (b0.toInt() == 10) {
                    break
                }
            }
            before = b0.toInt()
            i++
        }
        i = Math.min(fileLength - 1, i)
        val nParaSize = i - end + 1
        val buf = ByteArray(nParaSize)
        i = 0
        while (i < nParaSize) {
            buf[i] = mappedFile!!.get(end + i)
            i++
        }
        return buf
    }

    private fun pageUp() {
        var strParagraph = ""
        val tempList = ArrayList<String>()
        while (tempList.size < lineNumber && begin > 0) {
            val byteTemp = readParagraphBack(begin)
            begin -= byteTemp.size
            try {
                strParagraph = String(byteTemp, Charset.forName(encoding))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

            strParagraph = strParagraph.replace("\r\n".toRegex(), "  ")
            strParagraph = strParagraph.replace("\n".toRegex(), "  ")
            while (strParagraph.isNotEmpty()) {
                val size = mPaint.breakText(strParagraph, true, pageWidth.toFloat(), null)
                tempList.add(strParagraph.substring(0, size))
                strParagraph = strParagraph.substring(size)
                if (tempList.size >= lineNumber) {
                    break
                }
            }
            if (strParagraph.isNotEmpty()) {
                try {
                    begin += strParagraph.toByteArray(charset(encoding)).size
                } catch (u: UnsupportedEncodingException) {
                    u.printStackTrace()
                }

            }
        }

    }

    private fun readParagraphBack(begin: Int): ByteArray {
        var b0: Byte
        var before: Byte = 1
        var i = begin - 1
        while (i > 0) {
            b0 = mappedFile!!.get(i)
            if (encoding == YueTingConstant.ENCODING) {
                if (b0.toInt() == 10 && before.toInt() == 0 && i != begin - 2) {
                    i += 2
                    break
                }
            } else {
                if (b0.toInt() == 0X0a && i != begin - 1) {
                    i++
                    break
                }
            }
            i--
            before = b0
        }
        val nParaSize = begin - i
        val buf = ByteArray(nParaSize)
        for (j in 0 until nParaSize) {
            buf[j] = mappedFile!!.get(i + j)
        }
        return buf
    }

    private fun printPage() {
        var y = margin
        mCanvasA.drawColor(bgColor)
        mCanvasB.drawColor(bgColor)
        mCanvasC.drawColor(bgColor)
        content.forEachIndexed { index, s ->
            y += fontSize + lineSpace
            if (progress == 1) {
                mView.setDefaultPath()
                mCanvasA.drawText(s, margin.toFloat(), y.toFloat(), mPaint)
            } else {
                if (isNext) {
                    mCanvasB.drawText(s, margin.toFloat(), y.toFloat(), mPaint)
                    if (tempContent.isNotEmpty() && (index < tempContent.size)) {
                        mCanvasA.drawText(tempContent[index], margin.toFloat(), y.toFloat(), mPaint)
                        mCanvasC.drawText(tempContent[index], margin.toFloat(), y.toFloat(), mPaintC)
                    }
                } else {
                    mCanvasA.drawText(s, margin.toFloat(), y.toFloat(), mPaint)
                    mCanvasC.drawText(s, margin.toFloat(), y.toFloat(), mPaintC)
                    if (tempContent.isNotEmpty() && (index < tempContent.size)) {
                        mCanvasB.drawText(tempContent[index], margin.toFloat(), y.toFloat(), mPaint)
                    }
                }
            }

        }

        putCache()
        mView.postInvalidate()
        tempContent.clear()
        tempContent.addAll(content)

    }

    private fun putCache() {
        AppCache.APPCACHE.putInt(CacheConstant.READ_CHECK_KEY, bPosition)
        AppCache.APPCACHE.commit()
    }

    private fun getCache() {
        bPosition = AppCache.APPCACHE.getInt(CacheConstant.READ_CHECK_KEY, CacheConstant.READ_CHECK_DEFAULT)
        getIndexFromDB()
    }

    private fun getIndexFromDB() {

        val c = DBManager.sqlData(DBManager.SqlFormat.selectSqlFormat(DataConstant.BOOK_TABLE_NAME,
                "${DataConstant.BOOK_TABLE_C2_INDEX_BEGIN}, ${DataConstant.BOOK_TABLE_C3_INDEX_END}",
                DataConstant.COMMON_COLUMN_PATH,
                "="),
                null, arrayOf(mBookInfo.path), DBManager.SqlType.SELECT)
                .getCursor()
        while (c?.moveToNext() == true) {
            begin = c.getInt(c.getColumnIndex(DataConstant.BOOK_TABLE_C2_INDEX_BEGIN))
            end = c.getInt(c.getColumnIndex(DataConstant.BOOK_TABLE_C3_INDEX_END))
        }

    }

}
