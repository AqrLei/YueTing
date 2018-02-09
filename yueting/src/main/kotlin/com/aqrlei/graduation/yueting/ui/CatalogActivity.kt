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
import com.aqrlei.graduation.yueting.factory.PageFactory
import com.aqrlei.graduation.yueting.model.local.infotool.ShareBookInfo
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
        // ReadActivity.jumpToReadActivity(this,ShareBookInfo.BookInfoTool.getInfo())
        val intent = Intent().putExtra("bPosition", chapterInfoS[position].bPosition)
        setResult(2, intent)
        AppLog.logDebug("test", "catalog test")
        finish()
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
    private val chapterInfoS = ChapterFactory.CHAPTER.getChapters()

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        rg_read_catalog.setOnCheckedChangeListener(this)
        initView()
    }

    private fun initView() {
        val mView = findViewById(R.id.lv_catalog) as AlphaListView
        mView.adapter = YueTingCatalogListAdapter(
                chapterInfoS,
                this,
                R.layout.catelog_bookmark_item, true)
        mView.onItemClickListener = this
    }



}