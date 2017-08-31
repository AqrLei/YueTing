package com.aqrlei.graduation.yueting.baselib.util

import com.aqrlei.graduation.yueting.baselib.base.BaseActivity

/**
 * @Author: AqrLei
 *@Name MyLearning
 *@Description:
 *@Date: 2017/8/22
 */
object ActivityCollector {
    private var activities = ArrayList<BaseActivity>()

    fun add(activity: BaseActivity?) {
        if (activity == null) return
        activities.add(activity)
    }

    fun remove(activity: BaseActivity?) {
        /*if(activity == null) return*/
        activities.remove(activity)
    }

    fun removeAll() {
        for (activity in activities ){
            if (activity != null && !activity.isFinishing) {
                activity.finish()
            }
            activities.clear()
        }
    }
}