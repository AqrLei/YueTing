package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.ui.view.BookPageView
import com.aqrairsigns.aqrleilib.util.AppCache
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.factory.BookPageFactory
import com.aqrlei.graduation.yueting.factory.ChapterFactory
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
        BookPageView.OnPageTouchListener,
        SeekBar.OnSeekBarChangeListener,
        RadioGroup.OnCheckedChangeListener,
        AdapterView.OnItemSelectedListener {


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        pageFactory.changeFontStyle(position)
        // bookPageFactory.changeFontStyle(position)
    }


    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        val bgColor = (findViewById(checkedId).background as ColorDrawable).color
        val position: Int = (0 until 4).firstOrNull { group?.getChildAt(it)?.id == checkedId }
                ?: 0
        pageFactory.setPageBackground(bgColor, position)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (seekBar?.id == R.id.sb_rate) {

            val percent = DecimalFormat("#00.00").format(progress * 1.00f / 10.00f)
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

    override fun onMiddleClick() {
        showMenu()
    }

    override fun onFinalScroll() {
        pageFactory.setMCanvasAContent()
    }

    override fun onLeftScroll() {
        if (!display && !dSetting && !dProgress) {
            if (pageFactory.getCurrentBegin() <= 0) {
                AppToast.toastShow(this, "已经是第一页了", 1000, Gravity.TOP)
            } else {
                pageFactory.prePage()
            }
        }
    }

    override fun onRightScroll() {
        if (!display && !dSetting && !dProgress) {
            if (pageFactory.getCurrentEnd() >= bookInfo.fileLength) {
                AppToast.toastShow(this, "已经是最后一页了", 1000, Gravity.BOTTOM)
            } else {
                pageFactory.nextPage()
            }
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_MENU -> {
                showMenu()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    /*在restart -> start -> resume之前调用， 在其跳转的Activity的pause之后调用*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == YueTingConstant.CATALOGRECODE) {
            if (requestCode == YueTingConstant.TXTRQCODE) {
                val bPosition = data?.extras?.getInt("bPosition") ?: 0
                pageFactory.nextPage(1, bPosition)
                mPresenter.addIndexToDB(bookInfo.path, pageFactory.getCurrentBegin(),
                        pageFactory.getCurrentEnd())
            }
        }
    }

    override val mPresenter: TxtReadActivityPresenter
        get() = TxtReadActivityPresenter(this)
    override val layoutRes: Int
        get() = R.layout.activity_txt_read
    private val pageFactory = BookPageFactory.BOOKPAGEFACTORY
    // private val bookPageFactory = BookPageFactory.BOOKPAGEFACTORY
    private lateinit var seekBar: SeekBar
    private lateinit var topRelativeLayout: RelativeLayout
    private lateinit var bottomLinearLayout: LinearLayout
    private lateinit var lLSetting: LinearLayout
    private lateinit var lLSeekBar: LinearLayout
    // private lateinit var pageView: PageView
    private lateinit var bookPageView: BookPageView
    private var display: Boolean = false
    private var dProgress: Boolean = false
    private var dSetting: Boolean = false
    private lateinit var bookInfo: BookInfo

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
        // pageView = findViewById(R.id.pv_read) as PageView
        bookPageView = findViewById(R.id.bpv_read) as BookPageView
        seekBar = findViewById(R.id.sb_rate) as SeekBar
        topRelativeLayout = findViewById(R.id.rl_top_read) as RelativeLayout
        bottomLinearLayout = findViewById(R.id.ll_bottom_read) as LinearLayout
        lLSeekBar = findViewById(R.id.ll_bottom_read_seekBar) as LinearLayout
        lLSetting = findViewById(R.id.ll_bottom_read_setting) as LinearLayout
        seekBar.setOnSeekBarChangeListener(this)
        sp_textStyle_select.onItemSelectedListener = this
        try {
            sb_light_degree.progress =
                    Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        rg_read_bg.setOnCheckedChangeListener(this)
        sb_light_degree.setOnSeekBarChangeListener(this)
        // setPageFactory(pageView)
        setBookPageFactory(bookPageView)
        setCheckedId()
        tv_book_title.text = bookInfo.name

    }

    override fun onPause() {
        super.onPause()
        addIndexToDB()
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
                jumpToCatalog()
                hideView()

            }
            R.id.tv_rate -> {
                val percent = DecimalFormat("#00.00").format(pageFactory.getCurrentBegin() * 100.00f / bookInfo.fileLength * 1.00f)
                tv_done_percent.text = "$percent %"
                seekBar.progress = pageFactory.getCurrentBegin() / bookInfo.fileLength * 100
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

    private fun showMenu() {
        if (display) {
            hideView()
        } else {
            if (!dSetting && !dProgress) {
                displayView()
            }
        }
    }

    private fun addIndexToDB() {
        mPresenter.addIndexToDB(bookInfo.path, pageFactory.getCurrentBegin(), pageFactory.getCurrentEnd())
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

    private fun jumpToCatalog() {
        startActivityForResult(Intent(this, CatalogActivity::class.java), YueTingConstant.TXTRQCODE)
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

    /* private fun setPageFactory(pageView: PageView) {
         bookInfo = intent.extras.getSerializable("bookInfo") as BookInfo
         ChapterFactory.init(bookInfo)
         pageFactory.setBookInfo(pageView, bookInfo)
         pageFactory.nextPage()
         pageView.setOnLongClickListener(this)
        // pageView.setOnPageTouchListener(this)
     }*/
    private fun setBookPageFactory(bookPageView: BookPageView) {
        bookInfo = intent.extras.getSerializable("bookInfo") as BookInfo
        ChapterFactory.init(bookInfo)
        pageFactory.setBookInfo(bookPageView, bookInfo)
        pageFactory.nextPage()
        bookPageView.setOnPageTouchListener(this)
    }

    private fun changeBright(brightness: Int) {
        val window = this.window
        val lp = window.attributes
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        } else {
            lp.screenBrightness = (if (brightness <= 0) 1 else brightness) / 255f
        }
        window.attributes = lp
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