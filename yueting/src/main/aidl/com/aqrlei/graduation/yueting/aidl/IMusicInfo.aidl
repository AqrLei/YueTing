// IMusicInfo.aidl
package com.aqrlei.graduation.yueting.aidl;
import com.aqrlei.graduation.yueting.aidl.MusicInfo;
// Declare any non-default types here with import statements

interface IMusicInfo {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
  //in 接收传入数据，无数据返回
  //out 返回数据，数据不接收传入数据
  //inout 既接受返回数据也接受传入数据
  //接受类型，java基础类型、String、 CharSequence、List、map 、 AIDL、Parcelable
    void setMusicInfo(in List<MusicInfo> infoS);
}
