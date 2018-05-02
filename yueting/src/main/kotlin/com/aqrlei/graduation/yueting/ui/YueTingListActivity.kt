package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.KeyEvent
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.ActivityCollector
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.presenter.YueTingListPresenter
import com.aqrlei.graduation.yueting.ui.adapter.TitlePagerAdapter
import com.aqrlei.graduation.yueting.ui.fragment.TitleFragment
import kotlinx.android.synthetic.main.main_activity_yueting_list.*

/**
 * created by AqrLei on 2018/4/22
 */
class YueTingListActivity : MvpContract.MvpActivity<YueTingListPresenter>() {
    companion object {
        fun jumpToYueTingListActivity(context: Context) {
            val intent = Intent(context, YueTingListActivity::class.java)
            if (IntentUtil.queryActivities(context, intent)) context.startActivity(intent)
        }
    }

    override val mPresenter: YueTingListPresenter
        get() = YueTingListPresenter(this)
    override val layoutRes: Int
        get() = R.layout.main_activity_yueting_list

    private val bookFragment: TitleFragment
            by lazy {
                TitleFragment.newInstance(YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK)
            }
    private val musicFragment: TitleFragment
            by lazy {
                TitleFragment.newInstance(YueTingConstant.FRAGMENT_TITLE_TYPE_MUSIC)
            }
    private val mAdapter: TitlePagerAdapter
            by lazy {
                TitlePagerAdapter(supportFragmentManager, ArrayList<Fragment>().apply {
                    add(bookFragment)
                    add(musicFragment)
                })
            }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder(this).apply {
                setTitle("提示")
                setMessage("确定退出应用吗？")
                setPositiveButton("是", { _, _ ->
                    ActivityCollector.killApp()
                })
                setNegativeButton("否", null)
                show()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        contentVp.adapter = mAdapter
        contentVp.currentItem = 0
        contentVp.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(titleTl))

        titleTl.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(contentVp))
    }

}