package com.aqrlei.graduation.yueting.ui

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.RadioGroup
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.ui.view.AlphaListView
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrairsigns.aqrleilib.util.DensityUtil
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.constant.YueTingConstant.CATALOG_RES
import com.aqrlei.graduation.yueting.model.ChapterInfo
import com.aqrlei.graduation.yueting.presenter.CatalogPresenter
import com.aqrlei.graduation.yueting.ui.adapter.YueTingCatalogListAdapter
import com.aqrlei.graduation.yueting.util.ChapterLoader
import com.aqrlei.graduation.yueting.util.createPopView
import kotlinx.android.synthetic.main.read_activity_catalog.*

/**
 * Author : AqrLei
 * Date : 2017/11/17.
 */
class CatalogActivity : MvpContract.MvpActivity<CatalogPresenter>(),
        View.OnClickListener,
        RadioGroup.OnCheckedChangeListener,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {
    companion object {
        fun jumpToCatalogActivity(context: Activity, reqCode: Int) {
            val intent = Intent(context, CatalogActivity::class.java)
            if (IntentUtil.queryActivities(context, intent))
                context.startActivityForResult(intent, reqCode)
        }
    }

    override val mPresenter: CatalogPresenter
        get() = CatalogPresenter(this)
    override val layoutRes: Int
        get() = R.layout.read_activity_catalog
    private val mDataInfoS = ArrayList<ChapterInfo>()
    private lateinit var mAdapter: YueTingCatalogListAdapter
    private val progressDialog: Dialog
            by lazy {
                createPopView(this, R.layout.common_progress_bar, Gravity.CENTER).apply {
                    setCancelable(false)
                }
            }
    private var markPosition: Int = 0
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backIv -> {
                this.finish()
            }
        }
    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        if (!mDataInfoS[position].flag) {
            markPosition = position
            showDialog()
        }
        return true
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val intent = Intent().putExtra(YueTingConstant.READ_BOOK_POSITION, mDataInfoS[position].bPosition)
        setResult(CATALOG_RES, intent)
        finish()
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.rb_catalog_title -> {
                dataChange(ChapterLoader.CHAPTER.getChapters())
            }
            R.id.rb_bookMark_title -> {
                dataChange(ChapterLoader.CHAPTER.getBookMarks())
            }
        }
    }


    override fun onStop() {
        super.onStop()
        /**
         * 退出目录,清除所有旧数据，避免与新数据混淆
         */
        ChapterLoader.CHAPTER.clearAllDatas()
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        progressDialog.show()
        getData()
        rg_read_catalog.setOnCheckedChangeListener(this)
        backIv.setOnClickListener(this)
    }

    fun loadCatalogDone(t: Boolean) {
        progressDialog.dismiss()
        AppToast.toastShow(this, "目录加载${if (t) "成功" else "失败"}")
        initView()
    }

    private fun getData() {
        mPresenter.getChapter()
    }

    private fun dataChange(data: ArrayList<ChapterInfo>) {
        mDataInfoS.clear()
        mDataInfoS.addAll(data)
        mAdapter.notifyDataSetInvalidated()
    }

    private fun initView() {
        val mView = findViewById<AlphaListView>(R.id.lv_catalog)
        mDataInfoS.addAll(ChapterLoader.CHAPTER.getChapters())
        mAdapter = YueTingCatalogListAdapter(mDataInfoS, this,
                R.layout.read_list_item_catelog)
        mView.adapter = mAdapter
        mView.onItemClickListener = this
        mView.onItemLongClickListener = this
    }

    private fun showDialog() {
        val dialog = Dialog(this, R.style.BottomDialog)
        dialog.setContentView(R.layout.read_pop_view_item)
        dialog.window.decorView.findViewById<View>(R.id.deleteItemTv).setOnClickListener({
            ChapterLoader.CHAPTER.removeBookMark(markPosition)
            dataChange(ChapterLoader.CHAPTER.getBookMarks())
            dialog.dismiss()
        })
        dialog.window.setGravity(Gravity.BOTTOM)
        dialog.window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT,
                DensityUtil.dipToPx(this, 50f))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }
}