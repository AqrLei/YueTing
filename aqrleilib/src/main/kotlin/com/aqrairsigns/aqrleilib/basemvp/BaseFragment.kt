package com.aqrairsigns.aqrleilib.basemvp

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aqrairsigns.aqrleilib.util.AppLog


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
    protected open fun initComponents(view: View?, savedInstanceState: Bundle?) {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            @Suppress("UNCHECKED_CAST")
            mContainerActivity = context as T
            AppLog.logDebug(AppLog.LOG_TAG_FRAGMENT, this.javaClass.simpleName + "----onAttach----" +
                    mContainerActivity.javaClass.simpleName)

        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater!!.inflate(layoutRes, container, false)
        //initComponents()
        AppLog.logDebug(AppLog.LOG_TAG_FRAGMENT, this.javaClass.simpleName + "----onCreateView----" +
                mContainerActivity.javaClass.simpleName)
        return mView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AppLog.logDebug(AppLog.LOG_TAG_FRAGMENT, this.javaClass.simpleName + "----onActivityCreated----" +
                mContainerActivity.javaClass.simpleName)
    }

    override fun onStart() {
        super.onStart()
        AppLog.logDebug(AppLog.LOG_TAG_FRAGMENT, this.javaClass.simpleName + "----onStart----" +
                mContainerActivity.javaClass.simpleName)
    }

    override fun onResume() {
        super.onResume()
        AppLog.logDebug(AppLog.LOG_TAG_FRAGMENT, this.javaClass.simpleName + "----onResume----" +
                mContainerActivity.javaClass.simpleName)
    }

    override fun onPause() {
        super.onPause()
        AppLog.logDebug(AppLog.LOG_TAG_FRAGMENT, this.javaClass.simpleName + "----onPause----" +
                mContainerActivity.javaClass.simpleName)
    }

    override fun onStop() {
        super.onStop()
        AppLog.logDebug(AppLog.LOG_TAG_FRAGMENT, this.javaClass.simpleName + "----onStop----" +
                mContainerActivity.javaClass.simpleName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        AppLog.logDebug(AppLog.LOG_TAG_FRAGMENT, this.javaClass.simpleName + "----onDestroyView----" +
                mContainerActivity.javaClass.simpleName)
    }

    override fun onDestroy() {
        super.onDestroy()
        AppLog.logDebug(AppLog.LOG_TAG_FRAGMENT, this.javaClass.simpleName + "----onDestroy----" +
                mContainerActivity.javaClass.simpleName)
    }

    override fun onDetach() {
        super.onDetach()
        AppLog.logDebug(AppLog.LOG_TAG_FRAGMENT, this.javaClass.simpleName + "----onDetach----" +
                mContainerActivity.javaClass.simpleName)
    }

    fun finish() {
        mContainerActivity.finish()
    }


}