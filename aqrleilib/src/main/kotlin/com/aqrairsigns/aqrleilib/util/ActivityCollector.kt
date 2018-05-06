package com.aqrairsigns.aqrleilib.util

import android.app.Activity


/**
 *@Author: AqrLei
 *@Date: 2017/8/22
 */
object ActivityCollector {
    private var activities = ArrayList<Activity>()

    fun add(activity: Activity) {

        activities.add(activity)
    }

    fun remove(activity: Activity) {

        activities.remove(activity)
    }

    fun killApp() {
        activities.filter { !it.isFinishing }
                .forEach {
                    it.finish()
                }
    }
}