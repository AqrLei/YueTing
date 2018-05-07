package com.aqrlei.graduation.yueting.presenter


import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrlei.graduation.yueting.model.observable.BookSingle
import com.aqrlei.graduation.yueting.ui.fragment.PdfRendererFragment

/**
 * @Author: AqrLei
 * @Date: 2017/8/23
 */
/*
* @#param mMvpView 访问对应的Fragment
* */
class PdfRendererPresenter(mMvpView: PdfRendererFragment) :
        MvpContract.FragmentPresenter<PdfRendererFragment>(mMvpView) {
    fun getIndexFromDB(path: String) {
        val disposable =
                BookSingle.selectIndex(path)
                        .subscribe({
                            mMvpView.loadBook(it[0])
                        }, {})
        addDisposables(disposable)
    }

    fun addIndexToDB(path: String, begin: Int, end: Int) {
        val disposables =
                BookSingle.updateIndex(path, begin, end)
                        .subscribe()
        addDisposables(disposables)
    }

    fun addMarkToDB(path: String, currentBegin: Int) {
        val disposables =
                BookSingle.insertMark(path, currentBegin)
                        .subscribe({
                            mMvpView.activity?.run {
                                AppToast.toastShow(this, if (it) "书签添加完毕" else "书签添加失败", 1000)

                            }
                        }, {})
        addDisposables(disposables)
    }
}