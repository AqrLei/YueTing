package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.presenter.activitypresenter.YueTingActivityPresenter
import kotlinx.android.synthetic.main.activity_yueting.*


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
        , RadioGroup.OnCheckedChangeListener
        , View.OnClickListener {
    override fun onCheckedChanged(radioGroup: RadioGroup?, checkedId: Int) {
        (0 until radioGroup!!.childCount)
                .filter { radioGroup.getChildAt(it).id == checkedId }
                .forEach {
                    changeFragment(it)
                    titleName = (radioGroup.getChildAt(it) as RadioButton).text.toString()
                }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_play_control -> {
                sendMusicBroadcast(YueTingActivityPresenter.SendType.PLAY)

            }
            R.id.tv_next -> {
                sendMusicBroadcast(YueTingActivityPresenter.SendType.NEXT)
            }
            R.id.tv_previous -> {
                sendMusicBroadcast(YueTingActivityPresenter.SendType.PREVIOUS)
            }
            R.id.ll_to_play -> {
                //TODO jumpTOPlayActivity()
            }
        }
    }

    override val mPresenter: YueTingActivityPresenter
        get() = YueTingActivityPresenter(this)
    override val layoutRes: Int
        get() = R.layout.activity_yueting


    private var mFragments = ArrayList<Fragment>()
    private var titleName: String = ""
    private lateinit var mFragmentManager: FragmentManager

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        mFragmentManager = supportFragmentManager
        initFragments(savedInstanceState)
        rg_anim_tab.setOnCheckedChangeListener(this)
        tv_file_local.setOnClickListener {
            FileActivity.jumpToFileActivity(this@YueTingActivity)
        }
    }

    private fun initFragments(savedInstanceState: Bundle?) {
        mPresenter.initFragments(savedInstanceState, mFragmentManager)
        changeFragment(YueTingConstant.TAG_FRAGMENT_HOME)
    }

    private fun changeFragment(position: Int) {
        for (i in mFragments.indices) {
            val currentFragment = mFragments[i]
            val ft = mFragmentManager.beginTransaction()
            if (i == position) {
                if (!currentFragment.isAdded) {
                    ft.add(R.id.fl_fragment, currentFragment, YueTingConstant.TAB_FRAGMENT_TAGS[i])
                }
                ft.show(currentFragment)
            } else {
                ft.hide(currentFragment)
            }
            ft.commit()
        }
    }

    private fun sendMusicBroadcast(type: YueTingActivityPresenter.SendType) {
        mPresenter.sendMusicBroadcast(type)
    }

    fun setFragments(fragments: ArrayList<Fragment>) {
        mFragments = fragments
    }


    companion object {
        fun jumpToYueTingActivity(context: Context, data: Int) {
            val intent = Intent(context, YueTingActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("code", data)
            if (IntentUtil.queryActivities(context, intent)) context.startActivity(intent)
        }
    }
}