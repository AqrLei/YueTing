package com.aqrlei.graduation.truckrental.baselib.base

import rx.Subscription

/**
 * @Author: AqrLei
 *@Name MyLearning
 *@Description:
 *@Date: 2017/8/22
 */
open class BasePresenter {
    protected lateinit var subscriptions: ArrayList<Subscription>
    fun cancle() {
        for (subscription in subscriptions) {
            if (!subscription.isUnsubscribed)
                subscription.unsubscribe()
        }
        subscriptions.clear()
    }

    fun addSubscription(subscription: Subscription) {
        subscriptions.add(subscription)
    }
}