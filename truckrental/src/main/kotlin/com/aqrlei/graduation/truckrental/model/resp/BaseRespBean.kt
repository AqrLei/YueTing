package com.aqrlei.graduation.truckrental.model.resp

import java.io.Serializable

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/4
 */

class BaseRespBean<T> : Serializable {
    var success: Boolean? = null
    var errorCode: String? = null
    var errorMsg: String? = null
    var data: T? = null

    constructor() {}

    constructor(success: Boolean?, errorCode: String, errorMsg: String, data: T) {
        this.success = success
        this.errorCode = errorCode
        this.errorMsg = errorMsg
        this.data = data
    }

    override fun toString(): String {
        return "BaseRespBean{" +
                "success=" + success +
                ", errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}'
    }

}
