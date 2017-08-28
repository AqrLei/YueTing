package com.aqrlei.graduation.truckrental.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.widget.RadioGroup
import com.aqrlei.graduation.truckrental.R
import com.aqrlei.graduation.truckrental.baselib.mvp.MvpContract
import com.aqrlei.graduation.truckrental.baselib.util.AppConstant
import com.aqrlei.graduation.truckrental.baselib.util.IntentUtil
import com.aqrlei.graduation.truckrental.presenter.activitypresenter.YueTingActivityPresenter
import kotlinx.android.synthetic.main.activity_animation.*

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/23
 */
/*
* @param mPresenter 访问对应的Presenter
* */
class YueTingActivity : MvpContract.MvpActivity<YueTingActivityPresenter>()
        , RadioGroup.OnCheckedChangeListener {
    private var mFragments = ArrayList<Fragment>()
    private lateinit var mFragmentManager: FragmentManager
    override fun onCheckedChanged(radioGroup: RadioGroup?, checkedId: Int) {
        (0 until radioGroup!!.childCount)
                .filter { radioGroup.getChildAt(it).id == checkedId }
                .forEach { mPresenter.changeFragment(it, mFragmentManager, mFragments) }
    }

    override val mPresenter: YueTingActivityPresenter
        get() = YueTingActivityPresenter(this)
    override val layoutRes: Int
        get() = R.layout.activity_animation

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        mFragmentManager = supportFragmentManager
        mFragments = mPresenter.initFragments(savedInstanceState, mFragmentManager)
        mPresenter.changeFragment(AppConstant.TAG_FRAGMENT_HOME, mFragmentManager, mFragments)
        rg_anim_tab.setOnCheckedChangeListener(this)
    }

    companion object {
        fun jumpToAnimationActivity(context: Context, data: Int) {
            val intent = Intent(context, YueTingActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("code", data)
            if (IntentUtil.queryActivities(context, intent)) context.startActivity(intent)
        }
    }
}