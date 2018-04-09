package com.aqrlei.graduation.yueting.ui.uiEt

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import com.aqrlei.graduation.yueting.constant.ActionConstant
import com.aqrlei.graduation.yueting.enumtype.SendType
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.infotool.ShareMusicInfo

/**
 * created by AqrLei on 2018/4/6
 */
fun sendPlayBroadcast(position: Int, context: Context) {
    val playIntent = Intent(ActionConstant.ACTION_PLAY)
    playIntent.putExtra("position", position)
    context.sendOrderedBroadcast(playIntent, null)
}

fun sendMusicBroadcast(type: SendType, context: Context, code: Int = 1) {
    ShareMusicInfo.MusicInfoTool.sendBroadcast(context, type, code)
}

fun initPlayView(playView: ViewGroup, position: Int, duration: Int = 0) {
    ShareMusicInfo.MusicInfoTool.shareViewInit(playView, position, duration)
}