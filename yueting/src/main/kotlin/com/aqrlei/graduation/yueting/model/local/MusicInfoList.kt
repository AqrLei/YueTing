package com.aqrlei.graduation.yueting.model.local

import android.os.Parcel
import android.os.Parcelable

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/9/22.
 */
data class MusicInfoList(
        var musicInfoList: ArrayList<MusicInfo>? = null)
    : Parcelable {
    constructor(parcel: Parcel) : this() {
        parcel.createTypedArrayList(MusicInfo.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(musicInfoList)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MusicInfoList> {
        override fun createFromParcel(parcel: Parcel): MusicInfoList {
            return MusicInfoList(parcel)
        }

        override fun newArray(size: Int): Array<MusicInfoList?> {
            return arrayOfNulls(size)
        }
    }
}

