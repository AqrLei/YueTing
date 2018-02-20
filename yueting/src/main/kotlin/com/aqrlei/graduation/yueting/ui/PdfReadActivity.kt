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
    override val layoutRes: Int
        get() = R.layout.activity_pdf_read
    override val mPresenter: PdfReadActivityPresenter
        get() = PdfReadActivityPresenter(this)

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        val fragment = PdfRendererFragment.newInstance(
                intent.extras.getSerializable("bookInfo") as BookInfo,
                intent.extras.getInt("bPosition"))
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.fl_container, fragment,
                    PDF_READ_TAG)
                    .commit()
        }
    }

    companion object {
        fun jumpToPdfReadActivity(context: Context, data0: BookInfo, data1: Int = 0) {
            val intent = Intent(context, PdfReadActivity::class.java)
            intent.putExtra("bookInfo", data0)
            intent.putExtra("bPosition", data1)
            if (IntentUtil.queryActivities(context, intent)) context.startActivity(intent)
        }

    }

}