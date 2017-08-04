package com.leilei.guoshujinfu.mylearning.model.resp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/4
 */

/**
 * {"name":"leilei"}
 */
public class PictureRespBean implements Serializable {
    @SerializedName("bannerImg")
    private String pictureUrl;


    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}

