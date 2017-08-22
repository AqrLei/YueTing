package com.aqrlei.graduation.truckrental.baselib.util

import com.aqrlei.graduation.truckrental.baselib.base.BaseActivity

/**
 * @Author: AqrLei
 *@Name MyLearning
 *@Description:
 *@Date: 2017/8/22
 */
object ActivityCollector {
    private lateinit var activities: ArrayList<BaseActivity>

    fun add(activity: BaseActivity){
       if(activity == null) return
        activities.add(activity)
    }
    fun remove(activity: BaseActivity) {
        if(activity == null) return
        activities.remove(activity)
    }
    fun removeAll() {
        for(activity in activities) {
            if(activity != null && !activity.isFinishing) {
                activity.finish()
            }
            activities.clear()
        }
    }
}