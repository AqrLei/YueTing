package com.aqrlei.graduation.yueting.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.presenter.fragmentpresenter.TitlePresenter
import com.aqrlei.graduation.yueting.ui.YueTingActivity
import com.aqrlei.graduation.yueting.ui.YueTingListActivity
import com.aqrlei.graduation.yueting.ui.adapter.YueTingListAdapter
import kotlinx.android.synthetic.main.main_title_fragment_list.*

/**
 * created by AqrLei on 2018/4/23
 */
class TitleFragment : MvpContract.MvpFragment<TitlePresenter, YueTingListActivity>(),
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {
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

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        YueTingActivity.jumpToYueTingActivity(mContainerActivity, type, titleList[position])
        // AppToast.toastShow(mContainerActivity, titleList[position], 1000)
    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        return true
    }

    override fun initComponents(view: View?, savedInstanceState: Bundle?) {
        super.initComponents(view, savedInstanceState)
        initView()
        mPresenter.getTypeInfoFromDB(type)
    }

    fun setTitleList(temp: ArrayList<String>) {
        titleList.clear()
        titleList.add(DataConstant.DEFAULT_TYPE_NAME)
        titleList.addAll(temp)
        mAdapter.notifyDataSetChanged()
    }

    private fun initView() {
        typeLv.adapter = mAdapter
        typeLv.onItemClickListener = this
        typeLv.onItemLongClickListener = this
    }
}