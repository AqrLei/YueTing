package com.example.testapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife

class MainActivity : AppCompatActivity() {
    @BindView(R.id.tv_test)
    internal var mTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        mTextView!!.text = (application as TestApplication).name
        val intent = (application as TestApplication).testIntent
        applicationContext.startService(intent)

    }
}
