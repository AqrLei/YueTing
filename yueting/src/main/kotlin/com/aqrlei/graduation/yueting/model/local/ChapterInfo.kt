package com.aqrlei.graduation.yueting.model.local

import java.io.Serializable

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2018/2/7.
 */
class ChapterInfo(
        var chapterName: String = "",
        var bPosition: Int = 0
) : BookInfo(),
        Serializable {
    override fun equals(other: Any?): Boolean {
        if (other is ChapterInfo) {

            return other.chapterName == this.chapterName && other.path == this.path
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        val result = 17
        return (result * 31 + super.hashCode())

    }
}