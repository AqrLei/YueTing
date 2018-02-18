package com.aqrlei.graduation.yueting.ui.fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.support.v4.app.Fragment
import android.util.DisplayMetrics
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.model.local.BookInfo

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2018/2/12.
 */

class PdfRendererFragment : Fragment(), View.OnTouchListener, View.OnClickListener {
    private val STATE_CURRENT_PAGE_INDEX = "current_page_index"

    private var mFileDescriptor: ParcelFileDescriptor? = null

    private var mPdfRenderer: PdfRenderer? = null

    private var mCurrentPage: PdfRenderer.Page? = null

    private var mImageView: ImageView? = null
    private var mTvTop: TextView? = null
    private var mTvBottom: TextView? = null

    private var mPageIndex: Int = 0
    private var touchSlop: Int = 0
    private var width: Int = 0
    private var height: Int = 0

    private var path: String = ""
    private var begin: Int = 0

    private var gestureDetector: GestureDetector? = null

    val pageCount: Int
        get() = mPdfRenderer!!.pageCount


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pdf_renderer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mImageView = view.findViewById(R.id.iv_pdf_read) as ImageView
        mTvBottom = view.findViewById(R.id.tv_pdf_bottom) as TextView
        mTvTop = view.findViewById(R.id.tv_pdf_top) as TextView
        val wm = this.activity.windowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        width = dm.widthPixels
        height = dm.heightPixels

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
    }

    override fun onStart() {
        super.onStart()
        try {
            openRenderer(activity)
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


    private fun openRenderer(context: Context) {
        /*  val sd = Environment.getExternalStorageDirectory()
          val path = sd.path + "/Android.pdf"*/

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
        // bitmap = bitmapZoom(bitmap, (float) width, (float) height);

        mCurrentPage!!.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        Log.d("test", "w:\t" + bitmap.width + "\n h:\t" + bitmap.height)
        Log.d("test", "W:\t" + width + "H:\t" + height)

        mImageView!!.setImageBitmap(bitmap)

        updateUi()
    }


    private fun updateUi() {
        val index = mCurrentPage!!.index
        val pageCount = mPdfRenderer!!.pageCount

    }

    private fun bitmapZoom(bitmap: Bitmap, newWidth: Float, newHeight: Float): Bitmap {
        val oldWidth = bitmap.width
        val oldHeight = bitmap.height
        val scaleWidth = newWidth / oldWidth
        val scaleHeight = newHeight / oldHeight
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, oldWidth, oldHeight, matrix, true)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector!!.onTouchEvent(event)
    }

    override fun onClick(v: View) {

        mTvTop!!.visibility = if (mTvTop!!.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
        mTvTop!!.bringToFront()
        mTvBottom!!.visibility = if (mTvBottom!!.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
        mTvBottom!!.bringToFront()
    }

    private inner class MoveGestureListener : GestureDetector.SimpleOnGestureListener() {
        private var moveX: Float = 0.toFloat()

        override fun onDown(e: MotionEvent): Boolean {
            //mImageView.performClick();
            moveX = e.x
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            if (Math.abs(e.x - moveX) <= touchSlop) {
                mImageView!!.performClick()
            }
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            if (e2.x - e1.x > 0 && Math.abs(e2.x - e1.x) > touchSlop) {
                showPage(mCurrentPage!!.index - 1)
                return true
            }
            if (e2.x - e1.x < 0 && Math.abs(e2.x - e1.x) > touchSlop) {
                showPage(mCurrentPage!!.index + 1)

                return true

            }
            return false
        }
    }

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
}
