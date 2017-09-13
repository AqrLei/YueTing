/*
package com.aqrairsigns.aqrleilib.util

import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.aqrairsigns.aqrleilib.info.DataTableInfo

*/
/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/12 Time: 14:57
 */
/*

class DBHelper private constructor(
        context: Context?,
        name: String?,
        factory: SQLiteDatabase.CursorFactory? = null,
        version: Int,
        errorHandler: DatabaseErrorHandler? = null
) : SQLiteOpenHelper(context, name, factory, version, errorHandler) {
    private var mDeleteTableInfoList: ArrayList<DataTableInfo>? = null

    companion object {

        val dbHelper: DBHelper
            get() = Singleton.INSTANCE.getInstance(mContext, mName, mVersion)
        private var mTableInfoList = ArrayList<DataTableInfo>()
        private var mContext: Context? = null
        private var mName: String? = null
        private var mVersion: Int = 0
        fun addTable(name: String, fileId: Array<String>, fileType: Array<String>) {
            if (fileId.size != fileType.size) {
                throw IllegalArgumentException("fileId and fileType arrays must be of equal length")
            }
            val temp = DataTableInfo()
            temp.name = name
            temp.fileId = fileId
            temp.fileType = fileType
            mTableInfoList.add(temp)
        }

        fun init(context: Context, name: String, version: Int) {
            mContext = context
            mName = name
            mVersion = version
        }

    }

    init {
        mDeleteTableInfoList = ArrayList()
    }

    fun remove(name: String): Boolean {
        if (!mTableInfoList.isEmpty()) {
            if (mTableInfoList.remove(DataTableInfo(name))) {
                mDeleteTableInfoList?.add(DataTableInfo(name))
                return true
            }
            return false
        } else {
            return false
        }
    }

    fun clear() {
        if (!mTableInfoList.isEmpty()) {
            mDeleteTableInfoList?.addAll(mTableInfoList)
            mTableInfoList.clear()
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        mTableInfoList.forEach {
            var sql = "create table ( _id integer primary key autoincrement, " + it.name
            for (i in it.fileId!!.indices) {
                sql += (it.fileId!![i] + " " + it.fileType!![i] + ",")
            }
            sql += ")"
            db?.execSQL(sql)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        mDeleteTableInfoList?.forEach {
            db?.execSQL("drop table if exists " + it.name)
        }
        mTableInfoList.forEach {
            db?.execSQL("drop table if exists " + it.name)
        }
        onCreate(db)
    }

    private enum class Singleton {
        INSTANCE;

        fun getInstance(context: Context?, name: String?, version: Int): DBHelper {
            if (context == null) {
                throw RuntimeException("must invoke init ahead")
            }
            if (mTableInfoList.isEmpty()) {
                throw RuntimeException("must invoke addTab ahead")
            }
            return DBHelper(context.applicationContext, name, null, version, null)
        }

    }
}*/
