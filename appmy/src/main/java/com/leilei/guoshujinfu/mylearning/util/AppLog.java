package com.leilei.guoshujinfu.mylearning.util;

import android.util.Log;

/**
 * Created by Administrator on 2017/8/1.
 */

public final class AppLog {
    public static final String LOG_TAG_ACTIVITY = "activity";
    public static final String LOG_TAG_TEST = "test";
    private AppLog(){

    }
    public static void logDebug(String tag, String msg) {
        Log.d(tag, msg);
    }
    public static void logError(String tag, String msg) {
        Log.e(tag, msg);
    }


}
