package com.aqrlei.graduation.yueting.contract

import com.aqrlei.graduation.yueting.model.SelectInfo
import com.aqrlei.graduation.yueting.mvp.BasePresenter
import com.aqrlei.graduation.yueting.mvp.BaseView

/**
 * @author  aqrLei on 2018/5/15
 */
interface ManageListContract {
    interface Presenter : BasePresenter {
        fun deleteItem(type: String, typeItem: String, deteteDate: List<SelectInfo>)
    }

    interface View : BaseView {
        fun deleteFinished(msg: String, result: Boolean)
    }
}