package com.aqrairsigns.aqrleilib.util.file

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.aqrairsigns.aqrleilib.constant.AppConstant
import com.aqrairsigns.aqrleilib.info.Info
import java.io.File

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/8 Time: 14:30
 */
object FileUtil {
    private val fileInfoList = ArrayList<Info.FileInfo>()
    fun createFileTree(path: String = AppConstant.ROOT_PATH): ArrayList<Info.FileInfo> {
        fileInfoList.clear()
        val fileInfo = Info.FileInfo()
        val file = File(path)
        val files = file.listFiles()
        fileInfo.path = path
        fileInfo.isDir = file.isDirectory
        if (!AppConstant.ROOT_PATH.equals(path)) {
            fileInfo.name = "@1"
            fileInfo.path = AppConstant.ROOT_PATH
            fileInfoList.add(fileInfo)
            fileInfo.name = "@2"
            fileInfo.parentPath = file.parent.toString()
            fileInfoList.add(fileInfo)
        }
        files.forEach { f ->
            fileInfo.name = f.name
            fileInfo.path = f.path
            fileInfo.isDir = f.isDirectory
            fileInfo.parentPath = f.parent
            fileInfoList.add(fileInfo)
        }
        return fileInfoList
    }

    fun getfileInfoList() = fileInfoList

    fun deleteFile(file: File) = file.delete()

    fun openFile(file: File, context: Context) {
        if (file.isDirectory) {
            return
        }
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val type = getMIMEType(file)
        intent.setDataAndType(Uri.fromFile(file), type)
        context.startActivity(intent)
    }

    private fun getMIMEType(file: File): String {
        var type = ""
        val name = file.name
        val end = name.substring(name.lastIndexOf(".") + 1, name.length).toLowerCase()
        for (i in AppConstant.MIME_TYPE.indices) {
            type = if (end.equals(AppConstant.MIME_TYPE[i][0])) {
                AppConstant.MIME_TYPE[i][1]
            } else {
                "*"
            }
        }
        type += "/*"
        return type
    }
}