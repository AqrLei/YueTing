package com.aqrlei.graduation.yueting.model

import java.io.Serializable

/**
 * @author  aqrLei on 2018/5/3
 */
data class SelectInfo(var status: Int = UNSELECTED, var name: String) : Serializable {
    companion object {
        const val SELECTED = 1
        const val UNSELECTED = 0
    }
}
