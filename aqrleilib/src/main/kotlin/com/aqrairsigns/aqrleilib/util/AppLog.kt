package com.aqrairsigns.aqrleilib.util

import android.util.Log

/**
 * @Author: AqrLei
 *@Name MyLearning
 *@Description:
 *@Date: 2017/8/22
 */
object AppLog {
    val LOG_TAG_ACTIVITY: String = "activity"
    val LOG_TAG_FRAGMENT: String = "fragment"
    val LOG_TAG_PRESENTER: String = "presenter"
    val LOG_TAG_APPLICATION: String = "application"
    val LOG_TAG_SERVICE: String = "service"
    fun logDebug(tag: String, msg: String) {
        Log.d(tag, msg)
    }
}