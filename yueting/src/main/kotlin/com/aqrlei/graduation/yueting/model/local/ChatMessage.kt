package com.aqrlei.graduation.yueting.model.local

import android.graphics.drawable.Drawable

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/25
 */
data class ChatMessage(var content: String? = "", var type: Int = 0, var avatar: Drawable,
                       var child: List<ChildMessage>?)
