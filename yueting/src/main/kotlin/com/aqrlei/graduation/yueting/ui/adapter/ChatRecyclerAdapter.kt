package com.aqrlei.graduation.yueting.ui.adapter

import com.aqrairsigns.aqrleilib.adapter.CommonRecyclerViewHolder
import com.aqrairsigns.aqrleilib.adapter.CommonRecylerAdapter
import com.aqrlei.graduation.yueting.model.local.ChatMessage

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/25
 */
class ChatRecyclerAdapter(mData: List<ChatMessage>, resId: Int) :
        CommonRecylerAdapter<ChatMessage>(mData, resId) {
    override fun bindData(holder: CommonRecyclerViewHolder, data: List<ChatMessage>, position: Int) {

    }
}