package com.aqrlei.graduation.yueting.mvp

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author  aqrLei on 2018/5/15
 */
abstract class BaseFragment<T : BaseActivity> : Fragment(),BaseView {
    protected abstract val layoutRes: Int
    protected lateinit var wrapperActivity: T
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        @Suppress("UNCHECKED_CAST")
        wrapperActivity = context as T

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }
    protected abstract fun initComponents()
}