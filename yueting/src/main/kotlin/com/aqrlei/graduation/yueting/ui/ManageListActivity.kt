package com.aqrlei.graduation.yueting.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.presenter.ManageListPresenter

/**
 * @author  aqrLei on 2018/5/2
 */
class ManageListActivity : MvpContract.MvpActivity<ManageListPresenter>() {
    companion object {
        fun jumpToManageListActivity(context: Activity, type: String, reqCode: Int) {
            val intent = Intent(context, ManageListActivity::class.java).apply {
                val bundle = Bundle()
                bundle.putString(YueTingConstant.MANAGE_TYPE_KEY, type)
                putExtras(bundle)
            }
            if (IntentUtil.queryActivities(context, intent)) {
                context.startActivityForResult(intent, reqCode)
            }
        }
    }

    override val mPresenter: ManageListPresenter
        get() = ManageListPresenter(this)
    override val layoutRes: Int
        get() = R.layout.common_activity_manage
}