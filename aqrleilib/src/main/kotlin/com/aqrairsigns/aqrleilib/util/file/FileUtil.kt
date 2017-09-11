package com.aqrairsigns.aqrleilib.util.file

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.aqrairsigns.aqrleilib.constant.AppConstant
import com.aqrairsigns.aqrleilib.info.Info.FileInfo
import java.io.File
import java.util.*

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/8 Time: 14:30
 */
object FileUtil {
    private val fileInfoList = ArrayList<FileInfo>()
    fun createFileInfoS(dirPath: String = AppConstant.ROOT_PATH): ArrayList<FileInfo> {
        fileInfoList.clear()
        val fileInfoParent = FileInfo()
        val file = File(dirPath)
        val files = file.listFiles()
        fileInfoParent.name = "@1"
        fileInfoParent.path = file.path
        fileInfoParent.parentPath = if (file.parent == null) AppConstant.ROOT_PATH else file.parent
        fileInfoList.add(fileInfoParent)
        files.forEach { f ->
            if (f.canRead() && !f.isHidden) {
                val fileInfo = FileInfo()
                fileInfo.name = f.name
                fileInfo.path = f.path
                fileInfo.isDir = f.isDirectory
                fileInfo.parentPath = f.parent
                fileInfoList.add(fileInfo)
            }
        }
        Collections.sort(fileInfoList)
        return fileInfoList
    }

    fun deleteFolder(path: String) {
        val folder = File(path)
        if (folder.isDirectory) {
            folder.listFiles().forEach { f ->
                if (f.isDirectory) deleteFolder(f.path)
                else f.delete()
            }
        }
        folder.delete()
    }


    fun openFile(filePath: String, context: Context) {
        val file = File(filePath)
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