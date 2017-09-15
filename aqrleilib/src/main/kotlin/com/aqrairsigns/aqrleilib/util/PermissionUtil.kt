package com.aqrairsigns.aqrleilib.util

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/13 Time: 17:04
 */
@TargetApi(value = Build.VERSION_CODES.M)
object PermissionUtil {
    fun selfPermissionGranted(context: Context, permission: String): Boolean =
            try {
                context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
            } catch (e: Exception) {
                false
            }
}