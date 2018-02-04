package com.aqrlei.graduation.yueting.model.local

import android.graphics.drawable.Drawable
import java.io.Serializable

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/28
 */
data class BookInfo(

        var name: String = "",
        var bookIcon: Drawable? = null,
        var id: Int = 0,
        var path: String = "",
        var encoding: String = "",
        var accessTime: Long = 0,
        var createTime: String = ""
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (other is BookInfo) {

            return other.name == this.name && other.path == this.path
        }
        return super.equals(other)
    }
}