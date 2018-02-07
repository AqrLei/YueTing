package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.AppLog
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrairsigns.aqrleilib.view.AlphaListView
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.factory.ChapterFactory
import com.aqrlei.graduation.yueting.presenter.activitypresenter.CatalogActivityPresenter
import com.aqrlei.graduation.yueting.ui.adapter.YueTingCatalogListAdapter
import kotlinx.android.synthetic.main.activity_catalog.*

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/11/17.
 */
class CatalogActivity : MvpContract.MvpActivity<CatalogActivityPresenter>(),
        RadioGroup.OnCheckedChangeListener,
        AdapterView.OnItemClickListener {
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        AppLog.logDebug("test", "catalog test")
    }

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
        initView()
    }

    private fun initView() {
        val mView = findViewById(R.id.lv_catalog) as AlphaListView
        mView.adapter = YueTingCatalogListAdapter(
                ChapterFactory.CHAPTER.getChapters(),
                this,
                R.layout.catelog_bookmark_item, true)
        mView.onItemClickListener = this
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