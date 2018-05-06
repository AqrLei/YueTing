package com.aqrlei.graduation.yueting.ui.fragment

import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.SeekBar
import android.widget.Toast
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.AppCache
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.CacheConstant
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.util.ChapterLoader
import com.aqrlei.graduation.yueting.model.BookInfo
import com.aqrlei.graduation.yueting.presenter.PdfRendererPresenter
import com.aqrlei.graduation.yueting.ui.PdfReadActivity
import com.github.barteksc.pdfviewer.listener.*
import kotlinx.android.synthetic.main.read_fragment_pdf.*
import kotlinx.android.synthetic.main.read_include_bottom.*
import kotlinx.android.synthetic.main.read_include_progress.*
import kotlinx.android.synthetic.main.read_include_setting.*
import kotlinx.android.synthetic.main.read_include_top.*
import java.io.File
import java.io.IOException

/**
 * Author : AqrLei
 * Date : 2018/2/12.
 */

class PdfRendererFragment : MvpContract.MvpFragment<PdfRendererPresenter, PdfReadActivity>(),
        SeekBar.OnSeekBarChangeListener,
        AdapterView.OnItemSelectedListener,
        View.OnClickListener,
        OnPageChangeListener,
        OnPageErrorListener,
        OnLoadCompleteListener,
        OnErrorListener,
        OnTapListener {

    companion object {
        fun newInstance(bookInfo: BookInfo): PdfRendererFragment {
            val args = Bundle()
            args.putSerializable(YueTingConstant.READ_BOOK_INFO, bookInfo)
            val fragment = PdfRendererFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val layoutRes: Int
        get() = R.layout.read_fragment_pdf
    override val mPresenter: PdfRendererPresenter
        get() = PdfRendererPresenter(this)
    private val currentPageIndex = "current_page_index"
    private var mPageIndex: Int = 0
    private var currentIndex: Int = 0
    private var display: Boolean = false
    private var dProgress: Boolean = false
    private var dSetting: Boolean = false
    private var pageCount: Int = 0
    private var pdfReadMode: Boolean = false
    private val bookInfo: BookInfo
        get() = arguments?.getSerializable(YueTingConstant.READ_BOOK_INFO) as BookInfo

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        changeReadMode(position == 1)
    }

    override fun onError(t: Throwable?) {
        AppToast.toastShow(mContainerActivity, "出错了~", 1000)
        mContainerActivity.finish()
    }

    override fun onPageError(page: Int, t: Throwable?) {
        AppToast.toastShow(mContainerActivity, "出错了~", 1000)
        mContainerActivity.finish()
    }

    override fun loadComplete(nbPages: Int) {
        pageCount = nbPages
        sb_rate.max = pageCount
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        currentIndex = page
        if (page == 0) {
            AppToast.toastShow(mContainerActivity, "已经是第一页了", 1000, Gravity.TOP)
        }
        if (page + 1 == pageCount) {
            AppToast.toastShow(mContainerActivity, "已经是最后一页了", 1000, Gravity.BOTTOM)

        }
    }

    override fun onTap(e: MotionEvent?): Boolean {
        showMenu()
        return false
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (seekBar?.id == R.id.sb_rate) {
            tv_done_percent.text = "${if (progress == 0) 1 else progress} / $pageCount "

        }
        if (seekBar?.id == R.id.sb_light_degree) {
            changeBright(progress)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        if (seekBar?.id == R.id.sb_rate) {
            loadPdfFile(if (seekBar.progress - 1 < 0) 0 else seekBar.progress - 1)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.backIv -> {
                this@PdfRendererFragment.finish()
            }
            R.id.addMarkIv -> {
                hideView()
                addBookMark(currentIndex)
            }
            R.id.tv_catalog -> {
                hideView()
                mContainerActivity.jumpToCatalog()
            }
            R.id.tv_rate -> {
                sb_rate.progress = if (currentIndex == 0) currentIndex + 1 else currentIndex
                tv_done_percent.text = "${if (currentIndex == 0) currentIndex + 1 else currentIndex}/ $pageCount"
                ll_bottom_read_seekBar.visibility = View.VISIBLE
                ll_bottom_read_seekBar.bringToFront()
                dProgress = true
                hideView()
            }
            R.id.tv_setting -> {
                ll_bottom_read_setting.visibility = View.VISIBLE
                ll_bottom_read_setting.bringToFront()
                spPdfReadMode.visibility = View.VISIBLE
                ll_bottom_font.visibility = View.GONE
                rg_read_bg.visibility = View.GONE
                dSetting = true
                hideView()
            }
        }
    }

    override fun initComponents(view: View?, savedInstanceState: Bundle?) {
        super.initComponents(view, savedInstanceState)
        initListener()
        try {
            sb_light_degree.progress =
                    Settings.System.getInt(mContainerActivity.contentResolver, Settings.System.SCREEN_BRIGHTNESS)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        bookTitleTv.text = bookInfo.name
        ChapterLoader.init(bookInfo)
        mPageIndex = bookInfo.indexBegin
        if (null != savedInstanceState) {
            mPageIndex = savedInstanceState.getInt(currentPageIndex, 0)
        }
    }

    override fun onStart() {
        super.onStart()
        try {
            getCache()
            spPdfReadMode.setSelection(if (pdfReadMode) 1 else 0)
            getIndexFromDB()
            loadPdfFile(mPageIndex, pdfReadMode)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(activity, "Error! " + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        putCache()
        putIndexToDB(currentIndex)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(currentPageIndex, currentIndex)
    }

    fun onBackPressed(): Boolean {
        return if (display || dSetting || dProgress) {
            ll_bottom_read_seekBar.visibility = View.INVISIBLE
            ll_bottom_read_setting.visibility = View.INVISIBLE
            hideView()
            display = false
            dSetting = false
            dProgress = false
            true
        } else {
            false
        }
    }

    fun setCurrentIndex(index: Int) {
        mPageIndex = index
    }

    fun showMenu() {
        if (display) {
            hideView()
        } else {
            if (!dSetting && !dProgress) {
                displayView()
            }
        }
    }

    fun putIndexToDB(index: Int) {
        mPresenter.addIndexToDB(bookInfo.path, index, pageCount)
    }

    private fun initListener() {
        backIv.setOnClickListener(this)
        addMarkIv.setOnClickListener(this)
        tv_catalog.setOnClickListener(this)
        tv_rate.setOnClickListener(this)
        tv_setting.setOnClickListener(this)
        sb_rate.setOnSeekBarChangeListener(this)
        sb_light_degree.setOnSeekBarChangeListener(this)
        spPdfReadMode.onItemSelectedListener = this
    }

    private fun putCache() {
        AppCache.APPCACHE.putBoolean(CacheConstant.READ_PDF_MODE, pdfReadMode)
                .commit()
    }

    private fun addBookMark(currentIndex: Int) {
        mPresenter.addMarkToDB(bookInfo.path, currentIndex)
    }

    private fun changeBright(brightness: Int) {
        val window = mContainerActivity.window
        val lp = window.attributes
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        } else {
            lp.screenBrightness = (if (brightness <= 0) 1 else brightness) / 255f
        }
        window.attributes = lp
    }

    private fun displayView() {
        ll_bottom_read.visibility = View.VISIBLE
        rl_top_read.visibility = View.VISIBLE
        ll_bottom_read.bringToFront()
        rl_top_read.bringToFront()
        display = true
    }

    private fun hideView() {
        ll_bottom_read.visibility = View.INVISIBLE
        rl_top_read.visibility = View.INVISIBLE
        display = false
    }

    private fun loadPdfFile(page: Int, swipeMode: Boolean = false) {
        pv_pdf_read.fromFile(File(bookInfo.path))
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(swipeMode)
                .enableDoubletap(true)
                .defaultPage(page)
                .onPageChange(this)
                .onError(this)
                .onPageError(this)
                // called on single tap, return true if handled, false to toggle scroll handle visibility
                .onTap(this)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
               // .pageFitPolicy(FitPolicy.BOTH)
                .onLoad(this)
                .load()

    }

    private fun changeReadMode(type: Boolean) {
        if (pdfReadMode != type) {
            loadPdfFile(currentIndex, type)
            pdfReadMode = type
        }
    }

    private fun getCache() {
        pdfReadMode = AppCache.APPCACHE.getBoolean(CacheConstant.READ_PDF_MODE, pdfReadMode)
    }

    private fun getIndexFromDB() {
        mPageIndex = mPresenter.getIndexFromDB(bookInfo.path)
    }
}
