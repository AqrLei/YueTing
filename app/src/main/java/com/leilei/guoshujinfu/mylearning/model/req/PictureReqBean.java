package com.leilei.guoshujinfu.mylearning.model.req;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/4
 */

public final class PictureReqBean {
    private String type;



    public PictureReqBean() {

    }

    public PictureReqBean(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
