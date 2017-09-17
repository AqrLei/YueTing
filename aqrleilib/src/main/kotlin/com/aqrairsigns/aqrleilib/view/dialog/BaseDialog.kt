package com.aqrairsigns.aqrleilib.view.dialog

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
/*
* @param context 上下文
* @param resId 布局Id
* @param themeResId 主题样式Id
* */
abstract class BaseDialog(context: Context, resId: Int, themeResId: Int) {
    private lateinit var mDialog: Dialog//Dialog

    init {
        val v = LayoutInflater.from(context).inflate(resId, null)
        bindView(v)
        initView(context, v, themeResId)
    }

    /*根据具体布局中的控件，在子类中实现控件绑定*/
    protected abstract fun bindView(v: View)

    /*根据布局和样式，进行Dialog的初始化并设置在屏幕中的位置*/
    private fun initView(context: Context, v: View, themeResId: Int) {
        mDialog = Dialog(context, themeResId)//创建Dialog并设置样式
        mDialog.setContentView(v)//设置Dialog的布局
        /*设置Dialog在屏幕中显示的位置*/
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