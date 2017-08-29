package com.aqrlei.graduation.yueting.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.baselib.mvp.MvpContract
import com.aqrlei.graduation.yueting.baselib.util.AppToast
import com.aqrlei.graduation.yueting.baselib.util.view.AlphaListView
import com.aqrlei.graduation.yueting.model.local.ReadMessage
import com.aqrlei.graduation.yueting.presenter.fragmentpresenter.TabReadPresenter
import com.aqrlei.graduation.yueting.ui.YueTingActivity
import com.aqrlei.graduation.yueting.ui.adapter.YueTingReadListAdapter
import kotlinx.android.synthetic.main.activity_yueting.*
import kotlinx.android.synthetic.main.listheader_read.view.*
import kotlinx.android.synthetic.main.yueting_fragment_read.view.*

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/8/26.
 */
class TabReadFragment : MvpContract.MvpFragment<TabReadPresenter, YueTingActivity>()
        , TabLayout.OnTabSelectedListener
        , AlphaListView.OnAlphaChangeListener {


    override val layoutRes: Int
        get() = R.layout.yueting_fragment_read
    override val mPresenter: TabReadPresenter
        get() = TabReadPresenter(this)

    private lateinit var mHeaderView: View
    private var mReadData = ArrayList<ReadMessage>()

    override fun onAlphaChanged(percent: Float) {
        mContainerActivity.rg_anim_tab.setBackgroundColor(
                Color.argb((175 * percent).toInt(), 113, 204, 180)
        )
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        AppToast.toastShow(mContainerActivity,tab?.text.toString(), 1000)
    }


    companion object {
        fun newInstance(): TabReadFragment {
            val args = Bundle()
            val fragment = TabReadFragment()
            fragment.arguments = args
            return fragment

        }
    }

    override fun initComponents() {
        super.initComponents()
        initView()
    }

    private fun initView() {
        var mReadLv = mView.lv_fragment_read
        mHeaderView = LayoutInflater.from(mContainerActivity).inflate(R.layout.listheader_read, null)
        val mTabLayout = mHeaderView.tl_tab_read
        mTabLayout.addTab(mTabLayout.newTab().setText("全部"), true)
        mTabLayout.addTab(mTabLayout.newTab().setText("技术"))
        mTabLayout.addTab(mTabLayout.newTab().setText("文学"))
        mTabLayout.addTab(mTabLayout.newTab().setText("其它"))
        mTabLayout.addOnTabSelectedListener(this)
        mReadLv.addHeaderView(mHeaderView)
        mReadData.add(ReadMessage("这是书名吧1", null))
        mReadData.add(ReadMessage("这是书名吧2", null))
        mReadData.add(ReadMessage("这是书名吧3", null))
        mReadData.add(ReadMessage("这是书名吧4", null))
        mReadData.add(ReadMessage("这是书名吧5", null))
        mReadData.add(ReadMessage("这是书名吧6", null))
        mReadData.add(ReadMessage("这是书名吧7", null))
        mReadLv.adapter = YueTingReadListAdapter(mReadData, mContainerActivity,
                R.layout.listitem_read)
        mReadLv.setAlphaChangeListener(this)


    }

}