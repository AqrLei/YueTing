package com.aqrlei.graduation.yueting.util

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.support.annotation.LayoutRes
import android.view.Gravity
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ListView
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.ui.adapter.PopViewListAdapter

/**
 * created by AqrLei on 2018/4/27
 */
fun createPopView(context: Context, @LayoutRes layoutRes: Int, gravity: Int = Gravity.BOTTOM): Dialog {
    return Dialog(context, R.style.BottomDialog).apply {
        setContentView(layoutRes)
        window.setGravity(gravity)
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
    }
}

fun createListPopView(
        context: Context,
        gravity:
        Int = Gravity.BOTTOM,
        adapter: PopViewListAdapter,
        listener: AdapterView.OnItemClickListener?): Dialog {
    return Dialog(context, R.style.BottomDialog).apply {
        setContentView(R.layout.common_list_pop_view)
        window.setGravity(gravity)
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        window.decorView?.also {
            (it.findViewById(R.id.popViewLv) as ListView).apply {
                this.adapter = adapter
                listener?.let {
                    this.onItemClickListener = listener
                }
            }
        }
    }
}