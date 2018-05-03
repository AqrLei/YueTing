package com.aqrlei.graduation.yueting.ui.uiEt

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintLayout
import android.view.Gravity
import com.aqrlei.graduation.yueting.R

/**
 * created by AqrLei on 2018/4/27
 */
fun createPopView(context: Context, @LayoutRes layoutRes: Int, gravity: Int = Gravity.BOTTOM): Dialog {
    return Dialog(context, R.style.BottomDialog).apply {
        setContentView(layoutRes)
        window.setGravity(gravity)
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT)
        setCanceledOnTouchOutside(false)
    }
}

fun createProgressDialog(context: Context, title: String, msg: String): ProgressDialog {
    return ProgressDialog(context).apply {
        setProgressStyle(ProgressDialog.STYLE_SPINNER)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        setTitle(title)
        setMessage(msg)
    }
}