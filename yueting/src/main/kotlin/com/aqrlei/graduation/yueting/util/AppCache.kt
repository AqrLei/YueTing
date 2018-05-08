package com.aqrlei.graduation.yueting.util

import android.content.Context
import android.content.SharedPreferences

/**
 * @Author: AqrLei
 * @CreateTime: Date: 2017/9/12 Time: 14:54
 */
enum class AppCache {
    APPCACHE;

    companion object {
        private lateinit var mContext: Context
        private var mFileName: String? = ""
        private lateinit var mShared: SharedPreferences
        private lateinit var mEditor: SharedPreferences.Editor
        fun init(context: Context, name: String?) {
            mContext = context.applicationContext
            mFileName = name
            mShared = mContext.getSharedPreferences(mFileName, Context.MODE_PRIVATE)
            mEditor = mShared.edit()
        }
    }

    fun putString(key: String, value: String): AppCache {
        mEditor.putString(key, value)
        return this
    }

    fun putFloat(key: String, value: Float): AppCache {
        mEditor.putFloat(key, value)
        return this
    }

    fun putLong(key: String, value: Long): AppCache {
        mEditor.putLong(key, value)
        return this
    }

    fun putInt(key: String, value: Int): AppCache {
        mEditor.putInt(key, value)
        return this
    }

    fun putBoolean(key: String, value: Boolean): AppCache {
        mEditor.putBoolean(key, value)
        return this
    }

    fun commit() {
        mEditor.commit()
        mEditor.clear()
    }

    fun remove(key: String): AppCache {
        mEditor.remove(key)
        return this
    }

    fun removeAll(): AppCache {
        mEditor.clear()
        return this
    }

    fun getString(key: String, defValue: String) = mShared.getString(key, defValue)
    fun getFloat(key: String, defValue: Float) = mShared.getFloat(key, defValue)
    fun getLong(key: String, defValue: Long) = mShared.getLong(key, defValue)
    fun getInt(key: String, defValue: Int) = mShared.getInt(key, defValue)
    fun getBoolean(key: String, defValue: Boolean) = mShared.getBoolean(key, defValue)

}