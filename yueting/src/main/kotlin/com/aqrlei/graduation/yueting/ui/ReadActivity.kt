package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.AppLog
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrairsigns.aqrleilib.view.PageView
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.factory.PageFactory
import com.aqrlei.graduation.yueting.model.local.BookInfo
import com.aqrlei.graduation.yueting.presenter.activitypresenter.ReadActivityPresenter

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/11/17.
 */
class ReadActivity : MvpContract.MvpActivity<ReadActivityPresenter>(),
        PageView.OnScrollListener,
        View.OnLongClickListener,
        SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        AppLog.logDebug("test", "onProgressChanged $progress")
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        AppLog.logDebug("test", "onStartTrackingTouch ${seekBar?.progress}")
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        AppLog.logDebug("test", "onStopTrackingTouch ${seekBar?.progress}")
    }

    override fun onLongClick(v: View?): Boolean {
        if (display) {
            hideView()
        } else {
            displayView()
        }
        return true
    }

    override fun onLeftScroll() {
        pageFactory.nextPage()
    }

    override fun onRightScroll() {
        pageFactory.prePage()
    }

    private fun displayView() {
        bottomLinearLayout.visibility = View.VISIBLE
        topRelativeLayout.visibility = View.VISIBLE
        bottomLinearLayout.bringToFront()
        topRelativeLayout.bringToFront()
        display = true
    }

    private fun hideView() {
        bottomLinearLayout.visibility = View.GONE
        topRelativeLayout.visibility = View.GONE
        display = false
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> {
                this@ReadActivity.finish()
            }
            R.id.tv_done -> {
                lLSeekBar.visibility = View.INVISIBLE
            }
            R.id.tv_add_mark -> {
                AppLog.logDebug("test", "add mark")
                hideView()
//TODO add bookmark to DB
            }
            R.id.tv_catalog -> {
                AppLog.logDebug("test", "jump to catalog")
                hideView()
                //TODO JumpToCatalogActivity
            }
            R.id.tv_rate -> {
                AppLog.logDebug("test", "display progress control")
                lLSeekBar.visibility = View.VISIBLE
                hideView()
//TODO Display progress control
            }
            R.id.tv_setting -> {
                AppLog.logDebug("test", "show setting")
//TODO about text, background and others
            }
        }
    }

    override val mPresenter: ReadActivityPresenter
        get() = ReadActivityPresenter(this)
    override val layoutRes: Int
        get() = R.layout.activity_read
    private lateinit var pageFactory: PageFactory
    private lateinit var seekBar: SeekBar
    private lateinit var topRelativeLayout: RelativeLayout
    private lateinit var bottomLinearLayout: LinearLayout
    private lateinit var lLSeekBar: LinearLayout
    private lateinit var pageView: PageView
    private var display: Boolean = false

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        pageView = findViewById(R.id.pv_read) as PageView
        seekBar = findViewById(R.id.sb_rate) as SeekBar
        topRelativeLayout = findViewById(R.id.fl_top_read) as RelativeLayout
        bottomLinearLayout = findViewById(R.id.ll_bottom_read) as LinearLayout
        lLSeekBar = findViewById(R.id.ll_bottom_read_seekBar) as LinearLayout
        seekBar.setOnSeekBarChangeListener(this)
        setPageFactory(pageView)

    }

    private fun setPageFactory(pageView: PageView) {
        val bookInfo = intent.extras.getSerializable("bookInfo") as BookInfo
        pageFactory = mPresenter.getPageFactory(bookInfo, pageView)
        pageFactory.nextPage()
        pageView.setOnLongClickListener(this)
        pageView.setOnScrollListener(this)
    }

    companion object {
        fun jumpToReadActivity(context: Context, data: BookInfo) {
            val intent = Intent(context, ReadActivity::class.java)
            /* val bundle = Bundle()
             bundle.putSerializable("bookInfo",data)*/
            intent.putExtra("bookInfo", data)
            if (IntentUtil.queryActivities(context, intent)) context.startActivity(intent)
        }

    }
}