package com.leilei.guoshujinfu.mylearning.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */

public final class ActivityCollector {
    private static List<BaseActivity> activities = new ArrayList<>();
    public static void add(BaseActivity activity) {
        if(activity == null) {
            return;
        }
        activities.add(activity);
    }
    public static void remove(BaseActivity activity) {
        if(activity == null) {
            return;
        }
        activities.remove(activity);
    }
    public static void removeAll() {
        for(BaseActivity activity : activities){
            if(activity != null && !activity.isFinishing()) {
                activity.finish();
            }
            activities.clear();

        }
    }
}
