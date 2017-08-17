package com.leilei.guoshujinfu.mylearning.util.metrics;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/10
 */

public class MetricsUtil {
    public static int getScreenWidth(Context context) {
        if(context instanceof Activity) {
            DisplayMetrics metrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            return metrics.widthPixels;
        }
        return 0;
    }
    public static int getScreenHeight(Context context) {
        if(context instanceof Activity) {
            DisplayMetrics metrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            return  metrics.heightPixels;
        }
        return 0;
    }
}
