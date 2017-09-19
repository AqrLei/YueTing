package com.aqrairsigns.aqrleilib.basemvp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.aqrairsigns.aqrleilib.util.AppLog

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/19 Time: 13:39
 */
open class BaseService : Service() {
    override fun onCreate() {
        AppLog.logDebug(AppLog.LOG_TAG_SERVICE, "--onCreate--")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        AppLog.logDebug(AppLog.LOG_TAG_SERVICE, "--onStartCommand--")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onRebind(intent: Intent?) {
        AppLog.logDebug(AppLog.LOG_TAG_SERVICE, "--onRebind--")
        super.onRebind(intent)
    }

    override fun onBind(p0: Intent?): IBinder? {
        AppLog.logDebug(AppLog.LOG_TAG_SERVICE, "--onBind--")
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        AppLog.logDebug(AppLog.LOG_TAG_SERVICE, "--onUnbind--")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        AppLog.logDebug(AppLog.LOG_TAG_SERVICE, "--onDestroy--")
        super.onDestroy()
    }
}