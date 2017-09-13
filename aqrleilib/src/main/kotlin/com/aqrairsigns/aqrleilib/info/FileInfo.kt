package com.aqrairsigns.aqrleilib.info

import com.aqrairsigns.aqrleilib.constant.AppConstant

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/8 Time: 17:00
 */

data class FileInfo(
        var name: String = "",
        var path: String = "",
        var isDir: Boolean = true,
        var parentPath: String = AppConstant.ROOT_PATH
) : Comparable<FileInfo> {
    override fun compareTo(other: FileInfo): Int {
        val thisName = this.name.toLowerCase()
        val otherName = other.name.toLowerCase()
        if (otherName == "@1" || (!this.isDir && other.isDir)) {
            return 1
        }
        if (thisName == "@1" || (this.isDir && !other.isDir)) {
            return -1
        }
        if ((this.isDir && other.isDir) || (!this.isDir && !other.isDir)) {
            if (otherName >= "一" || thisName >= "一") {
                if (thisName < otherName)
                    return 1
                if (thisName > otherName)
                    return -1
                return 0
            } else {
                if (thisName < otherName)
                    return -1
                if (thisName > otherName)
                    return 1
                return 0
            }
        }
        return 0
    }
}