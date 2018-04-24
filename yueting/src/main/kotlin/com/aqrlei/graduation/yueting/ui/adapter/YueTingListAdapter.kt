package com.aqrlei.graduation.yueting.ui.adapter

import android.content.Context
import android.widget.TextView
import com.aqrairsigns.aqrleilib.adapter.CommonListAdapter
import com.aqrairsigns.aqrleilib.adapter.CommonListViewHolder
import com.aqrairsigns.aqrleilib.util.DateFormatUtil
import com.aqrairsigns.aqrleilib.util.ImageUtil
import com.aqrairsigns.aqrleilib.util.StringChangeUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.aidl.MusicInfo
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.BookInfo
import com.facebook.drawee.view.SimpleDraweeView
import java.text.DecimalFormat

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/29
 */
/**
 * @param type 0 book, 1 music.
 */
class YueTingListAdapter(mData: List<Any>, mContext: Context, mResId: Int, val type: Int) :
        CommonListAdapter<Any>(mData, mContext, mResId) {
    override fun bindData(holderList: CommonListViewHolder, data: Any) {
        when (type) {
            YueTingConstant.ADAPTER_TYPE_BOOK -> {
                StringChangeUtil.SPANNABLE.clear()
                val bookInfo = data as BookInfo
                val percent =
                        if (bookInfo.type == YueTingConstant.READ_SUFFIX_PDF) {
                            if (bookInfo.indexEnd == 0) "" else
                                "${bookInfo.indexBegin + 1}/ ${bookInfo.indexEnd}"
                        } else {
                            "${DecimalFormat("#00.00").format(
                                    bookInfo.indexBegin.toFloat()
                                            / bookInfo.fileLength.toFloat() * 100F)}%"
                        }
                (holderList.get(R.id.tv_book_name) as TextView).text = bookInfo.name
                (holderList.get(R.id.tv_book_progress) as TextView).text = percent
            }
            YueTingConstant.ADAPTER_TYPE_MUSIC -> {
                (holderList.get(R.id.tv_music_name) as TextView).text =
                        (data as MusicInfo).title
                (holderList.get(R.id.tv_singer_name) as TextView).text =
                        data.artist
                (holderList.get(R.id.tv_play_time) as TextView).text =
                        DateFormatUtil.simpleTimeFormat(data.duration.toLong())

                (holderList.get(R.id.sdv_music_picture) as SimpleDraweeView).background =
                        ImageUtil.byteArrayToDrawable(data.picture)
                        ?: mContext.getDrawable(R.drawable.music_selector_note)
            }
            YueTingConstant.ADAPTER_TYPE_TITLE_BOOK -> {
                (holderList.get(R.id.titleNameTv) as TextView).text = data as String
            }
            YueTingConstant.ADAPTER_TYPE_TITLE_MUSIC -> {
                (holderList.get(R.id.titleNameTv) as TextView).apply {
                    text = data as String
                    compoundDrawables[0].level = 1
                }

            }
        }
    }

    override fun setInternalClick(holder: CommonListViewHolder) {}
}