package com.aqrairsigns.aqrleilib.info

import com.aqrairsigns.aqrleilib.constant.AppConstant
import java.util.*

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

    data class DataTableInfo(
            var name: String = " ",
            var fileId: Array<String> = arrayOf(" "),
            var fileType: Array<String> = arrayOf(" ")
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as DataTableInfo

            if (name != other.name) return false
            if (!Arrays.equals(fileId, other.fileId)) return false
            if (!Arrays.equals(fileType, other.fileType)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + Arrays.hashCode(fileId)
            result = 31 * result + Arrays.hashCode(fileType)
            return result
        }
    }

}