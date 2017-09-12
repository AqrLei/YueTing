package com.aqrairsigns.aqrleilib.util

import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.aqrairsigns.aqrleilib.info.DataTableInfo

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/12 Time: 14:57
 */
class DBHelper(
        context: Context,
        name: String,
        factory: SQLiteDatabase.CursorFactory? = null,
        version: Int,
        errorHandler: DatabaseErrorHandler? = null
) : SQLiteOpenHelper(context, name, factory, version, errorHandler) {

    companion object {
        private var mTableInfoList = ArrayList<DataTableInfo>()
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

        fun clear() {
            if (!mTableInfoList.isEmpty()) {
                mTableInfoList.clear()
            }
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
        mTableInfoList.forEach {
            db?.execSQL("drop table if exists " + it.name)
        }
        onCreate(db)
    }
}