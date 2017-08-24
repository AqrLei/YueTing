package com.leilei.guoshujinfu.mylearning.model.resp

import java.io.Serializable

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/4
 */

data class BaseRespBean<T> (var success: Boolean, var errorCode: String, var errorMsg: String, var data: T): Serializable
