package com.aqrlei.graduation.yueting.ui.dialog

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.aqrairsigns.aqrleilib.view.dialog.BaseDialog
import com.aqrlei.graduation.yueting.R
import kotlinx.android.synthetic.main.test_dialog.view.*

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/9/9.
 */

/*
* 自定义Dialog的具体实现
* */
class TestDialog(context: Context) :
        BaseDialog(context, R.layout.test_dialog, R.style.TestDialog) {
    private lateinit var mTitle: TextView
    private lateinit var mMsg: TextView
    private lateinit var mNegativeButton: TextView
    private lateinit var mPositiveButton: TextView
    private lateinit var mDivider: View

    override fun bindView(v: View) {
        mTitle = v.tv_test_dialog_title
        mMsg = v.tv_test_dialog_msg
        mNegativeButton = v.tv_test_dialog_negative_button
        mPositiveButton = v.tv_test_dialog_positive_button
        mDivider = v.v_test_dialog_divider
    }

    fun setTitle(title: String): TestDialog {
        mTitle.text = if (TextUtils.isEmpty(title)) "无标题"
        else title
        mTitle.visibility = View.VISIBLE
        return this
    }

    fun setMessage(msg: String): TestDialog {
        mTitle.text = if (TextUtils.isEmpty(msg)) "无标题"
        else msg
        mMsg.visibility = View.VISIBLE
        return this
    }

    fun setNegativeButton(content: String, listener: View.OnClickListener?): TestDialog {
        mDivider.visibility = View.VISIBLE
        mNegativeButton.text = if (TextUtils.isEmpty(content)) "0"
        else content
        mNegativeButton.visibility = View.VISIBLE
        mNegativeButton.setOnClickListener { v ->
            dismiss()
            listener?.onClick(v)
        }
        return this
    }

    fun setPositiveButton(content: String, listener: View.OnClickListener?): TestDialog {
        mDivider.visibility = View.VISIBLE
        mPositiveButton.text = if (TextUtils.isEmpty(content)) "0"
        else content
        mPositiveButton.visibility = View.VISIBLE
        mPositiveButton.setOnClickListener { v ->
            dismiss()
            listener?.onClick(v)
        }
        return this
    }
}