package com.aqrlei.graduation.yueting.basemvp

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.aqrlei.graduation.yueting.util.AppLog


/**
 *@Author: AqrLei
 *@Date: 2017/8/22
 */
open class BaseApplication : Application(), Application.ActivityLifecycleCallbacks {
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityPaused(p0: Activity?) {
        AppLog.logDebug(AppLog.LOG_TAG_APPLICATION, "-----\t" + this.javaClass.simpleName + "-----\t:"
                + "Paused")
    }

    override fun onActivityResumed(p0: Activity?) {
        AppLog.logDebug(AppLog.LOG_TAG_APPLICATION, "-----\t" + this.javaClass.simpleName + "-----\t:"
                + "Resumed")
    }

    override fun onActivityStarted(p0: Activity?) {
        AppLog.logDebug(AppLog.LOG_TAG_APPLICATION, "-----\t" + this.javaClass.simpleName + "-----\t:"
                + "Started")
    }

    override fun onActivityDestroyed(p0: Activity?) {
        AppLog.logDebug(AppLog.LOG_TAG_APPLICATION, "-----\t" + this.javaClass.simpleName + "-----\t:"
                + "Destroy")
    }

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
        AppLog.logDebug(AppLog.LOG_TAG_APPLICATION, "-----\t" + this.javaClass.simpleName + "-----\t:"
                + "SaveInstanceState")
    }

    override fun onActivityStopped(p0: Activity?) {
        AppLog.logDebug(AppLog.LOG_TAG_APPLICATION, "-----\t" + this.javaClass.simpleName + "-----\t:"
                + "Stoped")
    }

    override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
        AppLog.logDebug(AppLog.LOG_TAG_APPLICATION, "-----\t" + this.javaClass.simpleName + "-----\t:"
                + "Created")
    }

    companion object {
        fun getProcessName(context: Context, pid: Int = android.os.Process.myPid()): String? {
            val activityManger = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val list = activityManger.runningAppProcesses
            list.forEach {
                try {
                    if (it.pid == pid) {
                        return it.processName
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return null
        }
    }
}