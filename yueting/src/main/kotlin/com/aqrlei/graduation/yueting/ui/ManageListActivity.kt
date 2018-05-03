package com.aqrlei.graduation.yueting.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.presenter.ManageListPresenter
import kotlinx.android.synthetic.main.common_activity_manage.*

/**
 * @author  aqrLei on 2018/5/2
 */
class ManageListActivity :
        MvpContract.MvpActivity<ManageListPresenter>(),
        View.OnClickListener {
    companion object {
        fun jumpToManageListActivity(
                context: Activity,
                type: String,
                typeItem:String,
                data: ArrayList<String>,
                reqCode: Int) {
            val intent = Intent(context, ManageListActivity::class.java).apply {
                val bundle = Bundle()
                bundle.putString(YueTingConstant.MANAGE_TYPE_KEY, type)
                bundle.putString(YueTingConstant.FRAGMENT_TITLE_TYPE,typeItem)
                bundle.putStringArrayList(YueTingConstant.MANAGE_DATA, data)
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backIv -> {
                this.finish()
            }
        }
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        backIv.setOnClickListener(this)
        manageNameTv.text =
                if (intent.extras.getString(YueTingConstant.MANAGE_TYPE_KEY)
        == YueTingConstant.MANAGE_TYPE_LIST) "管理列表" else "管理文件"
    }
    private  fun initData(){
        val type = intent.extras.getString(YueTingConstant.MANAGE_TYPE_KEY)
    }
}