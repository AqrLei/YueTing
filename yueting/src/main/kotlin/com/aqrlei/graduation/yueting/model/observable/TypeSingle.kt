package com.aqrlei.graduation.yueting.model.observable

import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.DateFormatUtil
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.model.SelectInfo
import com.aqrlei.graduation.yueting.util.threadSwitch
import io.reactivex.Single

/**
 * @author  aqrLei on 2018/5/2
 */
object TypeSingle {
    fun selectType(type: String): Single<ArrayList<String>> {
        return Single.defer {
            val typeList = ArrayList<String>()
            DBManager.sqlData(
                    DBManager.SqlFormat.selectSqlFormat(
                            DataConstant.TYPE_TABLE_NAME,
                            DataConstant.TYPE_TABLE_C0_NAME,
                            DataConstant.TYPE_TABLE_C1_TYPE,
                            "="),
                    null, arrayOf(type), DBManager.SqlType.SELECT)
                    .getCursor()?.apply {
                        while (moveToNext()) {
                            val temp = getString(getColumnIndex(DataConstant.TYPE_TABLE_C0_NAME))
                            typeList.add(temp)
                        }
                        close()
                    }
            Single.just(typeList)
        }.threadSwitch()
    }

    fun insertType(type: String, name: String): Single<Boolean> {
        return Single.defer {
            val dateTime = DateFormatUtil.simpleDateFormat(System.currentTimeMillis())
            DBManager.sqlData(
                    DBManager.SqlFormat.insertSqlFormat(
                            DataConstant.TYPE_TABLE_NAME,
                            arrayOf(DataConstant.TYPE_TABLE_C0_NAME,
                                    DataConstant.TYPE_TABLE_C1_TYPE,
                                    DataConstant.COMMON_COLUMN_CREATE_TIME)),
                    arrayOf(name, type, dateTime),
                    null,
                    DBManager.SqlType.INSERT)
            Single.just(DBManager.finish())
        }.threadSwitch()
    }

    fun deleteType(nameList: List<SelectInfo>): Single<Boolean> {
        return Single.defer {
            nameList.filter { it.status == SelectInfo.SELECTED }
                    .forEach {
                        DBManager.sqlData(
                                DBManager.SqlFormat.deleteSqlFormat(DataConstant.TYPE_TABLE_NAME,
                                        DataConstant.TYPE_TABLE_C0_NAME, "="),
                                null, arrayOf(it.name), DBManager.SqlType.DELETE)
                    }
            Single.just(DBManager.finish())
        }.threadSwitch()
    }

    fun updateType(oldTypeName: String, newTypeName: String): Single<Boolean> {
        return Single.defer {
            DBManager.sqlData(
                    DBManager.SqlFormat.updateSqlFormat(
                            DataConstant.TYPE_TABLE_NAME,
                            DataConstant.TYPE_TABLE_C0_NAME,
                            DataConstant.TYPE_TABLE_C0_NAME, "="),
                    arrayOf(newTypeName, oldTypeName),
                    null,
                    DBManager.SqlType.UPDATE)
            Single.just(DBManager.finish())
        }.threadSwitch()
    }
}