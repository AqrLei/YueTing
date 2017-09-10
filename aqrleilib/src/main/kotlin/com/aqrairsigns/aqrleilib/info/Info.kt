package com.aqrairsigns.aqrleilib.info

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/8 Time: 17:00
 */
class Info {
    data class FileInfo(
            var name: String = "",
            var path: String = "",
            var isDir: Boolean = true,
            var parentPath: String? = ""
    ) : Comparable<FileInfo> {
        override fun compareTo(other: FileInfo): Int {
            if (this.name < other.name)
                return -1
            if (this.name > other.name)
                return 1
            return 0
        }
    }

}