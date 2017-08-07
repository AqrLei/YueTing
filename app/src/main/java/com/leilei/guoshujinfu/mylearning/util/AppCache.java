package com.leilei.guoshujinfu.mylearning.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

import com.leilei.guoshujinfu.mylearning.R;

/**
 * Created by Administrator on 2017/8/3.
 */

public final class AppCache {
    private static Context mContext;
    private static String mFileName;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private AppCache () {
        if(mContext == null) {
            throw new RuntimeException("must invoke init() before new an object");
        }
        mSharedPreferences = mContext.getSharedPreferences(mFileName,Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }
    public static void init(Context context) {
        mContext = context;
    }
    public static void setFileName(String fileName) {
        mFileName = fileName;
    }
    public static AppCache getAppCache() {
        return  CacheHolder.mCache;
    }
    public AppCache putString(String key, String defaultValue) {
        mEditor.putString(key, defaultValue);
        return this;
    }
    public AppCache getString(String key, String defaultValue) {
        mSharedPreferences.getString(key, defaultValue);
        return this;
    }
    public void commit() {
        mEditor.commit();
    }
    public AppCache remove(String key) {
        mEditor.remove(key);
        return this;
    }


    private static class CacheHolder {
        private static final AppCache mCache = new AppCache();
    }

    public static Drawable getAvatar(Context context){
        return context.getResources().getDrawable(R.mipmap.main_img_tab_profile_blue, null);
    }
}
