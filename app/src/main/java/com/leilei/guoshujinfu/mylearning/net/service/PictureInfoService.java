package com.leilei.guoshujinfu.mylearning.net.service;

import com.leilei.guoshujinfu.mylearning.model.req.PictureReqBean;
import com.leilei.guoshujinfu.mylearning.model.resp.BannerBean;
import com.leilei.guoshujinfu.mylearning.model.resp.BaseRespBean;
import com.leilei.guoshujinfu.mylearning.net.HttpReqConstants;
import com.leilei.guoshujinfu.mylearning.net.config.HttpReqConfig;

import retrofit2.Call;
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
    @Headers(HttpReqConfig.CONTENT_TYPE_JSON)
    @POST(HttpReqConstants.PICTURE_LIST_INFO)
    Observable<Response<BaseRespBean<BannerBean>>> getPicture(@Body PictureReqBean pictureReqBean);

    @Headers(HttpReqConfig.CONTENT_TYPE_JSON)
    @POST(HttpReqConstants.PICTURE_LIST_INFO)
    Call<BaseRespBean<BannerBean>> getBanner(@Body PictureReqBean reqBean);
}
