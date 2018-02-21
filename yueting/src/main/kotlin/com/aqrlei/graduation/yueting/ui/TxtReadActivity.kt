package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.ui.view.PageView
import com.aqrairsigns.aqrleilib.util.AppCache
import com.aqrairsigns.aqrleilib.util.AppLog
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.factory.ChapterFactory
import com.aqrlei.graduation.yueting.factory.PageFactory
import com.aqrlei.graduation.yueting.model.local.BookInfo
import com.aqrlei.graduation.yueting.presenter.activitypresenter.TxtReadActivityPresenter
import kotlinx.android.synthetic.main.read_item_progress.*
import kotlinx.android.synthetic.main.read_item_setting.*
import kotlinx.android.synthetic.main.read_item_top.*
import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/11/17.
 */
class TxtReadActivity : MvpContract.MvpActivity<TxtReadActivityPresenter>(),
        PageView.OnScrollListener,
        View.OnLongClickListener,
        SeekBar.OnSeekBarChangeListener,
        RadioGroup.OnCheckedChangeListener,
        AdapterView.OnItemSelectedListener {

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        pageFactory.changeFontStyle(position)
    }


    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        val bgColor = (findViewById(checkedId).background as ColorDrawable).color
        val position: Int = (0 until 4).firstOrNull { group?.getChildAt(it)?.id == checkedId }
                ?: 0

        pageFactory.setPageBackground(bgColor, position)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (seekBar?.id == R.id.sb_rate) {

            val percent = DecimalFormat("#00.0").format(progress * 1.0f / 10.0f)
            tv_done_percent.text = "$percent %"

            val pBegin = (bookInfo.fileLength * ((BigDecimal(percent).div(BigDecimal(100))).toDouble())).toInt()
            pageFactory.nextPage(1, pBegin)
        }
        if (seekBar?.id == R.id.sb_light_degree) {
            changeBright(progress)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onLongClick(v: View?): Boolean {
        if (display) {
            hideView()
        } else {
            if (!dSetting && !dProgress) {
                displayView()
            }
        }
        return true
    }

    override fun onLeftScroll() {
        if (!display && !dSetting && !dProgress) {
            pageFactory.nextPage()
        }
    }

    override fun onRightScroll() {
        if (!display && !dSetting && !dProgress) {
            pageFactory.prePage()
        }
    }

    override fun onBackPressed() {
        if (display || dSetting || dProgress) {
            lLSeekBar.visibility = View.INVISIBLE
            lLSetting.visibility = View.INVISIBLE
            hideView()
            dSetting = false
            dProgress = false
            display = false
        } else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 2) {
            if (requestCode == REQUESTCODE) {
                val bPosition = data?.extras?.getInt("bPosition") ?: 0
                pageFactory.nextPage(1, bPosition)
            }
        }
    }

    override val mPresenter: TxtReadActivityPresenter
        get() = TxtReadActivityPresenter(this)
    override val layoutRes: Int
        get() = R.layout.activity_txt_read
    private val pageFactory = PageFactory.PAGEFACTORY
    private lateinit var seekBar: SeekBar
    private lateinit var topRelativeLayout: RelativeLayout
    private lateinit var bottomLinearLayout: LinearLayout
    private lateinit var lLSetting: LinearLayout
    private lateinit var lLSeekBar: LinearLayout
    private lateinit var pageView: PageView
    private var display: Boolean = false
    private var dProgress: Boolean = false
    private var dSetting: Boolean = false
    private lateinit var bookInfo: BookInfo
    private val REQUESTCODE = 1

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        pageView = findViewById(R.id.pv_read) as PageView
        seekBar = findViewById(R.id.sb_rate) as SeekBar
        topRelativeLayout = findViewById(R.id.rl_top_read) as RelativeLayout
        bottomLinearLayout = findViewById(R.id.ll_bottom_read) as LinearLayout
        lLSeekBar = findViewById(R.id.ll_bottom_read_seekBar) as LinearLayout
        lLSetting = findViewById(R.id.ll_bottom_read_setting) as LinearLayout
        seekBar.setOnSeekBarChangeListener(this)
        sp_textStyle_select.onItemSelectedListener = this
        sb_light_degree.setOnSeekBarChangeListener(this)
        sb_light_degree.progress = (window.attributes.screenBrightness * 100).toInt()
        rg_read_bg.setOnCheckedChangeListener(this)
        setPageFactory(pageView)
        setCheckedId()
        tv_book_title.text = bookInfo.name

    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> {
                this@TxtReadActivity.finish()
            }
            R.id.tv_add_mark -> {
                addBookMark(pageFactory.getCurrentBegin())
                hideView()
            }
            R.id.tv_catalog -> {
                getCatalog()
                hideView()

            }
            R.id.tv_rate -> {
                lLSeekBar.visibility = View.VISIBLE
                lLSeekBar.bringToFront()
                dProgress = true
                hideView()
            }
            R.id.tv_setting -> {
                lLSetting.visibility = View.VISIBLE
                lLSetting.bringToFront()
                dSetting = true
                hideView()
            }
            R.id.tv_textSize_small -> {
                pageFactory.changeFontSize(15f)

            }
            R.id.tv_textSize_middle -> {
                pageFactory.changeFontSize(22f)
            }
            R.id.tv_textSize_big -> {
                pageFactory.changeFontSize(30f)
            }
        }
    }

    private fun setCheckedId() {
        for (i in 0 until 4) {
            (rg_read_bg.getChildAt(i) as RadioButton).isChecked =
                    i == AppCache.APPCACHE.getInt("bPosition", 0)
        }
    }

    private fun addBookMark(currentBegin: Int) {
        mPresenter.addMarkToDB(bookInfo.path, currentBegin)
    }

    fun jumpToCatalog(flag: Boolean) {

        if (flag) AppLog.logDebug("test", "catalog load successful")
        else AppLog.logDebug("test", "catalog load failure")
        //CatalogActivity.jumpToCatalogActivity(this, " ")

        startActivityForResult(Intent(this, CatalogActivity::class.java), REQUESTCODE)
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

    private fun setPageFactory(pageView: PageView) {
        bookInfo = intent.extras.getSerializable("bookInfo") as BookInfo
        ChapterFactory.init(bookInfo)
        pageFactory.setBookInfo(pageView, bookInfo)
        pageFactory.nextPage()
        pageView.setOnLongClickListener(this)
        pageView.setOnScrollListener(this)
    }

    private fun changeBright(brightValue: Int) {
        val lp = window.attributes
        lp.screenBrightness = if (brightValue <= 0) -1f else brightValue / 100f
        window.attributes = lp
    }

    private fun getCatalog() {
        mPresenter.getCatalog()
    }

    companion object {
        fun jumpToTxtReadActivity(context: Context, data0: BookInfo, data1: Int = 0) {
            val intent = Intent(context, TxtReadActivity::class.java)
            /* val bundle = Bundle()
             bundle.putSerializable("bookInfo",data)*/
            intent.putExtra("bookInfo", data0)
            intent.putExtra("bPosition", data1)
            if (IntentUtil.queryActivities(context, intent)) context.startActivity(intent)
        }

    }
}