package com.aqrairsigns.aqrleilib.util.file

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.aqrairsigns.aqrleilib.constant.AppConstant
import java.io.File

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/8 Time: 14:30
 */
object FileUtil {
    private val filePath = ArrayList<String>()
    private val fileName = ArrayList<String>()
    fun selectFileDir(path: String = AppConstant.ROOT_PATH) {
        fileName.clear()
        filePath.clear()
        var file = File(path)
        var files = file.listFiles()
        if (!AppConstant.ROOT_PATH.equals(path)) {
            fileName.add("@1")
            filePath.add(AppConstant.ROOT_PATH)

            fileName.add("@2")
            filePath.add(file.parent)
        }
        for (f in files) {
            fileName.add(f.name)
            filePath.add(f.path)
        }
    }

    fun getFileName() = fileName
    fun getFilePath() = filePath

    fun deleteFile(file: File) {

        //context.startActivity()

    }

    fun openFile(file: File, context: Context) {
        if(file.isDirectory) {
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