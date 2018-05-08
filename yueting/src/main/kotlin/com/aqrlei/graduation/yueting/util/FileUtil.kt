package com.aqrlei.graduation.yueting.util

import com.aqrlei.graduation.yueting.constant.AppConstant
import com.aqrlei.graduation.yueting.model.FileInfo
import java.io.File

/**
 * @Author: AqrLei
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
        fileInfoList.sort()
        return fileInfoList
    }

    fun getFileSuffix(data: FileInfo): String {
        val name = File(data.path).name
        return name.substring(name.lastIndexOf(".") + 1, name.length).toLowerCase()
    }

    fun getFileSuffix(filePath: String): String {
        val name = File(filePath).name
        return name.substring(name.lastIndexOf(".") + 1, name.length)
    }

}