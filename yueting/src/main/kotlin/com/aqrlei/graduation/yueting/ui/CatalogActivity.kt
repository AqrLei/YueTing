package com.aqrlei.graduation.yueting.ui

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.RadioGroup
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.ui.view.AlphaListView
import com.aqrairsigns.aqrleilib.util.DensityUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.YueTingConstant.CATALOGRECODE
import com.aqrlei.graduation.yueting.factory.ChapterFactory
import com.aqrlei.graduation.yueting.model.local.ChapterInfo
import com.aqrlei.graduation.yueting.presenter.activitypresenter.CatalogActivityPresenter
import com.aqrlei.graduation.yueting.ui.adapter.YueTingCatalogListAdapter
import kotlinx.android.synthetic.main.read_activity_catalog.*

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/11/17.
 */
class CatalogActivity : MvpContract.MvpActivity<CatalogActivityPresenter>(),
        View.OnClickListener,
        RadioGroup.OnCheckedChangeListener,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_remove_items -> {

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
        val intent = Intent().putExtra("bPosition", mDataInfoS[position].bPosition)
        setResult(CATALOGRECODE, intent)
        finish()
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.rb_catalog_title -> {

                dataChange(ChapterFactory.CHAPTER.getChapters())
            }
            R.id.rb_bookMark_title -> {
                dataChange(ChapterFactory.CHAPTER.getBookMarks())
            }
        }
    }


    override val mPresenter: CatalogActivityPresenter
        get() = CatalogActivityPresenter(this)
    override val layoutRes: Int
        get() = R.layout.read_activity_catalog
    private val mDataInfoS = ArrayList<ChapterInfo>()
    private lateinit var mAdapter: YueTingCatalogListAdapter
    private lateinit var mProgressDialog: ProgressDialog
    private var markPosition: Int = 0

    override fun onStop() {
        super.onStop()
        /**
         * 退出目录,清除所有旧数据，避免与新数据混淆
         */
        ChapterFactory.CHAPTER.clearAllDatas()
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        setProgressDialog()
        getData()
        rg_read_catalog.setOnCheckedChangeListener(this)

    }

    fun loadCatalogDone(t: Boolean) {
        mProgressDialog.dismiss()
        initView()
    }

    private fun setProgressDialog() {
        mProgressDialog = ProgressDialog(this)
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.setTitle("提示")
        mProgressDialog.setMessage("正在加载中~")
        mProgressDialog.show()
    }

    private fun getData() {
        mPresenter.getData()
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
                R.layout.read_list_item_catelog)
        mView.adapter = mAdapter
        mView.onItemClickListener = this
        mView.onItemLongClickListener = this

    }


    private fun showDialog() {
        val dialog = Dialog(this, R.style.BottomDialog)
        dialog.setContentView(R.layout.manage_dialog_bottom)
        dialog.window.decorView.findViewById(R.id.tv_remove_items).setOnClickListener({
            ChapterFactory.CHAPTER.removeBookMark(markPosition)
            dataChange(ChapterFactory.CHAPTER.getBookMarks())
            dialog.dismiss()
        })
        dialog.window.setGravity(Gravity.BOTTOM)
        dialog.window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT,
                DensityUtil.dipToPx(this, 50f))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }


}