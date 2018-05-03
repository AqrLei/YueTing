package com.aqrairsigns.aqrleilib.info

import java.util.*

/**
 * @Author: AqrLei
 * @CreateTime: Date: 2017/9/12 Time: 14:53
 */
data class DataTableInfo(
        var name: String = " ",
        var fileId: Array<String>? = null,
        var fileType: Array<String>? = null
) {
    override fun equals(other: Any?): Boolean {

        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataTableInfo
        if (this.name == other.name) return true
        if (name != other.name) return false
        /*if (!Arrays.equals(fileId, other.fileId)) return false
        if (!Arrays.equals(fileType, other.fileType)) return false*/

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (fileId?.let { Arrays.hashCode(it) } ?: 0)
        result = 31 * result + (fileType?.let { Arrays.hashCode(it) } ?: 0)
        return result
    }
}