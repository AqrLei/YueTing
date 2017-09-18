package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.model.local.ChatMessage
import com.aqrlei.graduation.yueting.presenter.activitypresenter.MainActivityPresenter
import com.aqrlei.graduation.yueting.ui.adapter.TestExpandableListAdapter
import com.aqrlei.graduation.yueting.ui.adapter.TestListViewTypeAdapter
import com.aqrlei.graduation.yueting.ui.dialog.TestDialog
import kotlinx.android.synthetic.main.activity_picture.*

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/22
 */

class MainActivity : MvpContract.MvpActivity<MainActivityPresenter>(),
        ExpandableListView.OnChildClickListener,
        View.OnClickListener {

    override fun onChildClick(p0: ExpandableListView?, p1: View?, p2: Int, p3: Int, p4: Long): Boolean {

        /*自定义Dialog的使用*/
        TestDialog(this@MainActivity)
                .setTitle("测试")
                .setMessage("一个测试\n 就是一个测试\n真得就是一个测试")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", null)
                .show()

        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_post -> {
                YueTingActivity.jumpToYueTingActivity(this, 0)
            }
            R.id.rb_test -> {
                AppToast.toastShow(this, "RippleButton", 1000)
            }

        }
    }


    /*
      private var mViewpagerAdapter: CommonPagerAdapter<*>? = null
      private var mViews: ArrayList<View>? = null

      private val mHandler = object : Handler() {
          override fun handleMessage(msg: Message) {
              if (msg.what == 1) {
                  vp_impage.currentItem = vp_impage.currentItem + 1
                  this.sendEmptyMessageDelayed(1, 3000)
              }
          }
      }*/
    private val mData = ArrayList<ChatMessage>()

    override val layoutRes: Int
        get() = R.layout.activity_picture
    override val mPresenter: MainActivityPresenter
        get() = MainActivityPresenter(this)

    override fun beforeSetContentView() {
        super.beforeSetContentView()
        //TODO permissionCheck(context: Context, permission: String, mRequestCode: Int): Boolean
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        aqr_tv_test.visibility = View.GONE
        tv_file_name.visibility = View.VISIBLE
        tv_file_name.text = " Hello World"
    }

    fun defaultExpandGroup() {
        // TODO() elv_test.expandGroup(groupIndex)
    }

    fun ListViewTest() {
        //mPresenter.getImg(HttpReqCofig.RQ_IMG_TYPE)
        //mHandler.sendEmptyMessageDelayed(1, 3000)

        /*通过布局的id获取ExpandableListView实例，设置adapter*/
        elv_test.setAdapter(TestExpandableListAdapter(this, mData, R.layout.listitem_content,
                R.layout.listitem_title_main))
        /*遍历group，默认展开*/
        for (i in mData.indices) elv_test.expandGroup(i)
        /*设置子项的点击事件*/
        elv_test.setOnChildClickListener(this)

        lv_test.adapter = TestListViewTypeAdapter(this, R.layout.listitem_title_main,
                R.layout.listitem_content, mData)
        lv_test.visibility = View.GONE
        elv_test.visibility = View.GONE
        defaultExpandGroup()
    }

    companion object {
        fun jumpToMainActivity(context: Context, data: Int) {
            val intent = Intent(context, MainActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("code", data)
            intent.putExtras(bundle)
            if (IntentUtil.queryActivities(context, intent)) context.startActivity(intent)
        }
    }
}