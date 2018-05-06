package com.aqrlei.graduation.yueting.util

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build

/**
 * @author  aqrLei on 2018/5/4
 */
@TargetApi(Build.VERSION_CODES.O)
fun createNotificationChannel(context: Context, channelId: String, channelName: String, importance: Int) {
    (context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager).run {
        this.createNotificationChannel(NotificationChannel(channelId, channelName, importance))
    }
}
