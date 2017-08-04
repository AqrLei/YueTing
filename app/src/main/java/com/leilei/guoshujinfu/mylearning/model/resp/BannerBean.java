package com.leilei.guoshujinfu.mylearning.model.resp;

import java.util.List;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/4
 */

public class BannerBean {
    private List<PictureRespBean> bannerConfig;

    public List<PictureRespBean> getBannerConfig() {
        return bannerConfig;
    }

    public void setBannerConfig(List<PictureRespBean> bannerConfig) {
        this.bannerConfig = bannerConfig;
    }
}
