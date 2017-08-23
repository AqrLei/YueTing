package com.aqrlei.graduation.truckrental.model.req

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/4
 */

/*request bean*/
class PictureReqBean {
    var type: String? = null

    constructor() {

    }

    constructor(type: String) {
        this.type = type
    }
}
