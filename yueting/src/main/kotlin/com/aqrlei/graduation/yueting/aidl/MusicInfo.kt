package com.aqrlei.graduation.yueting.aidl

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/14 Time: 14:42
 */
data class MusicInfo(
        var id: Int = 0,
        var albumUrl: String = "",
        var type: Int = 3,
        var album: String = "",
        var picture: ByteArray? = null,
        var title: String = "",
        var artist: String = "",
        var duration: Int = 0,
        var createTime: String = ""
) : Comparable<MusicInfo>, Parcelable {
    constructor(parcel: Parcel) : this(
            /*String属性值不为null，但readString()可能为null, 许StrictMode*/
            parcel.readInt(),
            parcel.readString() ?: " ",
            parcel.readInt(),
            parcel.readString() ?: " ",
            parcel.createByteArray(),
            parcel.readString() ?: " ",
            parcel.readString() ?: " ",
            parcel.readInt(),
            parcel.readString() ?: " ")

    override fun writeToParcel(out: Parcel?, flags: Int) {
        out?.writeInt(id)
        out?.writeString(albumUrl)
        out?.writeInt(type)
        out?.writeString(album)
        out?.writeByteArray(picture)
        out?.writeString(title)
        out?.writeString(artist)
        out?.writeInt(duration)
        out?.writeString(createTime)
    }

    override fun describeContents(): Int = 0

    override fun compareTo(other: MusicInfo): Int {
        val thisArtist = this.artist.toLowerCase()
        val otherArtist = other.artist.toLowerCase()
        if (otherArtist >= "一" || thisArtist >= "一") {
            if (thisArtist < otherArtist)
                return 1
            if (thisArtist > otherArtist)
                return -1
            return 0
        } else {
            if (thisArtist < otherArtist)
                return -1
            if (thisArtist > otherArtist)
                return 1
            return 0
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is MusicInfo) {

            return other.albumUrl == this.albumUrl
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + albumUrl.hashCode()
        result = 31 * result + album.hashCode()
        result = 31 * result + (picture?.let { Arrays.hashCode(it) } ?: 0)
        result = 31 * result + title.hashCode()
        result = 31 * result + artist.hashCode()
        result = 31 * result + duration
        result = 31 * result + type
        result = 31 * result + createTime.hashCode()
        return result
    }

    companion object CREATOR : Parcelable.Creator<MusicInfo> {
        override fun createFromParcel(parcel: Parcel): MusicInfo = MusicInfo(parcel)
        override fun newArray(size: Int): Array<MusicInfo?> = arrayOfNulls(size)
    }
}