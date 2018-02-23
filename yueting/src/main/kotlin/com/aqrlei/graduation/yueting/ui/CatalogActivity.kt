package com.aqrlei.graduation.yueting.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.RadioButton
import android.widget.RadioGroup
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.ui.view.AlphaListView
import com.aqrairsigns.aqrleilib.util.AppLog
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.factory.ChapterFactory
import com.aqrlei.graduation.yueting.model.local.ChapterInfo
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
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {
    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        if (!mDataInfoS[position].flag) {
            ChapterFactory.CHAPTER.removeBookMark(position)
            dataChange(ChapterFactory.CHAPTER.getBookMarks())
        }
        return true
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // TxtReadActivity.jumpToTxtReadActivity(this,ShareBookInfo.BookInfoTool.getInfo())
        val intent = Intent().putExtra("bPosition", mDataInfoS[position].bPosition)
        setResult(2, intent)
        AppLog.logDebug("test", "catalog test")
        finish()
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.rb_catalog_title -> {

                dataChange(ChapterFactory.CHAPTER.getChapters())
                AppLog.logDebug("test",
                        (findViewById(R.id.rb_catalog_title) as RadioButton).text.toString())
            }
            R.id.rb_bookMark_title -> {
                dataChange(ChapterFactory.CHAPTER.getBookMarks())
                AppLog.logDebug("test",
                        (findViewById(R.id.rb_bookMark_title) as RadioButton).text.toString())
            }
        }
    }


    override val mPresenter: CatalogActivityPresenter
        get() = CatalogActivityPresenter(this)
    override val layoutRes: Int
        get() = R.layout.activity_catalog
    private val mDataInfoS = ArrayList<ChapterInfo>()
    private lateinit var mAdapter: YueTingCatalogListAdapter
    override fun onStop() {
        super.onStop()
        /**
         * 退出目录,清除所有旧数据，避免与新数据混淆
         */
        ChapterFactory.CHAPTER.clearAllDatas()
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        rg_read_catalog.setOnCheckedChangeListener(this)
        initView()
    }

    private fun dataChange(data: ArrayList<ChapterInfo>) {
        mDataInfoS.clear()
        mDataInfoS.addAll(data)
        mAdapter.notifyDataSetInvalidated()
    }

    private fun initView() {
        val mView = findViewById(R.id.lv_catalog) as AlphaListView
        mDataInfoS.addAll(ChapterFactory.CHAPTER.getChapters())
        mAdapter = YueTingCatalogListAdapter(mDataInfoS, this,
                R.layout.catelog_bookmark_item)
        mView.adapter = mAdapter
        mView.onItemClickListener = this
        mView.onItemLongClickListener = this

    }


}