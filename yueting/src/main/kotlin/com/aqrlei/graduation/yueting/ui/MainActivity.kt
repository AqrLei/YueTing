package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ExpandableListView
import com.aqrairsigns.aqrleilib.adapter.CommonPagerAdapter
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.info.Info.FileInfo
import com.aqrairsigns.aqrleilib.util.AppLog
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrairsigns.aqrleilib.util.file.FileUtil
import com.aqrairsigns.aqrleilib.view.RoundBar
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.model.local.ChatMessage
import com.aqrlei.graduation.yueting.model.local.ChildMessage
import com.aqrlei.graduation.yueting.model.resp.PictureRespBean
import com.aqrlei.graduation.yueting.presenter.activitypresenter.MainActivityPresenter
import com.aqrlei.graduation.yueting.ui.adapter.TestExpandableListAdapter
import com.aqrlei.graduation.yueting.ui.adapter.TestListViewTypeAdapter
import com.aqrlei.graduation.yueting.ui.dialog.TestDialog
import kotlinx.android.synthetic.main.activity_picture.*
import kotlinx.android.synthetic.main.picture_from_url.view.*

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/22
 */

class MainActivity : MvpContract.MvpActivity<MainActivityPresenter>(),
        ExpandableListView.OnChildClickListener, RoundBar.OnDrawProgressListener {
    override fun onDrawProgressRatio(ratio: Float) {
        AppLog.logDebug("test", "Ratio: $ratio")
    }

    override fun onChildClick(p0: ExpandableListView?, p1: View?, p2: Int, p3: Int, p4: Long): Boolean {
        //AppToast.toastShow(this, "Group: \t" + mData[p2].content
        // + "Child: \t" + (mData[p2].child?.get(p3)?.name ?: "--"), 2000)

        /*自定义Dialog的使用*/
        TestDialog(this@MainActivity)
                .setTitle("测试")
                .setMessage("一个测试\n 就是一个测试\n真得就是一个测试")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", null)
                .show()

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
        rb_test_ratio.setOnDrawProgressListener(this)
        val fileInfos = FileUtil.createFileInfos()
        tv_file_name.movementMethod = ScrollingMovementMethod.getInstance()
        fileInfos.forEach { (name, path, isDir) ->
            tv_file_name.append("name:  $name path:  $path dir:  $isDir\n")
        }
        initData()


        //mPresenter.getImg(HttpReqConfig.RQ_IMG_TYPE)
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
        bt_post.setOnClickListener({
            YueTingActivity.jumpToAnimationActivity(this, 0)
        })
        lv_test.visibility = View.GONE
        elv_test.visibility = View.GONE
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