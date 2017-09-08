package com.aqrairsigns.aqrleilib.util

import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/8 Time: 11:27
 */
abstract class BaseDialog(context: Context, resId: Int, themeResId: Int) {
    protected lateinit var mDialog: Dialog

    init {
        val v = LayoutInflater.from(context).inflate(resId, null)
        bindView(v)
        initView(context, themeResId, v)
    }

    protected abstract fun bindView(v: View)
    private fun initView(context: Context, themeResId: Int, v: View) {
        mDialog = Dialog(context, themeResId)
        mDialog.setContentView(v)
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metrics)
        v.layoutParams = FrameLayout.LayoutParams((metrics.widthPixels * 0.75).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun dismiss() {
        mDialog.dismiss()
    }

    fun show() {
        mDialog.show()
    }

}