package com.aqrlei.graduation.yueting.ui

import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.ExpandableListView
import com.aqrairsigns.aqrleilib.adapter.CommonPagerAdapter
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.*
import com.aqrairsigns.aqrleilib.view.RoundBar
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.model.local.ChatMessage
import com.aqrlei.graduation.yueting.model.local.ChildMessage
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
        ExpandableListView.OnChildClickListener, RoundBar.OnDrawProgressChangeListener,
        View.OnClickListener {
    override fun onDrawProgressChange(currentProgress: Float) {
        AppLog.logDebug("test", "currentProgress:\t$currentProgress")
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

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.bt_post -> {
                YueTingActivity.jumpToYueTingActivity(this, 0)
            }
            R.id.rb_test -> {
                AppToast.toastShow(this, "RippleButton", 1000)
            }

        }
    }


    //private var mPictureRespBeans: MutableList<PictureRespBean>? = null
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

    private fun initData() {
        rb_test_ratio.visibility = View.VISIBLE
        rb_test_ratio.setOnDrawProgressListener(this)
        tv_file_name.movementMethod = ScrollingMovementMethod.getInstance()

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
        // TODO() elv_test.expandGroup(groupIndex)
    }

    /* fun initViews(data: List<PictureRespBean>) {
         Log.d("Lei", "initViews")
         mPictureRespBeans = ArrayList()
         mPictureRespBeans!!.addAll(data)
         mViews = ArrayList()
         for (i in mPictureRespBeans!!.indices) {
             addImgs(i)
         }
         mViewpagerAdapter = CommonPagerAdapter(mViews as ArrayList<View>)
         vp_impage!!.adapter = mViewpagerAdapter
     }*/

    /* private fun addImgs(pos: Int) {
         val view = LayoutInflater.from(this).inflate(R.layout.picture_from_url, null)
         view.sdv_picture.setImageURI(Uri.parse(mPictureRespBeans!![pos].pictureUrl), null)
         *//* view.sdv_picture.setImageURI()*//*
        mViews!!.add(view)
    }
*/
    fun dbManager() {
        /*数据库SQLiteDatabase操作相关*/
        DBManager.addTable("test", arrayOf("name", "path"), arrayOf("varchar", "varchar"))
                .createDB()
        DBManager.sqlData(DBManager.SqlFormat.insertSqlFormat("test", arrayOf("name", "path")),
                arrayOf("大爱的", "xiao"), null, DBManager.SqlType.INSERT)
        val c = DBManager
                .sqlData(DBManager.SqlFormat.selectSqlFormat("test"), null, null, DBManager.SqlType.SELECT)
                .getCursor()
        while (c!!.moveToNext()) {
            tv_file_name.append(c.getString(c.getColumnIndex("name")) + "\n")
            tv_file_name.append(c.getString(c.getColumnIndex("path")) + "\n")

        }
        c.close()
        DBManager
                .sqlData(
                        DBManager.SqlFormat.deleteSqlFormat("test", "name", "="),
                        null,
                        arrayOf("大爱的"),
                        DBManager.SqlType.DELETE
                )
                .closeDB()
    }

    fun MediaData() {
        /*
       * MediaMetadataRetriever()获取mp3文件相关信息
       * */
        val path = "/storage/sdcard0/netease/cloudmusic/Music/陈奕迅 - 重口味.mp3"
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(path)
        val title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        val album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
        val artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        val duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val byte = mmr.embeddedPicture
        val bitMap = ImageUtil.byteArrayToBitmap(byte)
        val drawable = ImageUtil.bitmapToDrawable(bitMap)
        drawable?.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        tv_file_name.append("title:\t $title \n " +
                "album:\t $album \n " +
                "artist:\t $artist\n" +
                " duration:\t $duration")
        tv_file_name.setCompoundDrawables(drawable, null, null, null)
    }

    fun FileInfo() {

        /*文件操作相关*/
        val fileInfos = FileUtil.createFileInfoS()
        /*  fileInfos.forEach { (name, path, isDir) ->
              tv_file_name.append("name:  $name\t path:  $path\t dir:  $isDir\n")
          }*/
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