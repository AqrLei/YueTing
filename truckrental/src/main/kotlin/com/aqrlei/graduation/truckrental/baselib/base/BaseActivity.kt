package com.aqrlei.graduation.truckrental.baselib.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.aqrlei.graduation.truckrental.baselib.util.ActivityCollector
import com.aqrlei.graduation.truckrental.baselib.util.AppLog

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.remove(this)
    }

    protected abstract val layoutRes: Int
    protected open fun beforeSetContentView() {

    }

    protected open fun initComponents(savedInstanceState: Bundle?) {
        ActivityCollector.add(this)
        AppLog.logDebug(AppLog.LOG_TAG_ACTIVITY, "当前的activity是：" + this.javaClass.simpleName)
    }

}
