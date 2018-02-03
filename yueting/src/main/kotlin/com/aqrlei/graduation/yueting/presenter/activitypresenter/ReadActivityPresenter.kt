package com.aqrlei.graduation.yueting.presenter.activitypresenter

import android.os.Environment
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.view.PageView
import com.aqrlei.graduation.yueting.factory.PageFactory
import com.aqrlei.graduation.yueting.model.local.BookMessage
import com.aqrlei.graduation.yueting.ui.ReadActivity
import java.io.File
import java.io.InputStream

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/11/17.
 */
class ReadActivityPresenter(mMvpActivity: ReadActivity) :
        MvpContract.ActivityPresenter<ReadActivity>(mMvpActivity) {
    fun getPageFactory(pageView: PageView): PageFactory {
        val bookMessage = BookMessage()
        val sd = Environment.getExternalStorageDirectory()
        val path = sd.path + "/太古神王.txt"
        val file = File(path)
        bookMessage.name = file.name
        bookMessage.path = file.path
        bookMessage.encoding = "GBK"
        return PageFactory(pageView, bookMessage)
    }
}