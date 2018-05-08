package com.aqrlei.graduation.yueting.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

/**
 * @Author: AqrLei
 * @CreateTime: Date: 2017/9/14 Time: 14:50
 */
object ImageUtil {
    fun byteArrayToBitmap(byte: ByteArray?, offset: Int = 0): Bitmap? =
            if (byte != null) {
                BitmapFactory.decodeByteArray(byte, offset, byte.size)
            } else null

    fun byteArrayToDrawable(byte: ByteArray?, offset: Int = 0): Drawable? =
            if (byte != null) {
                BitmapDrawable(null, BitmapFactory.decodeByteArray(byte, offset, byte.size))
            } else null
}