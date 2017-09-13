package com.aqrairsigns.aqrleilib.basemvp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.aqrairsigns.aqrleilib.util.AppCache
import com.aqrairsigns.aqrleilib.util.AppLog
import com.aqrairsigns.aqrleilib.util.DBManager
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * @Author: AqrLei
 *@Name MyLearning
 *@Description:
 *@Date: 2017/8/22
 */
class BaseApplication : Application(), Application.ActivityLifecycleCallbacks {
    override fun onCreate() {
        super.onCreate()
        /*AppSharedPreferences.init(this)
        AppSharedPreferences.setFileName("yueting")*/
        AppCache.init(this, "yueting")
        DBManager.initDBHelper(this, "yueting.db", 1)
        Fresco.initialize(this)
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
}