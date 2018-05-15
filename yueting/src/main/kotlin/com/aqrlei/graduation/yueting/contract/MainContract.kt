package com.aqrlei.graduation.yueting.contract

import android.graphics.Path
import com.aqrlei.graduation.yueting.mvp.BasePresenter
import com.aqrlei.graduation.yueting.mvp.BaseView

/**
 * @author  aqrLei on 2018/5/15
 */
interface MainContract {
    enum class PathType {
        TOP_RIGHT, TOP_LEFT, BOTTOM_RIGHT, BOTTOM_LEFT, CENTER_LEFT, CENTER_RIGHT
    }

    interface Presenter : BasePresenter {

        fun getPath(type: PathType, h: Float, w: Float)
    }

    interface View : BaseView {
        fun setPath(type: PathType, path: Path)
    }
}