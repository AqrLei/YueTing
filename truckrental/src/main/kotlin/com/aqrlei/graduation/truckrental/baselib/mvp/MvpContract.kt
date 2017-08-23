package com.aqrlei.graduation.truckrental.baselib.mvp

import android.os.Bundle
import com.aqrlei.graduation.truckrental.baselib.base.BaseActivity
import com.aqrlei.graduation.truckrental.baselib.base.BaseFragment
import com.aqrlei.graduation.truckrental.baselib.base.BasePresenter

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/23
 */
abstract class MvpContract {
    abstract class MvpActivity<T : BasePresenter> : BaseActivity() {
        protected abstract val mPresenter: T
        override fun initComponents(savedInstanceState: Bundle?) {
            super.initComponents(savedInstanceState)
        }

        override fun onDestroy() {
            super.onDestroy()
            if (mPresenter != null) {
                mPresenter.cancle()
            }
        }

    }

    abstract class MvpFragment<T : BasePresenter, V : BaseActivity> : BaseFragment<V>() {
        protected var mPresenter: T? = null
        override fun initComponents() {
            super.initComponents()
            mPresenter = createPresenter()
        }

        protected abstract fun createPresenter(): T
        override fun onDestroy() {
            super.onDestroy()
            if (mPresenter != null) {
                mPresenter!!.cancle()
                mPresenter = null
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            if (mPresenter != null) {
                mPresenter!!.cancle()
                mPresenter = null
            }
        }
    }

    abstract class Presenter<T : BaseActivity>(protected var mMvpActivity :T) : BasePresenter() {


    }
    /*abstract class MvpFragmentPrestenter: BasePresenter(){

    }
    abstract class MvpActivityPresentre: BasePresenter() {

    }*/
}