package com.aqrlei.graduation.yueting.model.local

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/31
 */
data class ExpandMusicMessage(var groupName: String = "",
                              var musicMessage: List<MusicMessage>? = null)