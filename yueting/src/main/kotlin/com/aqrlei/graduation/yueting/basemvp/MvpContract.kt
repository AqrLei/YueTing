package com.aqrlei.graduation.yueting.basemvp

import android.os.Bundle
import android.view.View


/**
 * @Author: AqrLei
 * @Date: 2017/8/23
 */
/*
* 将Presenter， Activity, Fragmemt三者绑定
* Java中互相指定泛型上界的做法在Kotlin中会陷入死循环
* */
abstract class MvpContract {
    /*Activity中绑定Presenter,由具体实现类给mPresenter实例化*/
    abstract class MvpActivity<out T : BasePresenter> : BaseActivity() {
        protected abstract val mPresenter: T
        override fun initComponents(savedInstanceState: Bundle?) {
            super.initComponents(savedInstanceState)
        }

        override fun onDestroy() {
            super.onDestroy()
            mPresenter.finish()

        }
    }

    /*
    * Fragment中绑定Presenter 和 包含该Fragment的Activity
    * 在BaseFragMent的onAttach中获取对应的 Activity
    * */
    abstract class MvpFragment<out T : BasePresenter, V : BaseActivity> : BaseFragment<V>() {
        /*由具体实现类指定该Presenter*/
        protected abstract val mPresenter: T

        override fun initComponents(view: View?, savedInstanceState: Bundle?) {
            super.initComponents(view, savedInstanceState)
        }

        override fun onDestroy() {
            super.onDestroy()
            mPresenter.finish()

        }

        override fun onDestroyView() {
            super.onDestroyView()

        }
    }

    /*创建Activity的Presenter，在构造方法中传入对应的Activity实现绑定*/
    abstract class ActivityPresenter<T : BaseActivity>(protected var mMvpActivity: T) : BasePresenter()

    /*
    * 创建Fragment的Presenter,在构造方法中传入对应的Fragment实现绑定
    * <*> 对于<T>, T的上界为TUpper,则读取值时等同于<out TUpper> ,写值时等同于<in Nothing>
    * */
    abstract class FragmentPresenter<T : BaseFragment<*>>(protected var mMvpView: T) : BasePresenter()
}