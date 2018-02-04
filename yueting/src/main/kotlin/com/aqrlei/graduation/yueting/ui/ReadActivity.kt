package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrairsigns.aqrleilib.view.PageView
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.factory.PageFactory
import com.aqrlei.graduation.yueting.presenter.activitypresenter.ReadActivityPresenter

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/11/17.
 */
class ReadActivity : MvpContract.MvpActivity<ReadActivityPresenter>(), PageView.OnScrollListener,
        View.OnLongClickListener {
    override fun onLongClick(v: View?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLeftScroll() {
        pageFactory.nextPage()
    }

    override fun onRightScroll() {
        pageFactory.prePage()
    }

    override val mPresenter: ReadActivityPresenter
        get() = ReadActivityPresenter(this)
    override val layoutRes: Int
        get() = R.layout.activity_read
    private lateinit var pageFactory: PageFactory

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        val view = findViewById(R.id.pv_read) as PageView
        setPageFactory(view)

    }

    private fun setPageFactory(pageView: PageView) {
        pageFactory = mPresenter.getPageFactory(pageView)
        pageFactory.nextPage()
        pageView.setOnLongClickListener(this)
        pageView.setOnScrollListener(this)
    }

    companion object {
        fun jumpToReadActivity(context: Context, data: Int) {
            val intent = Intent(context, ReadActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("code", data)
            if (IntentUtil.queryActivities(context, intent)) context.startActivity(intent)
        }

    }
}