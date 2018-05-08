package com.aqrlei.graduation.yueting.util

import android.util.Log

/**
 *@Author: AqrLei
 *@Date: 2017/8/22
 */
object AppLog {
    const val LOG_TAG_ACTIVITY: String = "activity"
    const val LOG_TAG_FRAGMENT: String = "fragment"
    const val LOG_TAG_PRESENTER: String = "presenter"
    const val LOG_TAG_APPLICATION: String = "application"
    const val LOG_TAG_SERVICE: String = "service"
    fun logDebug(tag: String, msg: String) {
        Log.d(tag, msg)
    }
}