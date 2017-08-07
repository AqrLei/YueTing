package com.leilei.guoshujinfu.mylearning.util;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/7
 */

public class BasePresenter {
    protected List<Subscription> subscriptions;
    public BasePresenter() {
        subscriptions = new ArrayList<>();
    }
    public void cancle() {
        for(Subscription subscription: subscriptions) {
            if(!subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
        subscriptions.clear();

    }
    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
    }
}
