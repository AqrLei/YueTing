package com.leilei.guoshujinfu.mylearning.model.resp

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
data class PictureRespBean(@SerializedName("bannerImg") var pictureUrl: String): Serializable

