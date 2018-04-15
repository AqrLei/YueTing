package com.aqrlei.graduation.yueting.model.local

import android.graphics.drawable.Drawable
import java.io.Serializable

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/28
 */
open class BookInfo(
        var name: String = "",
        var bookIcon: Drawable? = null,
        var type: String = "txt",//0 : txt; 1 : pdf
        var id: Int = 0,
        var path: String = "",
        var encoding: String = "GBK",
        var accessTime: Long = 0,
        var createTime: String = "",
        var fileLength: Int = 0,
        var indexBegin: Int = 0,
        var indexEnd: Int = 0
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (other is BookInfo) {
            return other.path == this.path
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = 17
        result = result * 31 + name.hashCode()
        result = result * 31 + id
        result = result * 31 + type.hashCode()
        result = result * 31 + path.hashCode()
        result = result * 31 + encoding.hashCode()
        result = result * 31 + accessTime.hashCode()
        result = result * 31 + createTime.hashCode()
        result = result * 31 + fileLength
        result = result * 31 + indexBegin
        result = result * 31 + indexEnd
        return result
    }
}