package com.aqrlei.graduation.yueting.presenter.activitypresenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.info.FileInfo
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrairsigns.aqrleilib.util.FileUtil
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.ui.FileActivity
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/11 Time: 13:41
 */
class FileActivityPresenter(mMvpActivity: FileActivity) :
        MvpContract.ActivityPresenter<FileActivity>(mMvpActivity) {
    fun getFileInfo(path: String) {
        mMvpActivity.changeFileInfo(FileUtil.createFileInfoS(path))
    }

    fun addToDataBase(data: ArrayList<FileInfo>) {

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        DBManager.createDB()
        for (i in 0 until data.size) {
            val suffix = FileUtil.getFileSuffix(data[i])

            if (suffix != "mp3" && suffix != "ape") continue

            val dateTime = dateFormatter.format(Date(System.currentTimeMillis()))
            DBManager.sqlData(
                    DBManager.SqlFormat.insertSqlFormat(YueTingConstant.MUSIC_TABLE_NAME,
                            arrayOf("path", "createTime")),
                    arrayOf(data[i].path, dateTime), null, DBManager.SqlType.INSERT)

        }
    }

}