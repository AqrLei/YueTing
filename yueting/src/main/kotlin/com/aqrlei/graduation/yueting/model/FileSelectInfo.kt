package com.aqrlei.graduation.yueting.model

import com.aqrairsigns.aqrleilib.info.FileInfo
import java.io.Serializable

/**
 * created by AqrLei at 21:49 on 星期四, 五月 03, 2018
 */
data class FileSelectInfo(var status: Int = UNSELECTED, var fileInfo: FileInfo) : Serializable {
    companion object {
        const val SELECTED = 1
        const val UNSELECTED = 0
    }
}