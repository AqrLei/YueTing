package com.aqrlei.graduation.yueting.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.basemvp.MvpContract
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.presenter.MainPresenter
import com.aqrlei.graduation.yueting.ui.view.PathView
import com.aqrlei.graduation.yueting.util.AppToast
import com.aqrlei.graduation.yueting.util.threadSwitch
import io.reactivex.Single
import kotlinx.android.synthetic.main.include_welcome_view.view.*
import kotlinx.android.synthetic.main.welcome_activity_main.*


/**
 * @Author: AqrLei
 * @Date: 2017/8/22
 */

class MainActivity : MvpContract.MvpActivity<MainPresenter>(),
        View.OnClickListener,
        PathView.OnPathAnimationCallBack {

    companion object {
        private const val DURATION = 2000L
        fun setView(context: MainActivity, v: View, w: Float, h: Float, mPresenter: MainPresenter): Single<Boolean> {
            return Single.defer {
                var isDone = true
                try {
                    v.apply {
                        loadRT0Pv.apply {
                            prepare(
                                    path = mPresenter.getPath(MainPresenter.PathType.TOP_RIGHT, h, w),
                                    duration = DURATION)
                        }
                        loadRT1Pv.apply {
                            prepare(path = mPresenter.getPath(MainPresenter.PathType.TOP_RIGHT, h, w).apply {
                                offset(50f, -50f)
                            },
                                    duration = DURATION)
                        }
                        loadRT2Pv.apply {
                            prepare(path = mPresenter.getPath(MainPresenter.PathType.TOP_RIGHT, h, w).apply {
                                offset(100f, -100f)
                            },
                                    duration = DURATION)
                        }
                        loadRB0Pv.apply {
                            prepare(
                                    path = mPresenter.getPath(MainPresenter.PathType.BOTTOM_RIGHT, h, w),
                                    duration = DURATION)
                        }
                        loadRB1Pv.apply {
                            prepare(path = mPresenter.getPath(MainPresenter.PathType.BOTTOM_RIGHT, h, w).apply {
                                offset(50f, 50f)
                            },
                                    duration = DURATION)
                        }
                        loadRB2Pv.apply {
                            prepare(path = mPresenter.getPath(MainPresenter.PathType.BOTTOM_RIGHT, h, w).apply {
                                offset(100f, 100f)
                            },
                                    duration = DURATION)
                        }
                        loadLT0Pv.apply {
                            prepare(
                                    path = mPresenter.getPath(MainPresenter.PathType.TOP_LEFT, h, w),
                                    duration = DURATION)
                        }
                        loadLT1Pv.apply {
                            prepare(path = mPresenter.getPath(MainPresenter.PathType.TOP_LEFT, h, w).apply {
                                offset(-50f, -50f)
                            },
                                    duration = DURATION)
                        }
                        loadLT2Pv.apply {
                            prepare(path = mPresenter.getPath(MainPresenter.PathType.TOP_LEFT, h, w).apply {
                                offset(-100f, -100f)
                            },
                                    duration = DURATION)
                        }
                        loadLB0Pv.apply {
                            prepare(
                                    path = mPresenter.getPath(MainPresenter.PathType.BOTTOM_LEFT, h, w),
                                    duration = DURATION)
                        }
                        loadLB1Pv.apply {
                            prepare(path = mPresenter.getPath(MainPresenter.PathType.BOTTOM_LEFT, h, w).apply {
                                offset(-50f, 50f)
                            },
                                    duration = DURATION)
                        }
                        loadLB2Pv.apply {
                            prepare(path = mPresenter.getPath(MainPresenter.PathType.BOTTOM_LEFT, h, w).apply {
                                offset(-100f, 100f)
                            },
                                    duration = DURATION)
                        }
                        loadCLPv.apply {
                            prepare(
                                    path = mPresenter.getPath(MainPresenter.PathType.CENTER_LEFT, h, w),
                                    duration = DURATION)
                        }
                        loadCRPv.apply {
                            prepare(
                                    path = mPresenter.getPath(MainPresenter.PathType.CENTER_RIGHT, h, w),
                                    duration = DURATION)
                            setCallBack(context)
                        }
                    }
                } catch (e: Exception) {
                    isDone = false
                }

                Single.just(isDone)
            }.threadSwitch()
        }
    }

    private val v: View
            by lazy {
                pathViewVs.inflate()
            }
    override val layoutRes: Int
        get() = R.layout.welcome_activity_main
    override val mPresenter: MainPresenter
        get() = MainPresenter(this)

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
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

    override fun callBack(id: Int) {
        goTv.visibility = View.VISIBLE
        goTv.bringToFront()
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.goTv) jump()
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        initView()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == YueTingConstant.RQ_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                YueTingListActivity.jumpToYueTingListActivity(this)
            } else {
                AppToast.toastShow(this, "Permission Denied!", 1000)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun jump() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionCheck(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)) {
                YueTingListActivity.jumpToYueTingListActivity(this)
            }
        } else {
            YueTingListActivity.jumpToYueTingListActivity(this)
        }
    }

    private fun initView() {
        goTv.setOnClickListener(this)
        val w: Float = this.resources.displayMetrics.widthPixels.toFloat()
        val h: Float = this.resources.displayMetrics.heightPixels.toFloat()
        val disposable = setView(this, v, w, h, mPresenter)
                .subscribe({
                    if (it) startAnimation()
                }, {})
        mPresenter.addDisposables(disposable)
    }

    private fun startAnimation() {
        v.apply {
            loadRT0Pv.startAnimation()
            loadRT1Pv.startAnimation()
            loadRT2Pv.startAnimation()
            loadRB0Pv.startAnimation()
            loadRB1Pv.startAnimation()
            loadRB2Pv.startAnimation()
            loadLT0Pv.startAnimation()
            loadLT1Pv.startAnimation()
            loadLT2Pv.startAnimation()
            loadLB0Pv.startAnimation()
            loadLB1Pv.startAnimation()
            loadLB2Pv.startAnimation()
            loadCLPv.startAnimation()
            loadCRPv.startAnimation()
        }
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
        ActivityCompat.requestPermissions(this, tempPermission, YueTingConstant.RQ_PERMISSION_CODE)
        return false
    }
}