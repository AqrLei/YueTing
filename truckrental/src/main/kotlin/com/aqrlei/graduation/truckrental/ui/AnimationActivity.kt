package com.aqrlei.graduation.truckrental.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.widget.RadioGroup
import com.aqrlei.graduation.truckrental.R
import com.aqrlei.graduation.truckrental.baselib.mvp.MvpContract
import com.aqrlei.graduation.truckrental.baselib.util.AppConstant
import com.aqrlei.graduation.truckrental.baselib.util.IntentUtil
import com.aqrlei.graduation.truckrental.presenter.activitypresenter.AnimationActivityPresenter
import com.aqrlei.graduation.truckrental.ui.fragment.TabHomeFragment
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
class AnimationActivity : MvpContract.MvpActivity<AnimationActivityPresenter>()
        , RadioGroup.OnCheckedChangeListener {
    override fun onCheckedChanged(radioGroup: RadioGroup?, checkedId: Int) {
        (0 until radioGroup!!.childCount)
                .filter { radioGroup.getChildAt(it).id == checkedId }
                .forEach { mPresenter.changeFragment(it) }
    }

    override val mPresenter: AnimationActivityPresenter
        get() = AnimationActivityPresenter(this)
    override val layoutRes: Int
        get() = R.layout.activity_animation

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        mPresenter.initFragments(savedInstanceState, supportFragmentManager)
        mPresenter.changeFragment(AppConstant.TAG_FRAGMENT_HOME)
        rg_anim_tab.setOnCheckedChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.finish()
    }

    companion object {
        fun jumpToAnimationActivity(context: Context, data: Int) {
            val intent = Intent(context, AnimationActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("code", data)
            if (IntentUtil.queryActivities(context, intent)) context.startActivity(intent)
        }
    }
}