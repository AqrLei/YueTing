package com.aqrairsigns.aqrleilib.util

import com.aqrairsigns.aqrleilib.basemvp.BaseActivity


/**
 * @Author: AqrLei
 *@Name MyLearning
 *@Description:
 *@Date: 2017/8/22
 */
object ActivityCollector {
    private var activities = ArrayList<BaseActivity?>()

    fun add(activity: BaseActivity?) {
        if (activity == null) return
        activities.add(activity)
    }

    fun remove(activity: BaseActivity?) {
        /*if(activity == null) return*/
        activities.remove(activity)
    }

    fun removeAll() {
        activities
                .filterNotNull()
                .filterNot { it.isFinishing }
                .forEach { it.finish() }
        activities.clear()
    }
}