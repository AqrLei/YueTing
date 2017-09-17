package com.aqrairsigns.aqrleilib.util

import android.content.Context
import android.content.SharedPreferences

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/12 Time: 14:54
 */
class AppSharedPreferences private constructor() {
    private val mShared: SharedPreferences
    private val mEditor: SharedPreferences.Editor

    init {
        if (mContext == null) {
            throw RuntimeException("must invoke init ahead")
        }
        mShared = mContext!!.getSharedPreferences(mFileName, Context.MODE_PRIVATE)
        mEditor = mShared.edit()
    }

    fun putString(key: String, value: String): AppSharedPreferences {
        mEditor.putString(key, value)
        return this
    }

    fun putFloat(key: String, value: Float): AppSharedPreferences {
        mEditor.putFloat(key, value)
        return this
    }

    fun putLong(key: String, value: Long): AppSharedPreferences {
        mEditor.putLong(key, value)
        return this
    }

    fun putInt(key: String, value: Int): AppSharedPreferences {
        mEditor.putInt(key, value)
        return this
    }

    fun putBoolean(key: String, value: Boolean): AppSharedPreferences {
        mEditor.putBoolean(key, value)
        return this
    }

    fun commit() {
        mEditor.commit()
        mEditor.clear()
    }

    fun remove(key: String): AppSharedPreferences {
        mEditor.remove(key)
        return this
    }

    fun removeAll(key: String): AppSharedPreferences {
        mEditor.clear()
        return this
    }

    fun getString(key: String, defValue: String) = mShared.getString(key, defValue)
    fun getFloat(key: String, defValue: Float) = mShared.getFloat(key, defValue)
    fun getLong(key: String, defValue: Long) = mShared.getLong(key, defValue)
    fun getInt(key: String, defValue: Int) = mShared.getInt(key, defValue)
    fun getBoolean(key: String, defValue: Boolean) = mShared.getBoolean(key, defValue)

    companion object {
        private var mContext: Context? = null
        private var mFileName: String? = null

        fun init(context: Context) {
            mContext = context.applicationContext
        }

        fun setFileName(name: String?) {
            mFileName = name
        }

        val mSharedPreferences: AppSharedPreferences
            get() = Singleton.INSTANCE.instance
    }
    private enum class Singleton {
        INSTANCE;
        val instance = AppSharedPreferences()
    }
}