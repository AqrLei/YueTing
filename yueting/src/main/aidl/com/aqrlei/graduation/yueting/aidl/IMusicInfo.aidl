// IMusicInfo.aidl
package com.aqrlei.graduation.yueting.aidl;
import com.aqrlei.graduation.yueting.model.local.MusicInfo;

// Declare any non-default types here with import statements

interface IMusicInfo {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     void setInfo(ArrayList<MusicInfo> info);
}
