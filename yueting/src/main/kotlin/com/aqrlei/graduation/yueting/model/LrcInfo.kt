package com.aqrlei.graduation.yueting.model

/**
 * created by AqrLei at 21:29 on 星期六, 五月 05, 2018
 */
data class LrcInfo(var lrcContent: String = "", var lrcTime: Int = 0) : Comparable<LrcInfo> {
    override fun compareTo(other: LrcInfo): Int {
        return when {
            this.lrcTime < other.lrcTime -> -1
            this.lrcTime > other.lrcTime -> 1
            else -> 0
        }
    }
}
