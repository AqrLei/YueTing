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
    abstract class MvpActivity<out T : BasePresenter> : BaseActivity() {
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

    abstract class MvpFragment<out T : BasePresenter, V : BaseActivity> : BaseFragment<V>() {
        protected abstract val mPresenter: T
        override fun initComponents() {
            super.initComponents()

        }
        override fun onDestroy() {
            super.onDestroy()
            if (mPresenter != null) {
                mPresenter.cancle()

            }
        }
        override fun onDestroyView() {
            super.onDestroyView()
            if (mPresenter != null) {
                mPresenter.cancle()
            }
        }
    }

    abstract class ActivityPresenter<T : BaseActivity>(protected var mMvpActivity :T) : BasePresenter()
    abstract  class FragmentPresenter<T : BaseFragment<*>>(protected var mMvpView: T): BasePresenter()
}