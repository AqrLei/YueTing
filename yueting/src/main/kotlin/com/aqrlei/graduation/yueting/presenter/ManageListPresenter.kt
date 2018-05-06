package com.aqrlei.graduation.yueting.presenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.SelectInfo
import com.aqrlei.graduation.yueting.model.observable.BookSingle
import com.aqrlei.graduation.yueting.model.observable.MusicSingle
import com.aqrlei.graduation.yueting.model.observable.TypeSingle
import com.aqrlei.graduation.yueting.ui.ManageListActivity
import io.reactivex.Single

/**
 * @author  aqrLei on 2018/5/2
 */
class ManageListPresenter(mMvpActivity: ManageListActivity) :
        MvpContract.ActivityPresenter<ManageListActivity>(mMvpActivity) {
    fun deleteItem(type: String, typeItem: String, deleteData: List<SelectInfo>) {
        if (type == YueTingConstant.MANAGE_TYPE_LIST) {
            if (typeItem == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK) {
                deleteBookList(deleteData)
            } else {
                deleteMusicList(deleteData)
            }
        } else {
            if (typeItem == YueTingConstant.FRAGMENT_TITLE_TYPE_BOOK) {
                deleteBook(deleteData)
            } else {
                deleteMusic(deleteData)
            }
        }
    }

    private fun deleteMusicList(typeNameList: List<SelectInfo>) {
        val disposable =
                TypeSingle.deleteType(typeNameList)
                        .flatMap { t: Boolean ->
                            if (t) {
                                MusicSingle.deleteMusicInfoByList(typeNameList)
                            } else {
                                throw Exception()
                            }
                        }
                        .subscribe({
                            if (it) {
                                mMvpActivity.deleteFinished("歌单删除成功", it)
                            } else {
                                mMvpActivity.deleteFinished("歌单删除失败", it)
                            }
                        }, {

                        })
        addDisposables(disposable)
    }

    private fun deleteBookList(typeNameList: List<SelectInfo>) {
        val disposable =
                TypeSingle.deleteType(typeNameList)
                        .flatMap {
                            if (it) {
                                BookSingle.deleteBookInfoByList(typeNameList)
                            } else {
                                Single.just(it)
                            }
                        }
                        .subscribe({
                            if (it) {
                                mMvpActivity.deleteFinished("书单删除成功", it)
                            } else {
                                mMvpActivity.deleteFinished("书单删除失败", it)
                            }
                        }, {})
        addDisposables(disposable)
    }

    private fun deleteMusic(musicList: List<SelectInfo>) {
        val disposable =
                MusicSingle.deleteMusicInfo(musicList).subscribe({
                    if (it) {
                        mMvpActivity.deleteFinished("歌曲删除成功", it)
                    } else {
                        mMvpActivity.deleteFinished("歌曲删除失败", it)
                    }
                }, {})
        addDisposables(disposable)
    }

    private fun deleteBook(bookList: List<SelectInfo>) {
        val disposable =
                BookSingle.deleteBookInfo(bookList).subscribe({
                    if (it) {
                        mMvpActivity.deleteFinished("书籍删除成功", it)
                    } else {
                        mMvpActivity.deleteFinished("书籍删除失败", it)
                    }
                }, {})
        addDisposables(disposable)
    }
}