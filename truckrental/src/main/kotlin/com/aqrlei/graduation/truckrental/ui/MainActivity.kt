package com.aqrlei.graduation.truckrental.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ExpandableListView
import com.aqrlei.graduation.truckrental.R
import com.aqrlei.graduation.truckrental.baselib.mvp.MvpContract
import com.aqrlei.graduation.truckrental.baselib.util.AppToast
import com.aqrlei.graduation.truckrental.baselib.util.IntentUtil
import com.aqrlei.graduation.truckrental.baselib.util.adapter.CommonPagerAdapter
import com.aqrlei.graduation.truckrental.model.local.ChatMessage
import com.aqrlei.graduation.truckrental.model.local.ChildMessage
import com.aqrlei.graduation.truckrental.model.resp.PictureRespBean
import com.aqrlei.graduation.truckrental.presenter.activitypresenter.MainActivityPresenter
import com.aqrlei.graduation.truckrental.ui.adapter.TestExpandableListAdapter
import com.aqrlei.graduation.truckrental.ui.adapter.TestListViewTypeAdapter
import kotlinx.android.synthetic.main.activity_picture.*
import kotlinx.android.synthetic.main.picture_from_url.view.*

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/22
 */

class MainActivity : MvpContract.MvpActivity<MainActivityPresenter>(),
        ExpandableListView.OnChildClickListener {
    override fun onChildClick(p0: ExpandableListView?, p1: View?, p2: Int, p3: Int, p4: Long): Boolean {
        AppToast.toastShow(this, "Group: \t" + mData[p2].content
                + "Child: \t" + (mData[p2].child?.get(p3)?.name ?: "--"), 2000)
        return true
    }


    private var mPictureRespBeans: MutableList<PictureRespBean>? = null
    private var mViewpagerAdapter: CommonPagerAdapter<*>? = null
    private var mViews: ArrayList<View>? = null
    val mData = ArrayList<ChatMessage>()
    val mChild = ArrayList<ChildMessage>()
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == 1) {
                vp_impage.currentItem = vp_impage.currentItem + 1
                this.sendEmptyMessageDelayed(1, 3000)
            }
        }
    }


    override val layoutRes: Int
        get() = R.layout.activity_picture
    override val mPresenter: MainActivityPresenter
        get() = MainActivityPresenter(this)


    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        initData()

        //mPresenter.getImg(HttpReqConfig.RQ_IMG_TYPE)
        //mHandler.sendEmptyMessageDelayed(1, 3000)
        elv_test.setAdapter(TestExpandableListAdapter(this, mData, R.layout.listitem_content,
                R.layout.listitem_title))
        elv_test.setOnChildClickListener(this)
        lv_test.adapter = TestListViewTypeAdapter(this, R.layout.listitem_title,
                R.layout.listitem_content, mData)
        bt_post.setOnClickListener({
            AnimationActivity.jumpToAnimationActivity(this, 0)
        })
        defaultExpandGroup()

    }

    private fun initData() {
        mChild.add(ChildMessage("child1", getDrawable(R.mipmap.ic_launcher_round)))
        mChild.add(ChildMessage("child2", getDrawable(R.mipmap.ic_launcher_round)))
        mChild.add(ChildMessage("child3", getDrawable(R.mipmap.ic_launcher_round)))
        mChild.add(ChildMessage("child4", getDrawable(R.mipmap.ic_launcher_round)))
        mChild.add(ChildMessage("child5", getDrawable(R.mipmap.ic_launcher_round)))

        mData.add(ChatMessage("一", 1, getDrawable(R.mipmap.ic_launcher), mChild))
        mData.add(ChatMessage("二", 1, getDrawable(R.mipmap.ic_launcher_round), null))
        mData.add(ChatMessage("三", 1, getDrawable(R.mipmap.ic_launcher), null))
        mData.add(ChatMessage("四", 1, getDrawable(R.mipmap.ic_launcher_round), mChild))
        mData.add(ChatMessage("五", 1, getDrawable(R.mipmap.ic_launcher), null))
        mData.add(ChatMessage("六", 1, getDrawable(R.mipmap.ic_launcher_round), null))
        mData.add(ChatMessage("七", 1, getDrawable(R.mipmap.ic_launcher_round), mChild))
        mData.add(ChatMessage("九", 1, getDrawable(R.mipmap.ic_launcher), null))
        mData.add(ChatMessage("十", 1, getDrawable(R.mipmap.ic_launcher), null))
        mData.add(ChatMessage("十一", 1, getDrawable(R.mipmap.ic_launcher_round), null))
        mData.add(ChatMessage("十二", 1, getDrawable(R.mipmap.ic_launcher), null))
        mData.add(ChatMessage("十三", 1, getDrawable(R.mipmap.ic_launcher_round), mChild))
        mData.add(ChatMessage("十四", 1, getDrawable(R.mipmap.ic_launcher), null))
    }

    fun defaultExpandGroup() {
        for (i in mData.indices) elv_test.expandGroup(i)
    }

    fun initViews(data: List<PictureRespBean>) {
        Log.d("Lei", "initViews")
        mPictureRespBeans = ArrayList()
        mPictureRespBeans!!.addAll(data)
        mViews = ArrayList()
        for (i in mPictureRespBeans!!.indices) {
            addImgs(i)
        }
        mViewpagerAdapter = CommonPagerAdapter(mViews as ArrayList<View>)
        vp_impage!!.adapter = mViewpagerAdapter
    }

    private fun addImgs(pos: Int) {
        val view = LayoutInflater.from(this).inflate(R.layout.picture_from_url, null)
        view.sdv_picture.setImageURI(Uri.parse(mPictureRespBeans!![pos].pictureUrl), null)
        /* view.sdv_picture.setImageURI()*/
        mViews!!.add(view)
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