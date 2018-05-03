package com.aqrairsigns.aqrleilib.basemvp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.aqrairsigns.aqrleilib.util.ActivityCollector
import com.aqrairsigns.aqrleilib.util.AppLog


abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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


    protected open fun initComponents(savedInstanceState: Bundle?) {
        ActivityCollector.add(this)
        AppLog.logDebug(AppLog.LOG_TAG_ACTIVITY, "当前的activity是：" + this.javaClass.simpleName)
    }

}
