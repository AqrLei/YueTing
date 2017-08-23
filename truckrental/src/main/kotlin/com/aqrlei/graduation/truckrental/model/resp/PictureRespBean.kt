package com.aqrlei.graduation.truckrental.model.resp

import com.google.gson.annotations.SerializedName

import java.io.Serializable

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/4
 */

/**
 * {"bannerImg":url}
 */
class PictureRespBean : Serializable {
    @SerializedName("bannerImg")
    var pictureUrl: String? = null
}

