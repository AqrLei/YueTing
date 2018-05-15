package com.aqrlei.graduation.yueting.contract

import com.aqrlei.graduation.yueting.mvp.BasePresenter
import com.aqrlei.graduation.yueting.mvp.BaseView

/**
 * @author  aqrLei on 2018/5/15
 */
interface TitleContract {
    interface Presenter : BasePresenter {

        fun modifyTypeName(isNew: Boolean, type: String, name: String, newName: String = "")
        fun fetchTypeInfo(type: String)
    }

    interface View : BaseView {
        fun modifyFinish(result: Boolean, msg: String)
        fun setTitelList(title: ArrayList<String>)
    }
}