package com.leilei.guoshujinfu.mylearning.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.leilei.guoshujinfu.mylearning.R;

/**
 * Created by Administrator on 2017/8/3.
 */

public final class AppCache {
    public static Drawable getAvatar(Context context){
        return context.getResources().getDrawable(R.mipmap.main_img_tab_profile_blue, null);
    }
}
