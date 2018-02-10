package com.aqrlei.graduation.yueting.factory

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.DisplayMetrics
import com.aqrairsigns.aqrleilib.util.AppCache
import com.aqrairsigns.aqrleilib.util.AppLog
import com.aqrairsigns.aqrleilib.util.DensityUtil
import com.aqrairsigns.aqrleilib.view.PageView
import com.aqrlei.graduation.yueting.model.local.BookInfo
import java.io.File
import java.io.RandomAccessFile
import java.io.UnsupportedEncodingException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.util.*

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/11/9.
 */

enum class PageFactory {
    PAGEFACTORY;

    private var screenHeight: Int = 0
    private var screenWidth: Int = 0
    private var pageHeight: Int = 0
    private var pageWidth: Int = 0
    private var lineNumber: Int = 0
    private lateinit var mPaint: Paint
    private lateinit var mContext: Context
    private var margin: Int = 0
    private var lineSpace: Int = 0
    private lateinit var mCanvas: Canvas
    private lateinit var mView: PageView
    private var fileLength: Int = 0
    private var end: Int = 0
    private var begin: Int = 0
    private var fontSize = 45
    private var fontStyle = 0
    private lateinit var encoding: String
    private var mappedFile: MappedByteBuffer? = null
    private var randomFile: RandomAccessFile? = null
    private var refreshPage: Boolean = true
    private var bgColor: Int = Color.parseColor("#c7eece")
    private var bPosition: Int = 0

    private val content = ArrayList<String>()

    fun setBookInfo(view: PageView, bookInfo: BookInfo) {
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
        mPaint.textSize = fontSize.toFloat()
        mPaint.color = Color.BLACK
        mPaint.typeface = Typeface.DEFAULT
        margin = DensityUtil.pxToDip(mContext, 5f)
        lineSpace = DensityUtil.pxToDip(mContext, 5F)

        val bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888)
        mView.setBitmap(bitmap)
        mCanvas = Canvas(bitmap)
        mCanvas.drawColor(bgColor)
        content.add(bookInfo.name)
        content.add(bookInfo.path)
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
            }
            1 -> {
                mPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            }
            2 -> {
                mPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            3 -> {
                mPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC)
            }
        }
        refreshPage = false
        nextPage()
    }

    fun getCurrentBegin() = begin
    fun getBookByteArray(position: Int) = readParagraphForward(position)


    fun nextPage(isProgress: Int = 0, pBegin: Int = 0) {//进度条或目录跳转控制参数：isProgress:标志；pBegin:起始位
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


    private fun openBook(bookInfo: BookInfo) {
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
            AppLog.logDebug("readTest", "打开失败")
        }

        // Log.d("testApp", bookInfo.getName());
    }


    private fun pageDown() {
        var strParagraph = ""
        //int i = 0;
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
                //i++;
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
        /* Log.d("testHeight", "Height: " + pageHeight + "I: " + i + "LineNumber: " + lineNumber +
                "Height: " + (lineNumber * (fontSize + lineSpace)) +
                "Margin: " + margin+"Size:" +content.size());*/

    }

    private fun readParagraphForward(end: Int): ByteArray {
        var b0: Byte
        var before = 0
        var i = end
        while (i < fileLength) {
            b0 = mappedFile!!.get(i)
            if (encoding == "UTF-16LE") {
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

    fun prePage() {
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

    fun setPageBackground(color: Int, position: Int) {
        bPosition = position
        bgColor = color
        // mCanvas.drawColor(bgColor)
        //mView.invalidate()
        refreshPage = false
        nextPage()


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
            if (encoding == "UTF-16LE") {
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
        mCanvas.drawColor(bgColor)
        for (line in content) {
            y += fontSize + lineSpace
            mCanvas.drawText(line, margin.toFloat(), y.toFloat(), mPaint)
            // Log.d("text",line);
        }
        putCache()
        mView.invalidate()
    }

    private fun putCache() {
        AppCache.APPCACHE.putInt("bPosition", bPosition)
        AppCache.APPCACHE.putInt("cBegin", begin)
        AppCache.APPCACHE.putInt("cEnd", end)
        AppCache.APPCACHE.commit()
    }

    private fun getCache() {
        bPosition = AppCache.APPCACHE.getInt("bPosition", 0)
        begin = AppCache.APPCACHE.getInt("cBegin", 0)
        end = AppCache.APPCACHE.getInt("cEnd", 0)
    }

}
