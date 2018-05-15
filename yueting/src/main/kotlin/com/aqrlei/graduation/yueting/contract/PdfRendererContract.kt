package com.aqrlei.graduation.yueting.contract

import com.aqrlei.graduation.yueting.mvp.BasePresenter
import com.aqrlei.graduation.yueting.mvp.BaseView

/**
 * @author  aqrLei on 2018/5/15
 */
interface PdfRendererContract {
    interface Presenter : BasePresenter {

        fun getIndexFromDB(path: String)
        fun addIndexToDB(path: String)
        fun addMarkToDB(path: String, currentBegin: Int)
    }

    interface View : BaseView {
        fun loadBook(begin: Int)
        fun showToast(msg: String)
    }
}