package com.aqrairsigns.aqrleilib.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Author : AqrLei
 * Date : 2017/9/16.
 */
object DateFormatUtil {
    private var dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    fun simpleDateFormat(time: Long): String =
        dateFormatter.format(Date(time))

    fun simpleTimeFormat(time: Long): String {
        dateFormatter.applyPattern("mm:ss")
        return dateFormatter.format(Date(time))
    }


}