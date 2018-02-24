package com.aqrlei.graduation.yueting.ui.adapter

import android.content.Context
import android.widget.TextView
import com.aqrairsigns.aqrleilib.adapter.CommonListAdapter
import com.aqrairsigns.aqrleilib.adapter.CommonListViewHolder
import com.aqrairsigns.aqrleilib.util.ImageUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.aidl.MusicInfo
import com.aqrlei.graduation.yueting.model.local.BookInfo
import com.facebook.drawee.view.SimpleDraweeView

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/29
 */
/**
 * @param type 0 book, 1 music.
 */
class YueTingListAdapter(mData: List<Any>, mContext: Context, mResId: Int, type: Int) :
        CommonListAdapter<Any>(mData, mContext, mResId) {
    private val mType = type
    override fun bindData(holderList: CommonListViewHolder, data: Any) {
        when (mType) {
            0 -> {
                (holderList.get(R.id.tv_book_name) as TextView).text = (data as BookInfo).name
            }
            1 -> {
                (holderList.get(R.id.tv_music_name) as TextView).text =
                        (data as MusicInfo).title
                (holderList.get(R.id.tv_singer_name) as TextView).text =
                        data.artist
                (holderList.get(R.id.tv_play_time) as TextView).text =
                        data.duration.toString()
                (holderList.get(R.id.sdv_music_picture) as SimpleDraweeView).background =
                        ImageUtil.byteArrayToDrawable(data.picture) ?: mContext.getDrawable(R.mipmap.ic_launcher_round)
            }
        }
    }


    override fun setInternalClick(holder: CommonListViewHolder) {

    }
}