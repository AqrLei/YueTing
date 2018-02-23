package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrlei.graduation.yueting.R
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
    private val PDF_READ_TAG = "pdf_read_tag"
    private val REQUESTCODE = 3
    private val bookInfo: BookInfo
        get() = intent.extras.getSerializable("bookInfo") as BookInfo
    private lateinit var fragment: PdfRendererFragment
    override val layoutRes: Int
        get() = R.layout.activity_pdf_read
    override val mPresenter: PdfReadActivityPresenter
        get() = PdfReadActivityPresenter(this)

    override fun onBackPressed() {
        if (!fragment.onBackPressed()) {
            super.onBackPressed()
        }
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

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        fragment = PdfRendererFragment.newInstance(
                bookInfo)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.fl_container, fragment,
                    PDF_READ_TAG)
                    .commit()
        }
    }

    private fun addIndexToDB(index: Int) {
        fragment.putIndexToDB(index)
    }

    fun getCatalog() {
        mPresenter.getCatalog()
    }

    fun jumpToCatalog(flag: Boolean) {
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