package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.presenter.activitypresenter.ReadActivityPresenter

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/11/17.
 */
class ReadActivity : MvpContract.MvpActivity<ReadActivityPresenter>() {
    override val mPresenter: ReadActivityPresenter
        get() = ReadActivityPresenter(this)
    override val layoutRes: Int
        get() = R.layout.activity_read

    companion object {
        fun jumpToReadActivity(context: Context, data: Int) {
            val intent = Intent(context, ReadActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("code", data)
            if (IntentUtil.queryActivities(context, intent)) context.startActivity(intent)
        }

    }
}