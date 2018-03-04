package com.aqrlei.graduation.yueting.ui.fragment

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.Toast
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.AppCache
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.factory.ChapterFactory
import com.aqrlei.graduation.yueting.model.local.BookInfo
import com.aqrlei.graduation.yueting.presenter.fragmentpresenter.PdfRendererPresenter
import com.aqrlei.graduation.yueting.ui.PdfReadActivity
import com.github.barteksc.pdfviewer.listener.*
import com.github.barteksc.pdfviewer.util.FitPolicy
import kotlinx.android.synthetic.main.fragment_pdf_renderer.*
import kotlinx.android.synthetic.main.read_item_bottom.*
import kotlinx.android.synthetic.main.read_item_progress.*
import kotlinx.android.synthetic.main.read_item_setting.*
import kotlinx.android.synthetic.main.read_item_top.*
import java.io.File
import java.io.IOException

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2018/2/12.
 */

class PdfRendererFragment : MvpContract.MvpFragment<PdfRendererPresenter, PdfReadActivity>(),
        SeekBar.OnSeekBarChangeListener,
        View.OnClickListener,
        OnPageChangeListener,
        OnPageErrorListener,
        OnLoadCompleteListener,
        OnErrorListener,
        OnTapListener {
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

    override val layoutRes: Int
        get() = R.layout.fragment_pdf_renderer
    override val mPresenter: PdfRendererPresenter
        get() = PdfRendererPresenter(this)


    private val STATE_CURRENT_PAGE_INDEX = "current_page_index"

    private var mPageIndex: Int = 0
    private var currentIndex: Int = 0
    private var display: Boolean = false
    private var dProgress: Boolean = false
    private var dSetting: Boolean = false
    private var pageCount: Int = 0

    private val bookInfo: BookInfo
        get() = arguments.getSerializable("bookInfo") as BookInfo

    companion object {
        fun newInstance(bookInfo: BookInfo): PdfRendererFragment {
            val args = Bundle()
            args.putSerializable("bookInfo", bookInfo)
            val fragment = PdfRendererFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> {
                this@PdfRendererFragment.finish()
            }
            R.id.tv_add_mark -> {
                hideView()
                addBookMark(currentIndex)
            }
            R.id.tv_catalog -> {
                hideView()
                mContainerActivity.jumpToCatalog()
            }
            R.id.tv_rate -> {
                sb_rate.progress = currentIndex
                tv_done_percent.text = "$currentIndex / $pageCount"
                ll_bottom_read_seekBar.visibility = View.VISIBLE
                ll_bottom_read_seekBar.bringToFront()
                dProgress = true
                hideView()
            }
            R.id.tv_setting -> {
                ll_bottom_read_setting.visibility = View.VISIBLE
                ll_bottom_read_setting.bringToFront()
                ll_bottom_font.visibility = View.INVISIBLE
                rg_read_bg.visibility = View.INVISIBLE
                dSetting = true
                hideView()
            }
        }
    }

    override fun initComponents(view: View?, savedInstanceState: Bundle?) {
        super.initComponents(view, savedInstanceState)

        iv_back.setOnClickListener(this)
        tv_add_mark.setOnClickListener(this)
        tv_catalog.setOnClickListener(this)
        tv_rate.setOnClickListener(this)
        tv_setting.setOnClickListener(this)
        sb_rate.setOnSeekBarChangeListener(this)
        sb_light_degree.setOnSeekBarChangeListener(this)
        tv_book_title.text = bookInfo.name
        ChapterFactory.init(bookInfo)
        mPageIndex = bookInfo.indexBegin
        setCheckedId()
        if (null != savedInstanceState) {
            mPageIndex = savedInstanceState.getInt(STATE_CURRENT_PAGE_INDEX, 0)
        }
    }

    override fun onStart() {
        super.onStart()
        try {
            getIndexFromDB()
            loadPdfFile(mPageIndex)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(activity, "Error! " + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        putIndexToDB(currentIndex)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_CURRENT_PAGE_INDEX, currentIndex)
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

    private fun setCheckedId() {
        for (i in 0 until 4) {
            (rg_read_bg.getChildAt(i) as RadioButton).isChecked =
                    i == AppCache.APPCACHE.getInt("bPosition2", 0)
        }
    }

    private fun addBookMark(currentIndex: Int) {
        mPresenter.addMarkToDB(bookInfo.path, currentIndex)
    }

    private fun changeBright(brightValue: Int) {
        val lp = mContainerActivity.window.attributes
        lp.screenBrightness = if (brightValue <= 0) -1f else brightValue / 100f
        mContainerActivity.window.attributes = lp
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

    private fun loadPdfFile(page: Int) {
        pv_pdf_read.fromFile(File(bookInfo.path))
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
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
                .pageFitPolicy(FitPolicy.BOTH)
                .onLoad(this)
                .load()

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

    private fun getIndexFromDB() {
        mPageIndex = mPresenter.getIndexFromDB(bookInfo.path)
    }
}
