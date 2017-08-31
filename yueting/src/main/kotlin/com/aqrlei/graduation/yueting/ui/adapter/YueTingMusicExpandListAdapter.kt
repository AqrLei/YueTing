package com.aqrlei.graduation.yueting.ui.adapter

import android.content.Context
import android.widget.TextView
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.baselib.util.adapter.CommonExpandListAdapter
import com.aqrlei.graduation.yueting.baselib.util.adapter.CommonListViewHolder
import com.aqrlei.graduation.yueting.model.local.ExpandMusicMessage
import com.aqrlei.graduation.yueting.model.local.MusicMessage

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/31
 */
class YueTingMusicExpandListAdapter(
        context: Context,
        data: List<ExpandMusicMessage>,
        childResId: Int,
        groupResId: Int,
        listener:
        CommonExpandListAdapter.OnExpandListInternalClick? = null) :
        CommonExpandListAdapter<ExpandMusicMessage>(context, data,
                childResId, groupResId, listener) {

    override fun getGroup(groupPosition: Int) = mData[groupPosition].groupName


    override fun getChildrenCount(groupPosition: Int) =
            mData[groupPosition].musicMessage?.size ?: 0

    override fun getChild(groupPosition: Int, childPosition: Int) =
            mData[groupPosition].musicMessage?.get(childPosition) ?: null

    override fun bindData(holder: CommonListViewHolder, data: List<ExpandMusicMessage>,
                          groupPosition: Int, childPosition: Int, type: Int) {
        when (type) {
            TYPE_GROUP -> {
                (holder.get(R.id.tv_title_name) as TextView).text = getGroup(groupPosition)
            }
            TYPE_CHILD -> {
                val childData = getChild(groupPosition, childPosition) as MusicMessage
                (holder.get(R.id.tv_music_name) as TextView).text = childData.musicName
                (holder.get(R.id.tv_singer_name) as TextView).text = childData.singerName
                (holder.get(R.id.tv_play_time) as TextView).text = childData.playTime.toString()
            }
        }

    }

    override fun setExpandListInternalClick(holder: CommonListViewHolder, type: Int) {
        when (type) {
            TYPE_GROUP -> holder.get(R.id.iv_music_group_setting).setOnClickListener(this)
            TYPE_CHILD -> holder.get(R.id.tv_music_manager).setOnClickListener(this)
        }
    }
}