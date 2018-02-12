package com.aqrlei.graduation.yueting.ui

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.ExpandableListView
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.AppLog
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrairsigns.aqrleilib.util.IntentUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.model.local.ChatMessage
import com.aqrlei.graduation.yueting.presenter.activitypresenter.MainActivityPresenter
import com.aqrlei.graduation.yueting.ui.adapter.TestExpandableListAdapter
import com.aqrlei.graduation.yueting.ui.adapter.TestListViewTypeAdapter
import com.aqrlei.graduation.yueting.ui.dialog.TestDialog
import com.facebook.stetho.common.LogUtil
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
                /*val musicIntent: Intent? = (this@MainActivity.application as YueTingApplication).getServiceIntent()
                if(musicIntent != null) {
                    this@MainActivity.applicationContext.startService(musicIntent)
                }*/
                YueTingActivity.jumpToYueTingActivity(this, 0)
            }
            R.id.rb_test -> {
                AppToast.toastShow(this, "RippleButton", 1000)
            }
            R.id.bt_read -> {
                AppLog.logDebug("readTest", "button")
                // TxtReadActivity.jumpToTxtReadActivity(this, "")
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
        val memory = (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).memoryClass
        tv_file_name.text = " Hello World $memory\n"


    }

    override fun onResume() {
        super.onResume()
        /* webViewTest()*/
    }

    private fun webViewTest() {
        val webSetting = wv_test.settings
        webSetting.javaScriptEnabled = true
        webSetting.javaScriptCanOpenWindowsAutomatically = true
        wv_test.loadUrl("file:///android_asset/html/test.html")
        wv_test.setWebChromeClient(object : WebChromeClient() {
            /*警告弹窗拦截, 没有放回值*/
            override fun onJsAlert(view: WebView?, url: String?, message: String?,
                                   result: JsResult?): Boolean {
                tv_file_name.append(" loadUrl:\t $message \n")
                // result?.confirm()//确认，窗口消失
                // return true ,事件不再传递
                return super.onJsAlert(view, url, message, result)
            }

            /*确认框拦截，返回值只有true 和false*/
            override fun onJsConfirm(view: WebView?, url: String?, message: String?,
                                     result: JsResult?): Boolean {
                return super.onJsConfirm(view, url, message, result)
            }

            /*消息框拦截,返回值自定义*/
            override fun onJsPrompt(view: WebView?, url: String?, message: String?,
                                    defaultValue: String?, result: JsPromptResult?):
                    Boolean {
                val uri = Uri.parse(message)
                if (uri.scheme == "js") {
                    if (uri.authority == "webView") {
                        result?.confirm("来自Android - Native 的回调")
                    }
                    return true
                }
                return super.onJsPrompt(view, url, message, defaultValue, result)
            }
        })
        wv_test.addJavascriptInterface(JsInterface(), "obj")
        wv_test.setWebViewClient(object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                view?.loadUrl("javascript:callJS()")
                view?.evaluateJavascript("javascript:call()") { message ->
                    tv_file_name.append("evaluate:\t $message \n")
                }
                //view?.loadUrl("javascript:show()")
                /*
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    view?.loadUrl("javascript:callJS()")
                else
                    view?.evaluateJavascript("javascript:call()") { message ->
                        tv_file_name.append( "evaluate:\t $message \n")
                    }*/
            }

            /*在版本24（7.0)及以上不建议使用*/
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val uri = Uri.parse(url)
                if (uri.scheme == "js") {
                    if (uri.authority == "webView") {
                        val collection = uri.queryParameterNames//获取属性名集合
                        collection.forEach {
                            tv_file_name.append(" $it:\t")
                            val p = uri.getQueryParameter(it)//获取属性值
                            tv_file_name.append("$p\n")

                        }

                        return true//此次拦截事件执行完毕
                    }
                }

                return super.shouldOverrideUrlLoading(view, url)//执行父类的
            }


        })
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

    private class JsInterface {

        @JavascriptInterface
        fun showFromJs(toast: String) {
            AppLog.logDebug("js", toast)
        }
    }
}