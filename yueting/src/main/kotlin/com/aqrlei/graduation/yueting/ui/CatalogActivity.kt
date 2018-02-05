package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.AppLog
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.presenter.activitypresenter.CatalogActivityPresenter
import kotlinx.android.synthetic.main.activity_catalog.*

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/11/17.
 */
class CatalogActivity : MvpContract.MvpActivity<CatalogActivityPresenter>(),
        RadioGroup.OnCheckedChangeListener {
    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.rb_catalog_title -> {
                AppLog.logDebug("test",
                        (findViewById(R.id.rb_catalog_title) as RadioButton).text.toString())
            }
            R.id.rb_bookMark_title -> {
                AppLog.logDebug("test",
                        (findViewById(R.id.rb_bookMark_title) as RadioButton).text.toString())
            }
        }
    }


    override val mPresenter: CatalogActivityPresenter
        get() = CatalogActivityPresenter(this)
    override val layoutRes: Int
        get() = R.layout.activity_catalog

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        rg_read_catalog.setOnCheckedChangeListener(this)
    }


    companion object {
        fun jumpToCatalogActivity(context: Context, data: String) {
            val intent = Intent(context, CatalogActivity::class.java)
            /* val bundle = Bundle()
             bundle.putSerializable("bookInfo",data)*/
            intent.putExtra("bookInfo", data)
            if (IntentUtil.queryActivities(context, intent)) context.startActivity(intent)
        }

    }
}