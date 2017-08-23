package com.aqrlei.graduation.truckrental.ui

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.aqrlei.graduation.truckrental.R
import com.aqrlei.graduation.truckrental.baselib.mvp.MvpContract
import com.aqrlei.graduation.truckrental.baselib.util.adapter.ViewPagerAdapter
import com.aqrlei.graduation.truckrental.baselib.util.net.config.HttpReqConfig
import com.aqrlei.graduation.truckrental.model.resp.PictureRespBean
import com.aqrlei.graduation.truckrental.presenter.activitypresenter.MainPresenter
import kotlinx.android.synthetic.main.activity_picture.*
import kotlinx.android.synthetic.main.picture_from_url.view.*

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/22
 */

class MainActivity : MvpContract.MvpActivity<MainPresenter>() {

    private var mPictureRespBeans: MutableList<PictureRespBean>? = null
    private var mViewpagerAdapter: ViewPagerAdapter<*>? = null
    private var mViews: MutableList<View>? = null
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == 1) {
                vp_impage.currentItem = vp_impage.currentItem + 1
                this.sendEmptyMessageDelayed(1, 1000)
            }
        }
    }


    override val layoutRes: Int
        get() = R.layout.activity_picture
    override val mPresenter: MainPresenter
        get() = MainPresenter(this)


    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        mPresenter.getImg(HttpReqConfig.RQ_IMG_TYPE)
        //mHandler.sendEmptyMessageDelayed(1, 1000)

    }

    fun initViews(data: List<PictureRespBean>) {
        Log.d("Lei","initViews")
        mPictureRespBeans = ArrayList()
        mPictureRespBeans!!.addAll(data)
        mViews = ArrayList()
        for (i in mPictureRespBeans!!.indices) {
            addImgs(i)
        }
        mViewpagerAdapter = ViewPagerAdapter(mViews)
        vp_impage!!.adapter = mViewpagerAdapter
    }

    private fun addImgs(pos: Int) {
        val view = LayoutInflater.from(this).inflate(R.layout.picture_from_url, null)
        view.sdv_picture.setImageURI(Uri.parse(mPictureRespBeans!![pos].pictureUrl))
        /* view.sdv_picture.setImageURI()*/
        mViews!!.add(view)
    }
}