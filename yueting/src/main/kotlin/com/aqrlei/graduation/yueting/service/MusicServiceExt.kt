package com.aqrlei.graduation.yueting.service

import com.aqrlei.graduation.yueting.model.observable.MusicSingle
import io.reactivex.disposables.Disposable

/**
 * created by AqrLei at 10:54 on 星期六, 五月 05, 2018
 */

fun MusicService.fetchMusicPath(typeName: String):Disposable{
   return MusicSingle.queryMusicInfo(typeName, contentResolver)
                    .subscribe({
                        this.refreshMusic(it)
                    }, {})
}
