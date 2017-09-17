package com.aqrairsigns.aqrleilib.basemvp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.aqrairsigns.aqrleilib.util.ActivityCollector
import com.aqrairsigns.aqrleilib.util.AppLog
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrairsigns.aqrleilib.util.PermissionUtil

abstract class
BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeSetContentView()
        setContentView(layoutRes)
        initComponents(savedInstanceState)
        AppLog.logDebug(AppLog.LOG_TAG_ACTIVITY, "onCreate：" + this.javaClass.simpleName)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        AppLog.logDebug(AppLog.LOG_TAG_ACTIVITY, "onNewIntent：" + this.javaClass.simpleName)
    }

    override fun onRestart() {
        super.onRestart()
        AppLog.logDebug(AppLog.LOG_TAG_ACTIVITY, "onRestart：" + this.javaClass.simpleName)
    }

    override fun onStart() {
        super.onStart()
        AppLog.logDebug(AppLog.LOG_TAG_ACTIVITY, "onStart：" + this.javaClass.simpleName)
    }

    override fun onResume() {
        super.onResume()
        AppLog.logDebug(AppLog.LOG_TAG_ACTIVITY, "onResume：" + this.javaClass.simpleName)
    }

    override fun onPause() {
        super.onPause()
        AppLog.logDebug(AppLog.LOG_TAG_ACTIVITY, "onPause：" + this.javaClass.simpleName)
    }

    override fun onStop() {
        super.onStop()
        AppLog.logDebug(AppLog.LOG_TAG_ACTIVITY, "onStop：" + this.javaClass.simpleName)
    }

    override fun onDestroy() {
        super.onDestroy()
        AppLog.logDebug(AppLog.LOG_TAG_ACTIVITY, "onDestroy：" + this.javaClass.simpleName)
        ActivityCollector.remove(this)
    }

    protected abstract val layoutRes: Int
    protected open fun beforeSetContentView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!permissionCheck(this, Manifest.permission_group.STORAGE, 1)) {
                AppToast.toastShow(this, "Permission Denied !", 1000)
            }
        }
    }

    fun permissionCheck(context: Context, permission: String, mRequestCode: Int): Boolean {
        var result = PermissionUtil.selfPermissionGranted(context, permission)
        if (!result) {
            ActivityCompat.OnRequestPermissionsResultCallback { requestCode, _, grantResults ->
                if (requestCode == mRequestCode) {
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        result = true
                    }
                }
            }
        }
        return result
    }

    protected open fun initComponents(savedInstanceState: Bundle?) {
        ActivityCollector.add(this)
        AppLog.logDebug(AppLog.LOG_TAG_ACTIVITY, "当前的activity是：" + this.javaClass.simpleName)
    }

}
