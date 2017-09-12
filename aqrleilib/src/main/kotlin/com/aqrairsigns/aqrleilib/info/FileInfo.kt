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
        if (other.name == "@1") {
            return 1
        }
        if (this.name == "@1") {
            return -1
        }
        if (!this.isDir && other.isDir) {
            return 1
        }
        if (this.isDir && !other.isDir) {
            return -1
        }
        if (this.isDir && other.isDir) {
            if (other.name >= "一" || this.name >= "一") {
                if (this.name < other.name)
                    return 1
                if (this.name > other.name)
                    return -1
                return 0
            } else {
                if (this.name < other.name)
                    return -1
                if (this.name > other.name)
                    return 1
                return 0
            }
        }
        if (!this.isDir && !other.isDir) {
            if (other.name >= "一" || this.name >= "一") {
                if (this.name < other.name)
                    return 1
                if (this.name > other.name)
                    return -1
                return 0
            } else {
                if (this.name < other.name)
                    return -1
                if (this.name > other.name)
                    return 1
                return 0
            }
        }
        return 0
    }
}