package com.aqrlei.graduation.yueting.presenter


import com.aqrlei.graduation.yueting.basemvp.MvpContract
import com.aqrlei.graduation.yueting.ui.YueTingActivity

/**
 * Author: AqrLei
 * Date: 2017/8/23
 */
/*
* @param mMvpActivity 访问对应的Activity
* */
class YueTingPresenter(mMvpActivity: YueTingActivity) :
        MvpContract.ActivityPresenter<YueTingActivity>(mMvpActivity)
