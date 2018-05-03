package com.aqrlei.graduation.yueting.presenter


import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrairsigns.aqrleilib.util.DBManager
import com.aqrlei.graduation.yueting.constant.DataConstant
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
    fun getIndexFromDB(path: String): Int {
        val c = DBManager.sqlData(DBManager.SqlFormat.selectSqlFormat(DataConstant.BOOK_TABLE_NAME,
                "${DataConstant.BOOK_TABLE_C2_INDEX_BEGIN}, ${DataConstant.BOOK_TABLE_C3_INDEX_END}",
                DataConstant.COMMON_COLUMN_PATH, "="),
                null, arrayOf(path), DBManager.SqlType.SELECT)
                .getCursor()
        var begin = 0
        while (c?.moveToNext() == true) {
            begin = c.getInt(c.getColumnIndex(DataConstant.BOOK_TABLE_C2_INDEX_BEGIN))
        }
        return begin
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
                            AppToast.toastShow(mMvpView.activity, if (it) "书签添加完毕" else "书签添加失败", 1000)
                        }, {})
        addDisposables(disposables)
    }
}