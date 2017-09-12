package com.aqrairsigns.aqrleilib.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/12 Time: 15:00
 */
class DBManager private constructor() {

    private var db: SQLiteDatabase? = null
    private var mContext: Context? = null
    private var mDBName: String = " "

    companion object {
        val dbManager: DBManager
            get() = Singleton.INSTANCE.instance
    }

    fun addTable(name: String, fileId: Array<String>, fileType: Array<String>) {
        DBHelper.addTable(name, fileId, fileType)
        db = DBHelper.dbHelper.writableDatabase
    }

    fun initDBHelper(context: Context, dbName: String, version: Int) {
        mContext = context
        mDBName = dbName
        DBHelper.init(context, dbName, version)
    }

    fun removeTable(name: String) {
        DBHelper.dbHelper.remove(name)
    }

    fun removeAllTable() {
        DBHelper.dbHelper.clear()
    }


    fun deleteDB() = mContext?.deleteDatabase(mDBName)
    fun deleteDB(name: String) = mContext?.deleteDatabase(name)
    fun deleteDB(context: Context) = context.deleteDatabase(mDBName)
    fun deleteDB(context: Context, name: String) = context.deleteDatabase(name)

    fun closeDB() {
        db?.close()
    }

    private enum class Singleton {
        INSTANCE;

        val instance = DBManager()
    }

}