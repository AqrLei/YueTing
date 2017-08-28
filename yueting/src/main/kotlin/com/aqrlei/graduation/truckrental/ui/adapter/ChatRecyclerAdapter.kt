package com.aqrlei.graduation.truckrental.ui.adapter

import com.aqrlei.graduation.truckrental.baselib.util.adapter.CommonRecyclerViewHolder
import com.aqrlei.graduation.truckrental.baselib.util.adapter.CommonRecylerAdapter
import com.aqrlei.graduation.truckrental.model.local.ChatMessage

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