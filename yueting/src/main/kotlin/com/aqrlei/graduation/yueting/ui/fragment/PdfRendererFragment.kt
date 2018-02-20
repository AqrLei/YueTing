package com.aqrlei.graduation.yueting.ui.fragment

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.model.local.BookInfo
import com.aqrlei.graduation.yueting.presenter.fragmentpresenter.PdfRendererPresenter
import com.aqrlei.graduation.yueting.ui.PdfReadActivity
import java.io.File
import java.io.IOException

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2018/2/12.
 */

class PdfRendererFragment : MvpContract.MvpFragment<PdfRendererPresenter, PdfReadActivity>(),
        View.OnTouchListener, View.OnClickListener {
    override val layoutRes: Int
        get() = R.layout.fragment_pdf_renderer
    override val mPresenter: PdfRendererPresenter
        get() = PdfRendererPresenter(this)


    private val STATE_CURRENT_PAGE_INDEX = "current_page_index"
    private var mFileDescriptor: ParcelFileDescriptor? = null
    private var mPdfRenderer: PdfRenderer? = null
    private var mCurrentPage: PdfRenderer.Page? = null

    private var mImageView: ImageView? = null
    private var mTvTop: TextView? = null
    private var mTvBottom: TextView? = null
    private var mPageIndex: Int = 0
    private var touchSlop: Int = 0

    private var path: String = ""
    private var begin: Int = 0
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


    companion object {
        fun newInstance(bookInfo: BookInfo, bPosition: Int): PdfRendererFragment {
            val args = Bundle()
            args.putSerializable("bookInfo", bookInfo)
            args.putInt("bPosition", bPosition)
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

        mTvTop!!.visibility = if (mTvTop!!.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
        mTvTop!!.bringToFront()
        mTvBottom!!.visibility = if (mTvBottom!!.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
        mTvBottom!!.bringToFront()
    }

    override fun initComponents(view: View?, savedInstanceState: Bundle?) {
        super.initComponents(view, savedInstanceState)
        mImageView = view?.findViewById(R.id.iv_pdf_read) as ImageView
        mTvBottom = view.findViewById(R.id.tv_pdf_bottom) as TextView
        mTvTop = view.findViewById(R.id.tv_pdf_top) as TextView
        path = (arguments.getSerializable("bookInfo") as BookInfo).path
        begin = arguments.getInt("bPosition")
        mImageView!!.setOnTouchListener(this)
        mImageView!!.setOnClickListener(this)
        touchSlop = ViewConfiguration.get(this.activity).scaledTouchSlop
        gestureDetector = GestureDetector(this.activity, MoveGestureListener())
        mPageIndex = 0
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
            showPage(mPageIndex)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(activity, "Error! " + e.message, Toast.LENGTH_SHORT).show()
        }

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

    private fun openRenderer() {

        val file = File(path)
        mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)

        if (mFileDescriptor != null) {
            mPdfRenderer = PdfRenderer(mFileDescriptor!!)
        }
    }

    @Throws(IOException::class)
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
            mCurrentPage!!.close()
        }
        mCurrentPage = mPdfRenderer!!.openPage(index)
        val bitmap = Bitmap.createBitmap(mCurrentPage!!.width, mCurrentPage!!.height,
                Bitmap.Config.ARGB_8888)
        mCurrentPage!!.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        mImageView!!.scaleType = ImageView.ScaleType.FIT_CENTER
        mImageView!!.setImageBitmap(bitmap)

        // updateUi()
    }

    private fun updateUi() {
        val index = mCurrentPage!!.index
        val pageCount = mPdfRenderer!!.pageCount

    }

    /**
     * 使用勾股定理返回两点之间的距离  */
    private fun distance(event: MotionEvent): Float = mPresenter.distance(event)

    /**
     * 计算两个手指间的中间点
     */
    private fun mid(event: MotionEvent): PointF = mPresenter.mid(event)


    private inner class MoveGestureListener : GestureDetector.SimpleOnGestureListener() {
        private var moveX: Float = 0.toFloat()

        override fun onDown(e: MotionEvent): Boolean {
            moveX = e.x
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            if (Math.abs(e.x - moveX) <= touchSlop) {
                mImageView?.performClick()
            }
            return true
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
