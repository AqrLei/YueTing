package com.aqrlei.graduation.yueting.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.adapter.CommonListAdapter
import com.aqrlei.graduation.yueting.basemvp.MvpContract
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.SelectInfo
import com.aqrlei.graduation.yueting.presenter.ManageListPresenter
import com.aqrlei.graduation.yueting.ui.adapter.SelectAdapter
import com.aqrlei.graduation.yueting.util.AppToast
import com.aqrlei.graduation.yueting.util.IntentUtil
import kotlinx.android.synthetic.main.common_activity_manage.*

/**
 * @author  aqrLei on 2018/5/2
 */
class ManageListActivity :
        MvpContract.MvpActivity<ManageListPresenter>(),
        View.OnClickListener,
        CommonListAdapter.OnInternalClick {
    companion object {
        fun jumpToManageListActivity(
                context: Activity,
                type: String,
                typeItem: String,
                data: ArrayList<SelectInfo>,
                reqCode: Int) {
            val intent = Intent(context, ManageListActivity::class.java).apply {
                val bundle = Bundle()
                bundle.putString(YueTingConstant.MANAGE_TYPE_KEY, type)
                bundle.putString(YueTingConstant.FRAGMENT_TITLE_TYPE, typeItem)
                bundle.putSerializable(YueTingConstant.MANAGE_DATA, data)
                putExtras(bundle)
            }
            if (IntentUtil.queryActivities(context, intent)) {
                context.startActivityForResult(intent, reqCode)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private val selectInfoList: ArrayList<SelectInfo>
            by lazy {
                intent.extras.getSerializable(YueTingConstant.MANAGE_DATA) as ArrayList<SelectInfo>
            }
    private val mAdapter: SelectAdapter
            by lazy {
                SelectAdapter(
                        data = selectInfoList,
                        context = this,
                        type = type,
                        listener = this)
            }
    private val type: String
            by lazy { intent.extras.getString(YueTingConstant.MANAGE_TYPE_KEY) }
    private val typeItem: String
            by lazy {
                intent.extras.getString(YueTingConstant.FRAGMENT_TITLE_TYPE)
            }
    override val mPresenter: ManageListPresenter
        get() = ManageListPresenter(this)
    override val layoutRes: Int
        get() = R.layout.common_activity_manage

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backIv -> {
                this.finish()
            }
            R.id.selectIv -> {
                selectIv.background.level = if (selectIv.background.level == 0) {
                    selectAll(true)
                    1
                } else {
                    selectAll(false)
                    0
                }
            }
            R.id.deleteTv -> {
                mPresenter.deleteItem(type, typeItem, selectInfoList)

            }
        }
    }

    override fun onInternalClick(v: View, position: Int) {
        val iv = v as ImageView
        iv.background.level = if (iv.background.level == 1) {
            if (selectIv.background.level == 1) {
                selectIv.background.level = 0
            }
            selectInfoList[position].status = SelectInfo.UNSELECTED
            0
        } else {
            selectInfoList[position].status = SelectInfo.SELECTED
            1
        }
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        backIv.setOnClickListener(this)
        selectIv.setOnClickListener(this)
        deleteTv.setOnClickListener(this)
        mAdapter
        selectLv.adapter = mAdapter
        manageNameTv.text = initTitle()
    }

    fun deleteFinished(msg: String, isDelete: Boolean) {
        AppToast.toastShow(this, msg)
        if (isDelete) {
            this.finish()
        }
    }

    private fun selectAll(isSelect: Boolean) {
        selectInfoList.forEach {
            if (isSelect) {
                it.status = SelectInfo.SELECTED
            } else {
                it.status = SelectInfo.UNSELECTED
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    private fun initTitle(): String {
        return if (type == YueTingConstant.MANAGE_TYPE_LIST) {
            if (typeItem == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK) {
                "管理书单"
            } else {
                "管理歌单"
            }
        } else {
            if (typeItem == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK) {
                "管理书籍"
            } else {
                "管理歌曲"
            }
        }
    }
}