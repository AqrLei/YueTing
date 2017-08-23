package com.aqrlei.graduation.truckrental.baselib.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aqrlei.graduation.truckrental.baselib.util.AppLog


/**
 * @Author: AqrLei
 *@Name MyLearning
 *@Description:
 *@Date: 2017/8/22
 */
abstract class BaseFragment<T : BaseActivity> : Fragment() {
    protected lateinit var mContainerActivity: T
    protected lateinit var mView: View


    protected abstract val layoutRes: Int
    protected open fun initComponents() {

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is BaseActivity) {
            mContainerActivity = context as T
            AppLog.logDebug(AppLog.LOG_TAG_FRAGMENT, this.javaClass.simpleName + "Attach" +
                    mContainerActivity.javaClass.simpleName)

        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater!!.inflate(layoutRes, container, false)
        initComponents()
        return mView
    }

    fun finish() {
        mContainerActivity.finish()
    }


}