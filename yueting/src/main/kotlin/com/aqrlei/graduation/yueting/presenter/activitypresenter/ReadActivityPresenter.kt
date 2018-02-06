package com.aqrlei.graduation.yueting.presenter.activitypresenter

import android.os.Environment
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.view.PageView
import com.aqrlei.graduation.yueting.factory.PageFactory
import com.aqrlei.graduation.yueting.model.local.BookInfo
import com.aqrlei.graduation.yueting.ui.ReadActivity
import java.io.File

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/11/17.
 */
class ReadActivityPresenter(mMvpActivity: ReadActivity) :
        MvpContract.ActivityPresenter<ReadActivity>(mMvpActivity) {

    fun getPageFactory(bookInfo: BookInfo, pageView: PageView): PageFactory = PageFactory(pageView, bookInfo)
    /*  val bookMessage = BookInfo()
      val sd = Environment.getExternalStorageDirectory()
      val path = bookInfo?.path ?: sd.path+"/太古神王.txt"
      val file = File(path)
      bookMessage.name = file.name
      bookMessage.path = file.path
      bookMessage.encoding = "GBK"*/


}