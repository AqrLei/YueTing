package com.guoshujinfu.aqrlei.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity


abstract class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeSetContentView()
        setContentView(layoutRes)
        initComponents(savedInstanceState)
    }

    protected open fun beforeSetContentView() {

    }

    protected abstract val layoutRes: Int


    protected open fun initComponents(savedInstanceState: Bundle?) {


    }


}

