package com.aqrlei.graduation.yueting.presenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.SelectInfo
import com.aqrlei.graduation.yueting.ui.ManageListActivity

/**
 * @author  aqrLei on 2018/5/2
 */
class ManageListPresenter(mMvpActivity: ManageListActivity) :
        MvpContract.ActivityPresenter<ManageListActivity>(mMvpActivity) {
    fun deleteItem(type:String, typeItem:String, deleteData:List<SelectInfo>){
        if (type == YueTingConstant.MANAGE_TYPE_LIST) {
            if (typeItem == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK) {
                //TODO delete the bookList and items in it
            } else {
                //TODO delete the musicList and items in it
            }
        } else {
            if (typeItem == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK) {
               //TODO delete the bookItems
            } else {
                //TODO delete the musicItems
            }
        }
    }
}