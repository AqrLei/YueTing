package com.aqrlei.graduation.yueting.model

import java.io.Serializable

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2018/2/7.
 */

class ChapterInfo(
        var chapterName: String = "",
        var bPosition: Int = 0,
        var flag: Boolean = true
) : BookInfo(),
        Serializable {
    override fun equals(other: Any?): Boolean {
        if (other is ChapterInfo) {
            return other.chapterName == this.chapterName && other.path == this.path
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = 17
        result = result * 31 + chapterName.hashCode()
        result = result * 31 + bPosition
        result = result * 31 + flag.hashCode()
        return (result * 31 + super.hashCode())
    }
}