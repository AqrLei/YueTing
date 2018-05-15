package com.aqrlei.graduation.yueting.contract

import com.aqrlei.graduation.yueting.mvp.BasePresenter
import com.aqrlei.graduation.yueting.mvp.BaseView

/**
 * @author  aqrLei on 2018/5/15
 */
interface TxtReadContract {
    interface Presenter : BasePresenter {
        fun addIndexToDB(path: String, begin: Int, end: Int)
        fun getBookInfo(path: String)
        fun addMarkToDB(path: String, currentBegin: Int)
    }

    interface View : BaseView {
        fun showToast(msg: String)
    }
}