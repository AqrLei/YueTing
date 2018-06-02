package com.aqrlei.graduation.yueting.presenter

import android.graphics.Path
import com.aqrlei.graduation.yueting.basemvp.MvpContract
import com.aqrlei.graduation.yueting.ui.MainActivity


/**
 * Author: AqrLei
 * Date: 2017/8/22
 */
class MainPresenter(mMvpActivity: MainActivity) :
        MvpContract.ActivityPresenter<MainActivity>(mMvpActivity) {

    enum class PathType {
        TOP_RIGHT, TOP_LEFT, BOTTOM_RIGHT, BOTTOM_LEFT, CENTER_LEFT, CENTER_RIGHT
    }

    fun getPath(type: PathType, h: Float, w: Float): Path {
        return when (type) {
            PathType.BOTTOM_LEFT -> {

                getBottomLeftPath(h, w)
            }
            PathType.BOTTOM_RIGHT -> {
                getBottomRightPath(h, w)
            }
            PathType.TOP_LEFT -> {
                getTopLeftPath(h, w)
            }
            PathType.TOP_RIGHT -> {
                getTopRightPath(h, w)
            }
            PathType.CENTER_LEFT -> {
                getHexagramLeftPath(h, w)
            }
            PathType.CENTER_RIGHT -> {
                getHexagramRightPath(h, w)
            }
        }
    }

    private fun getHexagramRightPath(h: Float, w: Float): Path {
        return Path().apply {
            moveTo(w / 2F, h - (h / 2F - 50F) / 2F)
            lineTo(w / 2F + 50F, h / 2F + 50F)
            lineTo(w / 2F + 150F, h / 2F + 50F)
            lineTo(w / 2F + 100F, h / 2F)
            lineTo(w / 2F + 150F, h / 2F - 50F)
            lineTo(w / 2F + 50F, h / 2F - 50F)
            lineTo(w / 2F, (h / 2F - 50F) / 2F)
        }
    }

    private fun getHexagramLeftPath(h: Float, w: Float): Path {
        return Path().apply {
            moveTo(w / 2F, (h / 2F - 50F) / 2F)
            lineTo(w / 2F - 50F, h / 2F - 50F)
            lineTo(w / 2F - 150F, h / 2F - 50F)
            lineTo(w / 2F - 100F, h / 2F)
            lineTo(w / 2F - 150F, h / 2F + 50F)
            lineTo(w / 2F - 50F, h / 2F + 50F)
            lineTo(w / 2F, h - (h / 2F - 50F) / 2F)
        }
    }

    private fun getTopRightPath(h: Float, w: Float): Path {

        return Path().apply {
            moveTo(w / 2F + 50F, 0F)
            lineTo(w / 2F + 50F, (h / 2F - 50F) / 2F)
            lineTo((w / 2F + 50F + (w / 2F - 50F) / 2F), h / 2F - 50F)
            lineTo(w, h / 2F - 50F)
        }
    }

    private fun getBottomRightPath(h: Float, w: Float): Path {
        return Path().apply {
            moveTo(w / 2F + 50F, h)
            lineTo(w / 2F + 50F, h - (h / 2F - 50F) / 2F)
            lineTo((w / 2F + 50F + (w / 2F - 50F) / 2F), h / 2F + 50F)
            lineTo(w, h / 2F + 50F)
        }
    }

    private fun getTopLeftPath(h: Float, w: Float): Path {
        return Path().apply {
            moveTo(w / 2F - 50F, 0F)
            lineTo(w / 2F - 50F, (h / 2F - 50F) / 2F)
            lineTo((w / 2F - 50F) / 2F, h / 2F - 50F)
            lineTo(0F, h / 2F - 50F)
        }
    }

    private fun getBottomLeftPath(h: Float, w: Float): Path {
        return Path().apply {
            moveTo(w / 2F - 50F, h)
            lineTo(w / 2F - 50F, h - (h / 2F - 50F) / 2F)
            lineTo((w / 2F - 50F) / 2F, h / 2F + 50F)
            lineTo(0F, h / 2F + 50F)
        }
    }
}








