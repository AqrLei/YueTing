package com.aqrlei.graduation.yueting.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.TextView
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.SelectInfo
import com.aqrlei.graduation.yueting.presenter.TitlePresenter
import com.aqrlei.graduation.yueting.ui.FileActivity
import com.aqrlei.graduation.yueting.ui.ManageListActivity
import com.aqrlei.graduation.yueting.ui.YueTingActivity
import com.aqrlei.graduation.yueting.ui.YueTingListActivity
import com.aqrlei.graduation.yueting.ui.adapter.YueTingListAdapter
import com.aqrlei.graduation.yueting.ui.uiEt.createPopView
import kotlinx.android.synthetic.main.main_title_fragment_list.*

/**
 * created by AqrLei on 2018/4/23
 */
class TitleFragment : MvpContract.MvpFragment<TitlePresenter, YueTingListActivity>(),
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        View.OnClickListener {
    companion object {
        fun newInstance(type: String): TitleFragment {
            return TitleFragment().apply {
                val args = Bundle()
                args.putString(YueTingConstant.FRAGMENT_TITLE_TYPE, type)
                arguments = args
            }
        }
    }

    override val mPresenter: TitlePresenter
        get() = TitlePresenter(this)
    override val layoutRes: Int
        get() = R.layout.main_title_fragment_list
    private val bottomDialog: Dialog
            by lazy {
                createPopView(mContainerActivity, R.layout.manage_pop_view_list).apply {
                    window.decorView?.apply {
                        findViewById(R.id.newListTv)?.also {
                            (it as TextView).text =
                                    if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_MUSIC) "新建歌单"
                                    else "新建书单"
                            it.setOnClickListener(this@TitleFragment)
                        }
                        findViewById(R.id.manageListsTv)
                                ?.also {
                                    (it as TextView).text =
                                            if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_MUSIC) "管理歌单"
                                            else "管理书单"
                                    it.setOnClickListener(this@TitleFragment)
                                }
                        findViewById(R.id.addItems)?.also {
                            (it as TextView).text =
                                    if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_MUSIC) "添加歌曲"
                                    else "添加书籍"
                            it.setOnClickListener(this@TitleFragment)
                        }
                    }
                }
            }
    private val modifyListDialog: Dialog
            by lazy {
                createPopView(mContainerActivity, R.layout.manage_new_list, Gravity.CENTER).apply {
                    window.decorView?.apply {
                        findViewById(R.id.sureTv).setOnClickListener(this@TitleFragment)
                        findViewById(R.id.cancelTv).setOnClickListener(this@TitleFragment)
                    }

                }
            }
    private val type: String
        get() = arguments.getString(YueTingConstant.FRAGMENT_TITLE_TYPE)
    private val titleList: ArrayList<String>
            by lazy {
                ArrayList<String>().apply {
                    add(DataConstant.DEFAULT_TYPE_NAME)
                }
            }
    private val mAdapter: YueTingListAdapter
            by lazy {
                if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_MUSIC) {
                    YueTingListAdapter(
                            titleList,
                            mContainerActivity,
                            R.layout.title_list_item,
                            YueTingConstant.ADAPTER_TYPE_TITLE_MUSIC
                    )
                } else {
                    YueTingListAdapter(
                            titleList,
                            mContainerActivity,
                            R.layout.title_list_item,
                            YueTingConstant.ADAPTER_TYPE_TITLE_BOOK
                    )
                }
            }
    private var isNewList: Boolean = true

    private lateinit var oldName: String
    override fun onClick(v: View) {
        when (v.id) {
            R.id.listTitleTv -> {
                bottomDialog.show()
            }
            R.id.manageListsTv -> {
                val selectInfoList = ArrayList<SelectInfo>()
                titleList.filter { it != DataConstant.DEFAULT_TYPE_NAME }
                        .forEach {
                            selectInfoList.add(SelectInfo(name = it))
                        }
                ManageListActivity.jumpToManageListActivity(
                        mContainerActivity,
                        YueTingConstant.MANAGE_TYPE_LIST,
                        type,
                        selectInfoList,
                        YueTingConstant.MANAGE_REQ)
                bottomDialog.dismiss()
            }
            R.id.addItems -> {
                FileActivity.jumpToFileActivity(
                        mContainerActivity,
                        0,
                        type,
                        titleList[0],
                        YueTingConstant.YUE_TING_LIST_FILE)
                bottomDialog.dismiss()
            }
            R.id.newListTv -> {
                bottomDialog.dismiss()
                modifyListDialog.show()
            }
            R.id.sureTv -> {
                val name = (modifyListDialog.window.decorView
                        .findViewById(R.id.listNameEt) as EditText)
                        .text.toString()
                verifyListTitle(name)
            }
            R.id.cancelTv -> {
                modifyListDialog.dismiss()
            }
        }


    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        YueTingActivity.jumpToYueTingActivity(mContainerActivity, type, titleList[position])
    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        if (position == 0) {
            return true
        }
        modifyListDialog.apply {
            (window.decorView.findViewById(R.id.listNameEt) as EditText).hint = "请输入新名称"
            show()
        }
        oldName = titleList[position]
        isNewList = false
        return true
    }

    override fun initComponents(view: View?, savedInstanceState: Bundle?) {
        super.initComponents(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.getTypeInfo(type)
    }

    fun modifyFinish(boolean: Boolean,msg:String) {
        if (boolean) {
            AppToast.toastShow(mContainerActivity, msg, 1000)
            modifyListDialog.dismiss()
            mPresenter.getTypeInfo(type)
        }
    }

    fun setTitleList(temp: ArrayList<String>) {
        titleList.clear()
        titleList.add(DataConstant.DEFAULT_TYPE_NAME)
        titleList.addAll(temp)
        mAdapter.notifyDataSetChanged()
    }

    private fun verifyListTitle(name: String) {
        if (name.isEmpty()) {
            AppToast.toastShow(mContainerActivity, "名字不可为空", 1000)
            return
        }
        if (isNewList) {
            mPresenter.modifyTypeName(
                    isNew = isNewList,
                    type = type,
                    name = name)
        } else {
            mPresenter.modifyTypeName(
                    isNew = isNewList,
                    type = type,
                    name = oldName,
                    newName = name)
        }

    }


    private fun initView() {
        typeLv.adapter = mAdapter
        typeLv.onItemClickListener = this
        typeLv.onItemLongClickListener = this

        listTitleTv.setOnClickListener(this)
        bgIv.background.level =
                if (type == YueTingConstant.FRAGMENT_TITLE_TYPE_MUSIC) {
                    listTitleTv.text = "创建的歌单"
                    YueTingConstant.TITLE_TYPE_MUSIC
                } else {
                    listTitleTv.text = "创建的书单"
                    YueTingConstant.TITLE_TYPE_BOOK
                }
    }
}