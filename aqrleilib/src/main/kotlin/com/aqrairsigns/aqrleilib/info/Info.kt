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
            var isDir: Boolean = false,
            var parentPath: String? = ""
    )
}