package com.leilei.guoshujinfu.mylearning;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.leilei.guoshujinfu.mylearning.util.AppCache;
import com.leilei.guoshujinfu.mylearning.util.AppLog;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/7
 */


public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        Fresco.initialize(this);
        AppCache.init(this);
        AppCache.setFileName("MyLearning");
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        AppLog.logDebug(AppLog.LOG_TAG_ACTIVITY, activity.getClass().getSimpleName() + "Created");

    }

    @Override
    public void onActivityStarted(Activity activity) {
        AppLog.logDebug(AppLog.LOG_TAG_ACTIVITY, activity.getClass().getSimpleName() + "Started");

    }

    @Override
    public void onActivityResumed(Activity activity) {
        AppLog.logDebug(AppLog.LOG_TAG_ACTIVITY, activity.getClass().getSimpleName() + "Resumed");

    }

    @Override
    public void onActivityPaused(Activity activity) {
        AppLog.logDebug(AppLog.LOG_TAG_ACTIVITY, activity.getClass().getSimpleName() + "Paused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        AppLog.logDebug(AppLog.LOG_TAG_ACTIVITY, activity.getClass().getSimpleName() + "Stopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        AppLog.logDebug(AppLog.LOG_TAG_ACTIVITY, activity.getClass().getSimpleName() + "InstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        AppLog.logDebug(AppLog.LOG_TAG_ACTIVITY, activity.getClass().getSimpleName() + "Destroyed");
    }
}
