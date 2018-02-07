package com.aqrlei.graduation.yueting.presenter.activitypresenter

import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.view.PageView
import com.aqrlei.graduation.yueting.factory.ChapterFactory
import com.aqrlei.graduation.yueting.factory.PageFactory
import com.aqrlei.graduation.yueting.model.local.BookInfo
import com.aqrlei.graduation.yueting.ui.ReadActivity
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/11/17.
 */
class ReadActivityPresenter(mMvpActivity: ReadActivity) :
        MvpContract.ActivityPresenter<ReadActivity>(mMvpActivity) {
    companion object {
        fun catalogsObservable(): Observable<Boolean> {
            return Observable.defer {
                Observable.just(ChapterFactory.CHAPTER.getChapter())
            }
        }
    }

    fun getPageFactory(bookInfo: BookInfo, pageView: PageView): PageFactory = PageFactory(pageView, bookInfo)
    /*  val bookMessage = BookInfo()
      val sd = Environment.getExternalStorageDirectory()
      val path = bookInfo?.path ?: sd.path+"/太古神王.txt"
      val file = File(path)
      bookMessage.name = file.name
      bookMessage.path = file.path
      bookMessage.encoding = "GBK"*/
    fun getCatalog() {
        val disposables = CompositeDisposable()
        addDisposables(disposables)
        disposables.add(
                catalogsObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableObserver<Boolean>() {
                            override fun onComplete() {
                            }

                            override fun onError(e: Throwable) {
                            }

                            override fun onNext(t: Boolean) {
                                mMvpActivity.jumpToCatalog(t)
                            }
                        }
                        )

        )

    }


}