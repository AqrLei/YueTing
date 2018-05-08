package com.aqrlei.graduation.yueting.ui.adapter

import android.content.Context
import android.widget.TextView
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.adapter.CommonListAdapter
import com.aqrlei.graduation.yueting.adapter.CommonListViewHolder
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.BookInfo
import com.aqrlei.graduation.yueting.model.MusicInfo
import com.aqrlei.graduation.yueting.util.DateFormatUtil
import com.aqrlei.graduation.yueting.util.ImageUtil
import com.facebook.drawee.view.SimpleDraweeView
import java.text.DecimalFormat

/**
 * @Author: AqrLei
 * @Date: 2017/8/29
 */
class YueTingListAdapter(mData: List<Any>, mContext: Context, mResId: Int, val type: Int) :
        CommonListAdapter<Any>(mData, mContext, mResId) {
    override fun bindData(holderList: CommonListViewHolder, t: Any) {
        when (type) {
            YueTingConstant.ADAPTER_TYPE_BOOK -> {
                val bookInfo = t as BookInfo
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
                        (t as MusicInfo).title
                (holderList.get(R.id.tv_singer_name) as TextView).text =
                        t.artist
                (holderList.get(R.id.tv_play_time) as TextView).text =
                        DateFormatUtil.simpleTimeFormat(t.duration.toLong())

                (holderList.get(R.id.sdv_music_picture) as SimpleDraweeView).background =
                        ImageUtil.byteArrayToDrawable(t.picture)
                        ?: mContext.getDrawable(R.drawable.music_selector_note)
            }
            YueTingConstant.ADAPTER_TYPE_TITLE_BOOK -> {
                (holderList.get(R.id.titleNameTv) as TextView).text = t as String
            }
            YueTingConstant.ADAPTER_TYPE_TITLE_MUSIC -> {
                (holderList.get(R.id.titleNameTv) as TextView).apply {
                    text = t as String
                    compoundDrawables[0].level = 1
                }

            }
        }
    }

    override fun setInternalClick(holder: CommonListViewHolder, position: Int) {}
}