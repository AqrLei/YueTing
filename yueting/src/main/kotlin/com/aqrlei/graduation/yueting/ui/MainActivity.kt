package com.aqrlei.graduation.yueting.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.AppToast
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (permissionCheck(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)) {
                        YueTingActivity.jumpToYueTingActivity(this, 0)
                    }
                } else {
                    YueTingActivity.jumpToYueTingActivity(this, 0)
                }
            }
        }
    }

    override val layoutRes: Int
        get() = R.layout.activity_main
    override val mPresenter: MainActivityPresenter
        get() = MainActivityPresenter(this)
    private val RQ_PERMISSION_CODE = 1

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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == RQ_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                YueTingActivity.jumpToYueTingActivity(this, 0)
            } else {
                AppToast.toastShow(this, "Permission Denied!", 1000)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun permissionCheck(vararg permission: String): Boolean {
        val tempPermission = Array(permission.size, { "" })
        var index = 0
        permission.forEach {
            if (ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED) {
                tempPermission[index++] = it
            }
        }
        if (tempPermission[0] == "") {
            return true
        }

        ActivityCompat.requestPermissions(this, tempPermission, RQ_PERMISSION_CODE)
        return false

    }


}