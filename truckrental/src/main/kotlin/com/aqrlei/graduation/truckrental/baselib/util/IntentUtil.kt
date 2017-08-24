package com.aqrlei.graduation.truckrental.baselib.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/24
 */
object IntentUtil {
    /*
    * 查询是否有Activity可以响应Intent的跳转
    * @param context 上下文
    * @param intent 需要查询的Intent
    * @return 返回Boolean值结果
    * */
    fun queryActivities(context: Context, intent: Intent): Boolean {
        val list = context.packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY)
        return list != null && list.size > 0
    }
}