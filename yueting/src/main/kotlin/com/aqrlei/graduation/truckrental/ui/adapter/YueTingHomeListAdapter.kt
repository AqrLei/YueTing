package com.aqrlei.graduation.truckrental.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.aqrlei.graduation.truckrental.R
import com.aqrlei.graduation.truckrental.baselib.util.adapter.CommonListViewHolder
import com.aqrlei.graduation.truckrental.model.local.MusicMessage
import com.aqrlei.graduation.truckrental.model.local.ReadMessage

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/28
 */
class YueTingHomeListAdapter(private var mContext: Context,
                             private var titleResId: Int,
                             private var readResId: Int,
                             private var musicResId: Int,
                             private var mReadData: List<Any>,
                             private var mMusicData: List<Any>
) : BaseAdapter() {
    private val mTitleData = ArrayList<String>()

    init {
        mTitleData.add("悦读")
        mTitleData.add("星听")
    }

    companion object {
        val TYPE_TITLE: Int = 0
        val TYPE_READ: Int = 1
        val TYPE_MUSIC: Int = 2
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var titleViewHolder: CommonListViewHolder
        var readViewHolder: CommonListViewHolder
        var musicViewHolder: CommonListViewHolder
        when (getItemViewType(position)) {
            TYPE_TITLE -> {
                titleViewHolder = CommonListViewHolder.getCommonViewHolder(mContext, titleResId,
                        position, convertView, parent)
                bindData(titleViewHolder, getItem(position), TYPE_TITLE)
                convertView = titleViewHolder.convertView
                convertView.isClickable = true
            }
            TYPE_READ -> {
                readViewHolder = CommonListViewHolder.getCommonViewHolder(mContext, readResId,
                        position, convertView, parent)
                bindData(readViewHolder, getItem(position), TYPE_READ)
                convertView = readViewHolder.convertView
            }
            TYPE_MUSIC -> {
                musicViewHolder = CommonListViewHolder.getCommonViewHolder(mContext, musicResId,
                        position, convertView, parent)
                bindData(musicViewHolder, getItem(position), TYPE_MUSIC)
                convertView = musicViewHolder.convertView
            }
        }
        return convertView!!
    }

    override fun getItem(position: Int) =
            when (getItemViewType(position)) {
                TYPE_TITLE -> if (position == 0) mTitleData[position]
                else mTitleData[position - mReadData.size]
                TYPE_READ -> mReadData[position - 1]
                TYPE_MUSIC -> mMusicData[position - mReadData.size - 2]
                else -> {

                }
            }

    override fun getItemViewType(position: Int) =
            when (position) {
                0 -> TYPE_TITLE
                in 1..mReadData.size -> TYPE_READ
                mReadData.size + 1 -> TYPE_TITLE
                else -> {
                    TYPE_MUSIC
                }
            }

    private fun bindData(holder: CommonListViewHolder, data: Any, viewType: Int) {
        when (viewType) {
            TYPE_TITLE -> {
                (holder.get(R.id.tv_title_name) as TextView).text = data.toString()

            }
            TYPE_READ -> {
                (holder.get(R.id.tv_book_name) as TextView).text = (data as ReadMessage).bookName
            }
            TYPE_MUSIC -> {
                (holder.get(R.id.tv_music_name) as TextView).text =
                        (data as MusicMessage).musicName
                (holder.get(R.id.tv_singer_name) as TextView).text =
                        data.singerName
                (holder.get(R.id.tv_play_time) as TextView).text =
                        data.playTime.toString()
            }
        }
    }

    override fun getItemId(position: Int) = position.toLong()
    override fun getCount() = mReadData.size + mMusicData.size + mTitleData.size
    override fun getViewTypeCount() = 3

}