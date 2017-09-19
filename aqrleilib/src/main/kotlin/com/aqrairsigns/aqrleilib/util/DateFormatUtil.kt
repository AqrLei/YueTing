package com.aqrairsigns.aqrleilib.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/9/16.
 */
object DateFormatUtil {
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    fun simpleDateFormat(time: Long): String =
        dateFormatter.format(Date(time))

}