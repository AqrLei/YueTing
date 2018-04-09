package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.BookInfo
import com.aqrlei.graduation.yueting.presenter.activitypresenter.PdfReadActivityPresenter
import com.aqrlei.graduation.yueting.ui.fragment.PdfRendererFragment

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2018/2/18.
 */
class PdfReadActivity : MvpContract.MvpActivity<PdfReadActivityPresenter>() {
    private val REQUESTCODE = 3
    private val bookInfo: BookInfo
        get() = intent.extras.getSerializable("bookInfo") as BookInfo
    private lateinit var fragment: PdfRendererFragment
    override val layoutRes: Int
        get() = R.layout.read_activity_pdf
    override val mPresenter: PdfReadActivityPresenter
        get() = PdfReadActivityPresenter(this)

    override fun onBackPressed() {
        if (!fragment.onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_MENU -> {
                fragment.showMenu()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 2) {
            if (requestCode == REQUESTCODE) {
                val index = data?.extras?.getInt("bPosition") ?: 0
                fragment.setCurrentIndex(index)
                addIndexToDB(index)
            }
        }
    }

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
        fragment = if (savedInstanceState != null) {
            (supportFragmentManager
                    .findFragmentByTag(
                            YueTingConstant.TAB_FRAGMENT_READ_PDF)
                    ?: PdfRendererFragment.newInstance(bookInfo)) as PdfRendererFragment
        } else {
            PdfRendererFragment.newInstance(bookInfo)
        }
        /**
         * 避免重复添加的错误
         */
        if (!fragment.isAdded &&
                supportFragmentManager.findFragmentByTag(
                        YueTingConstant.TAB_FRAGMENT_READ_PDF)
                == null) {
            supportFragmentManager.beginTransaction().add(R.id.fl_container, fragment,
                    YueTingConstant.TAB_FRAGMENT_READ_PDF)
                    .commit()
        }
    }

    private fun addIndexToDB(index: Int) {
        fragment.putIndexToDB(index)
    }

    fun jumpToCatalog() {
        startActivityForResult(Intent(this, CatalogActivity::class.java), REQUESTCODE)
    }

    companion object {
        fun jumpToPdfReadActivity(context: Context, data0: BookInfo, data1: Int = 0) {
            val intent = Intent(context, PdfReadActivity::class.java)
            intent.putExtra("bookInfo", data0)
            intent.putExtra("indexPdf", data1)
            if (IntentUtil.queryActivities(context, intent)) context.startActivity(intent)
        }

    }

}