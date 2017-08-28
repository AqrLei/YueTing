package com.aqrlei.graduation.yueting.baselib.util.net.config

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/4
 */

object HttpReqConfig {
    const val HTTP_BASE_URL = BuildConfig.SERVICE_ADDRESS
    const val HTTP_CONNECT_TIME_OUT = 60L
    const val HTTP_READ_TIME_OUT = 60L

    const val CONTENT_TYPE_JSON = "Content-Type: application/json"
    const val USER_AGENT = "User-Agent"
    const val CHANNEL_ID = "channelId"
    const val RQ_IMG_TYPE = "2"
}
