package com.aqrlei.graduation.yueting.baselib.util

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
    fun logDebug(tag: String, msg: String) {
        Log.d(tag, msg)
    }
}