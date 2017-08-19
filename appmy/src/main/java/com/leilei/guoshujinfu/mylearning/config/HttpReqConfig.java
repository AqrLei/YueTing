package com.leilei.guoshujinfu.mylearning.config;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/4
 */

public class HttpReqConfig {
    public static final String HTTP_BASE_URL = BuildConfig.SERVICE_ADDRESS;
    public static final long HTTP_CONNECT_TIME_OUT = 60L;
    public static final long HTTP_READ_TIME_OUT = 60L;

    public static final String CONTENT_TYPE_JSON = "Content-Type: application/json";
    public static final String USER_AGENT = "User-Agent";
    public static final String CHANNEL_ID = "channelId";
    public static final String RQ_IMG_TYPE = "2";
}
