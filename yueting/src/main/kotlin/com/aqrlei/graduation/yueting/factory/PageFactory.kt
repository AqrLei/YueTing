package com.aqrlei.graduation.yueting.factory

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.DisplayMetrics
import android.util.Log

import com.aqrairsigns.aqrleilib.util.DensityUtil
import com.aqrairsigns.aqrleilib.view.PageView
import com.aqrlei.graduation.yueting.model.local.BookMessage

import java.io.File
import java.io.RandomAccessFile
import java.io.UnsupportedEncodingException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.util.ArrayList

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/11/9.
 */

class PageFactory(private val mView: PageView, bookMessage: BookMessage) {
    private val screenHeight: Int
    private val screenWidth: Int
    private val pageHeight: Int
    private val pageWidth: Int
    private val lineNumber: Int
    private val mPaint: Paint
    private val mContext: Context
    private var margin: Int = 0
    private var lineSpace: Int = 0
    private val mCanvas: Canvas
    private var encoding: String = ""
    private var fileLength: Int = 0
    private var end: Int = 0
    private var begin: Int = 0
    private val fontSize = 45
    private var mappedFile: MappedByteBuffer? = null
    private var randomFile: RandomAccessFile? = null

    private val content = ArrayList<String>()

    init {

        val metrics = DisplayMetrics()
        mContext = mView.context
        (mContext as Activity).windowManager.defaultDisplay.getMetrics(metrics)
        screenHeight = metrics.heightPixels
        screenWidth = metrics.widthPixels
        pageHeight = screenHeight - 2 * margin
        pageWidth = screenWidth - 2 * margin
        lineNumber = pageHeight / (fontSize + lineSpace) - 1

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.textSize = fontSize.toFloat()
        mPaint.color = Color.BLACK
        margin = DensityUtil.pxToDip(mContext, 5f)
        lineSpace = DensityUtil.pxToDip(mContext, 5F)

        val bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888)
        mView.setBitmap(bitmap)
        mCanvas = Canvas(bitmap)
        mCanvas.drawColor(Color.YELLOW)
        content.add(bookMessage.name)
        content.add(bookMessage.path)
        openBook(bookMessage)


    }

    private fun openBook(bookMessage: BookMessage) {
        encoding = bookMessage.encoding
        val file = File(bookMessage.path)
        begin = 0
        end = 0
        fileLength = file.length().toInt()
        try {
            randomFile = RandomAccessFile(file, "r")
            mappedFile = randomFile!!.channel.map(FileChannel.MapMode.READ_ONLY, 0, fileLength.toLong())
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("testApp", "打开失败")
        }

        // Log.d("testApp", bookMessage.getName());
    }

    fun nextPage() {
        begin = end
        if (end >= fileLength) {
            return
        } else {
            content.clear()
            pageDown()
        }

        printPage()

    }

    private fun pageDown() {
        var strParagraph = ""
        //int i = 0;
        while (content.size < lineNumber && end < fileLength) {
            val byteTemp = readParagraphForward(end)
            end += byteTemp.size
            try {
                strParagraph = String(byteTemp, encoding as Charset)
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

    private fun pageUp() {
        var strParagraph = ""
        val tempList = ArrayList<String>()
        while (tempList.size < lineNumber && begin > 0) {
            val byteTemp = readParagraphBack(begin)
            begin -= byteTemp.size
            try {
                strParagraph = String(byteTemp, encoding as Charset)
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
        mCanvas.drawColor(Color.YELLOW)
        for (line in content) {
            y += fontSize + lineSpace
            mCanvas.drawText(line, margin.toFloat(), y.toFloat(), mPaint)
            // Log.d("text",line);
        }
        mView.invalidate()
    }


}
