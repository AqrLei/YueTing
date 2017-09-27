package com.aqrlei.graduation.yueting

import android.content.Intent
import android.os.StrictMode
import android.text.TextUtils
import com.aqrairsigns.aqrleilib.basemvp.BaseApplication
import com.aqrairsigns.aqrleilib.util.AppCache
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.service.MusicService
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/14 Time: 16:17
 */
class YueTingApplication : BaseApplication() {
    private var musicIntent: Intent? = null
    private var isSameProcess = false

    override fun onCreate() {

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll().penaltyLog().build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectAll().penaltyLog().build())
        }
        super.onCreate()
        /*AppSharedPreferences.init(this)
            AppSharedPreferences.setFileName("yueting")*/
        val processName = getProcessName(this)
        isSameProcess = !TextUtils.isEmpty(processName) && processName == this.packageName
        if (isSameProcess) {
            AppCache.init(this, YueTingConstant.SF_NAME)
            DBManager
                    .initDBHelper(this, YueTingConstant.DB_NAME, 1)
                    .addTable(YueTingConstant.MUSIC_TABLE_NAME,
                            YueTingConstant.MUSIC_TABLE_C,
                            YueTingConstant.MUSIC_TABLE_C_TYPE
                    ).createDB()
            Fresco.initialize(this)
        }

    }

    fun getServiceIntent(): Intent? {
        if (musicIntent == null) {
            musicIntent = Intent(this, MusicService::class.java)
        }
        return musicIntent
    }

    override fun onTerminate() {
        if (isSameProcess) {
            DBManager.closeDB()
            ShareMusicInfo.MusicInfoTool.clear()
        }
        super.onTerminate()
    }
}