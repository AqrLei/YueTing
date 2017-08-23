package com.aqrlei.graduation.truckrental.baselib.util.net.service;


import com.aqrlei.graduation.truckrental.baselib.util.net.HttpReqConstants;
import com.aqrlei.graduation.truckrental.baselib.util.net.config.HttpReqConfig;
import com.aqrlei.graduation.truckrental.model.req.PictureReqBean;
import com.aqrlei.graduation.truckrental.model.resp.BannerBean;
import com.aqrlei.graduation.truckrental.model.resp.BaseRespBean;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/4
 */

public interface PictureInfoService {
    /*"Content-Type: application/json"*/
    @Headers(HttpReqConfig.CONTENT_TYPE_JSON)
    /*"v4/client/config"*/
    @POST(HttpReqConstants.PICTURE_LIST_INFO)
    Observable<Response<BaseRespBean<BannerBean>>> getPicture(@Body PictureReqBean pictureReqBean);


}
