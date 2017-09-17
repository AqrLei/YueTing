package com.leilei.guoshujinfu.mylearning.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/8/3.
 */

public final class AppToast extends Toast {
    public AppToast(Context context) {
        super(context);
    }

    public static void toastShow(Context context, String content) {
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void toastShow(Context context, String content, int duration) {
        Toast toast = Toast.makeText(context, content, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
