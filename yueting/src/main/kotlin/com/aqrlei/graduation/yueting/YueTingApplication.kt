package com.aqrlei.graduation.yueting

import com.aqrairsigns.aqrleilib.basemvp.BaseApplication
import com.aqrairsigns.aqrleilib.util.AppCache
import com.aqrairsigns.aqrleilib.util.DBManager
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/14 Time: 16:17
 */
class YueTingApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        /*AppSharedPreferences.init(this)
        AppSharedPreferences.setFileName("yueting")*/
        AppCache.init(this, "yueting")
        DBManager.initDBHelper(this, "yueting.db", 1)
        Fresco.initialize(this)
    }
}