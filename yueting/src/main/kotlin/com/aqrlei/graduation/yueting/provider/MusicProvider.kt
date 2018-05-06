package com.aqrlei.graduation.yueting.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrlei.graduation.yueting.constant.DataConstant

/**
 * created by AqrLei at 21:46 on 星期五, 五月 04, 2018
 */
class MusicProvider : ContentProvider() {
    companion object {
        private const val TAG = "MusicProvider"
        const val AUTHORITY = "com.aqrlei.graduation.yueting.provider.MUSIC"
        val MUSIC_CONTENT_URI = Uri.parse("content://$AUTHORITY/${DataConstant.MUSIC_TABLE_NAME}")
        const val MUSIC_URI_CODE = 1
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, DataConstant.MUSIC_TABLE_NAME, MUSIC_URI_CODE)
        }
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri? {
        return null
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return DBManager.sqlData(
                DBManager.SqlFormat.selectSqlFormat(
                        DataConstant.MUSIC_TABLE_NAME,
                        "",
                        selection ?: "",
                        if (selection == null) "" else "="),
                null, selectionArgs, DBManager.SqlType.SELECT)
                .getCursor()

    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun getType(uri: Uri?): String? {
        return null
    }

    private fun getTableName(uri: Uri): String {
        return DataConstant.MUSIC_TABLE_NAME
    }
}