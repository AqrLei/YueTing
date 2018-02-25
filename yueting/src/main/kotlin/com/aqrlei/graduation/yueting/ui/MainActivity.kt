package com.aqrlei.graduation.yueting.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.presenter.activitypresenter.MainActivityPresenter
import kotlinx.android.synthetic.main.activity_main.*


/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/22
 */

class MainActivity : MvpContract.MvpActivity<MainActivityPresenter>(),
        View.OnClickListener {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_enjoy -> {
                YueTingActivity.jumpToYueTingActivity(this, 0)
            }
        }
    }

    override val layoutRes: Int
        get() = R.layout.activity_main
    override val mPresenter: MainActivityPresenter
        get() = MainActivityPresenter(this)

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            val decorView = window.decorView
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        bt_enjoy.setOnClickListener(this)
    }


}