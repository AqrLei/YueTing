package com.aqrairsigns.aqrleilib.util

import android.database.sqlite.SQLiteDatabase

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/12 Time: 9:43
 */
class SQLiteUtil {
    companion object {
        val CREATE_TABLE: String = "CREATE TABLE "
        val CREATE_TABLE_ID: String = " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
        val instance: SQLiteUtil
            get() = Singleton.INSTANCE.instance
    }

    private var db: SQLiteDatabase? = null
    fun openOrCreateDatabase(name: String): SQLiteUtil {
        db = SQLiteDatabase.openOrCreateDatabase(name, null, null)
        return this
    }

    fun createTable(tableName: String, fileId: Array<String>, fileType: Array<String>): SQLiteUtil {
        var sql: String = CREATE_TABLE + tableName + CREATE_TABLE_ID
        var i = 0
        fileId.forEach {
            sql += (it + " " + fileType[i] + ", ")
            i++
        }
        sql += ")"
        db?.execSQL(sql)
        return this
    }

    fun deleteTable(tableName: String): SQLiteUtil {

        return this
    }


    private enum class Singleton {
        INSTANCE;

        val instance: SQLiteUtil = SQLiteUtil()
    }


}