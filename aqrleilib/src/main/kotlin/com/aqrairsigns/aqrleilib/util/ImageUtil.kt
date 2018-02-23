package com.aqrairsigns.aqrleilib.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/14 Time: 14:50
 */
object ImageUtil {
    fun bitmapZoom(bitmap: Bitmap, newWidth: Float, newHeight: Float): Bitmap {
        val oldWidth = bitmap.width
        val oldHeight = bitmap.height
        val scaleWidth = (newWidth / oldWidth)
        val scaleHeight = (newHeight / oldHeight)
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, oldWidth, oldHeight, matrix, true)
    }
    fun byteArrayToBitmap(byte: ByteArray?, offset: Int = 0): Bitmap? =
            if (byte != null) {
                BitmapFactory.decodeByteArray(byte, offset, byte.size)
            } else null

    fun bitmapToDrawable(bitmap: Bitmap?, res: Resources? = null): Drawable? =
            if (bitmap != null) {
                BitmapDrawable(res, bitmap)
            } else null

    fun byteArrayToDrawable(byte: ByteArray?, offset: Int = 0): Drawable? =
            if (byte != null) {
                BitmapDrawable(null, BitmapFactory.decodeByteArray(byte, offset, byte.size))
            } else null
}