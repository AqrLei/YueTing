package com.aqrlei.graduation.yueting.ui.fragment

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.drawable.ColorDrawable
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.*
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.AppCache
import com.aqrairsigns.aqrleilib.util.AppLog
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.factory.ChapterFactory
import com.aqrlei.graduation.yueting.model.local.BookInfo
import com.aqrlei.graduation.yueting.presenter.fragmentpresenter.PdfRendererPresenter
import com.aqrlei.graduation.yueting.ui.PdfReadActivity
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
        View.OnTouchListener, SeekBar.OnSeekBarChangeListener,
        RadioGroup.OnCheckedChangeListener,
        View.OnClickListener {


    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        val bgColor = (mContainerActivity.findViewById(checkedId).background as ColorDrawable).color
        val position: Int = (0 until 4).firstOrNull { group?.getChildAt(it)?.id == checkedId }
                ?: 0
        mImageView!!.setBackgroundColor(bgColor)
        AppCache.APPCACHE.putInt("bPosition2", position).commit()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (seekBar?.id == R.id.sb_rate) {
            tv_done_percent.text = "$progress / $pageCount "
            showPage(progress)

        }
        if (seekBar?.id == R.id.sb_light_degree) {
            changeBright(progress)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    override val layoutRes: Int
        get() = R.layout.fragment_pdf_renderer
    override val mPresenter: PdfRendererPresenter
        get() = PdfRendererPresenter(this)


    private val STATE_CURRENT_PAGE_INDEX = "current_page_index"
    private var mFileDescriptor: ParcelFileDescriptor? = null
    private var mPdfRenderer: PdfRenderer? = null
    private var mCurrentPage: PdfRenderer.Page? = null

    private var mImageView: ImageView? = null
    private var mPageIndex: Int = 0
    private var touchSlop: Int = 0

    private var path: String = ""
    private var beginIndex: Int = 0
    private var currentIndex: Int = 0
    private var mode: Int = 0
    private val MODE_DRAG = 1
    private val MODE_ZOOM = 2
    private var startPoint = PointF()

    private var matrix = Matrix()
    private var currentMatrix = Matrix()
    private var originalMatrix = Matrix()
    /**
     * 保存默认位置矩阵的值
     */
    private var matrixValue: FloatArray = floatArrayOf(
            1.0F, 0.0F, 0.0F,
            0.0F, 1.0F, 0.0F,
            0.0F, 0.0F, 1.0F)
    /**
     * 两个手指的开始距离
     */
    private var startDis: Float = 0F
    /**
     * 两个手指的中间点
     */
    private var midPoint: PointF? = null

    private var gestureDetector: GestureDetector? = null

    private var isOriginal = true

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

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val tempValue = FloatArray(9)
        when (event.action and MotionEvent.ACTION_MASK) {
        // 手指压下屏幕
            MotionEvent.ACTION_DOWN -> {
                mode = MODE_DRAG
                // 记录ImageView当前的移动位置
                currentMatrix.set(mImageView!!.imageMatrix)
                startPoint.set(event.x, event.y)
            }
        // 手指在屏幕上移动，改事件会被不断触发
            MotionEvent.ACTION_MOVE -> {

                if (mode == MODE_DRAG) {//拖动图片
                    val dx = event.x - startPoint.x // 得到x轴的移动距离
                    val dy = event.y - startPoint.y // 得到x轴的移动距离
                    // 在没有移动之前的位置上进行移动
                    matrix.set(currentMatrix)
                    matrix.postTranslate(dx, dy)

                } else if (mode == MODE_ZOOM) {// 放大缩小图片
                    val endDis = distance(event)// 结束距离
                    if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                        val scale = endDis / startDis// 得到缩放倍数
                        matrix.set(currentMatrix)
                        matrix.postScale(scale, scale, midPoint!!.x, midPoint!!.y)

                    }
                }

            }
            MotionEvent.ACTION_UP -> {

            }
        // 当触点离开屏幕，但是屏幕上还有触点(手指)
            MotionEvent.ACTION_POINTER_UP -> {
                mode = 0
            }
        // 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
            MotionEvent.ACTION_POINTER_DOWN -> {
                mode = MODE_ZOOM
                /** 计算两个手指间的距离  */
                startDis = distance(event)
                /** 计算两个手指间的中间点  */
                if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                    midPoint = mid(event)
                    //记录当前ImageView的缩放倍数
                    currentMatrix.set(mImageView!!.imageMatrix)
                }
            }
        }
        matrix.getValues(tempValue)
        if (tempValue[0] <= matrixValue[0]) {
            isOriginal = true
            matrix.setValues(matrixValue)
            mImageView?.imageMatrix = matrix
        } else {
            isOriginal = false
        }
        if (!isOriginal) {
            mImageView?.imageMatrix = matrix
        }

        return gestureDetector!!.onTouchEvent(event)
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
                ll_bottom_read_seekBar.visibility = View.VISIBLE
                ll_bottom_read_seekBar.bringToFront()
                dProgress = true
                hideView()
            }
            R.id.tv_setting -> {
                ll_bottom_read_setting.visibility = View.VISIBLE
                ll_bottom_read_setting.bringToFront()
                ll_bottom_font.visibility = View.INVISIBLE
                dSetting = true
                hideView()
            }
        }
    }

    override fun initComponents(view: View?, savedInstanceState: Bundle?) {
        super.initComponents(view, savedInstanceState)
        // val bookInfo = arguments.getSerializable("bookInfo") as BookInfo

        iv_back.setOnClickListener(this)
        tv_add_mark.setOnClickListener(this)
        tv_catalog.setOnClickListener(this)
        tv_rate.setOnClickListener(this)
        tv_setting.setOnClickListener(this)

        sb_rate.setOnSeekBarChangeListener(this)
        sb_light_degree.setOnSeekBarChangeListener(this)
        rg_read_bg.setOnCheckedChangeListener(this)
        tv_book_title.text = bookInfo.name
        mImageView = view?.findViewById(R.id.iv_pdf_read) as ImageView
        path = bookInfo.path
        ChapterFactory.init(bookInfo)
        beginIndex = arguments.getInt("indexPdf")
        mImageView!!.setOnTouchListener(this)
        //  mImageView!!.setOnLongClickListener(this)
        touchSlop = ViewConfiguration.get(this.activity).scaledTouchSlop
        gestureDetector = GestureDetector(this.activity, MoveGestureListener())
        mPageIndex = bookInfo.indexBegin
        setCheckedId()
        if (null != savedInstanceState) {
            mPageIndex = savedInstanceState.getInt(STATE_CURRENT_PAGE_INDEX, 0)
        }
        mImageView!!.viewTreeObserver.addOnGlobalLayoutListener {
            /*监听组件是否布局完成
            * 1.完成时才能得到长和宽，否则得到的为零
            * 2.改变图片填充方式，若在这之前则会覆盖原本的填充方式
            * 3.获取图片的初始数组
            * */
            mImageView!!.scaleType = ImageView.ScaleType.MATRIX
            originalMatrix = mImageView!!.imageMatrix
            originalMatrix.getValues(matrixValue)
        }
    }

    override fun onStart() {
        super.onStart()
        try {
            openRenderer()
            getIndexFromDB()
            showPage(mPageIndex)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(activity, "Error! " + e.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onPause() {
        super.onPause()
        putIndexToDB(currentIndex)
    }
    override fun onStop() {

        try {
            closeRenderer()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (null != mCurrentPage) {
            outState.putInt(STATE_CURRENT_PAGE_INDEX, mCurrentPage!!.index)
        }
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

    private fun openRenderer() {

        val file = File(path)
        mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)

        if (mFileDescriptor != null) {
            mPdfRenderer = PdfRenderer(mFileDescriptor!!)
            pageCount = mPdfRenderer!!.pageCount - 1
            sb_rate.max = pageCount

        }
    }

    private fun closeRenderer() {
        if (null != mCurrentPage) {
            mCurrentPage!!.close()
        }
        mPdfRenderer!!.close()
        mFileDescriptor!!.close()
    }

    private fun showPage(index: Int) {
        if (mPdfRenderer!!.pageCount <= index || index < 0) {
            return
        }
        if (null != mCurrentPage) {
            try {
                mCurrentPage!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        mCurrentPage = mPdfRenderer!!.openPage(index)

        currentIndex = index
        val bitmap = Bitmap.createBitmap(mCurrentPage!!.width, mCurrentPage!!.height,
                Bitmap.Config.ARGB_8888)
        mCurrentPage!!.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        mImageView!!.scaleType = ImageView.ScaleType.FIT_CENTER
        AppLog.logDebug("test", "${mCurrentPage!!.index} : \t$index")
        mImageView!!.setImageBitmap(bitmap)


        updateState()
    }


    private fun updateState() {
        sb_rate.progress = currentIndex
        tv_done_percent.text = "$currentIndex / $pageCount"

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
        beginIndex = index
        mPresenter.addIndexToDB(bookInfo.path, beginIndex, pageCount)
    }

    /**
     * 使用勾股定理返回两点之间的距离  */
    private fun distance(event: MotionEvent): Float = mPresenter.distance(event)

    /**
     * 计算两个手指间的中间点
     */
    private fun mid(event: MotionEvent): PointF = mPresenter.mid(event)

    private fun getIndexFromDB() {
        mPageIndex = mPresenter.getIndexFromDB(bookInfo.path)
    }

    private inner class MoveGestureListener : GestureDetector.SimpleOnGestureListener() {
        private var moveX: Float = 0.toFloat()

        override fun onDown(e: MotionEvent): Boolean {
            moveX = e.x
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            if (Math.abs(e.x - moveX) <= touchSlop) {
                //mImageView?.performClick()
            }
            return true
        }

        override fun onLongPress(e: MotionEvent?) {

        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            if (isOriginal) {
                if (e2.x - e1.x > 0 && Math.abs(e2.x - e1.x) > touchSlop) {
                    showPage(mCurrentPage!!.index - 1)
                    return true
                }
                if (e2.x - e1.x < 0 && Math.abs(e2.x - e1.x) > touchSlop) {
                    showPage(mCurrentPage!!.index + 1)

                    return true

                }
            }
            return false
        }
    }
}
