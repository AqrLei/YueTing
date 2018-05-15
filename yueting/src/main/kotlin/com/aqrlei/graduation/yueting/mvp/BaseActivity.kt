package com.aqrlei.graduation.yueting.mvp

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.aqrlei.graduation.yueting.util.ActivityCollector

/**
 * @author  aqrLei on 2018/5/15
 */
abstract class BaseActivity : AppCompatActivity() ,BaseView{
    protected abstract val layoutRes: Int
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeSetContentView()
        setContentView(layoutRes)
        initComponents(savedInstanceState)
    }

    protected abstract fun beforeSetContentView()
    protected open fun initComponents(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = Color.WHITE
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        ActivityCollector.add(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.remove(this)
    }
}