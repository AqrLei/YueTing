package com.aqrairsigns.aqrleilib.adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/4
 */

class CommonPagerAdapter<T : View>(private val view: List<T>)
    : PagerAdapter() {
    override fun getCount(): Int {
        /*int型所能表达的最大值，用于实现图片的无限轮播*/
        return Integer.MAX_VALUE
    }

    /*
    * 必须实现的方法
    * 返回view与object是否是同一类型
    * */
    override fun isViewFromObject(view: View, any: Any) = view === any

    /*在指定的位置创建页面，并将之添加到要显示的容器中*/
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var mPosition = position

        /*由于getCount()中返回值是个int的最大值，不是实际值，故要对position做个预先处理*/
        mPosition %= view.size
        if (mPosition < 0) {
            mPosition = view.size + mPosition
        }
        val v = view[mPosition]
        /*
        * 如果v已经被添加过既这个view已经有了parent，要先将之从父容器中remove
        * 否则在addView()的时候会
        * if (child.getParent() != null) {
        *   throw new IllegalStateException("The specified child already has a parent. " +
        *           "You must call removeView() on the child's parent first.");
        * }
        * 在destroyItem中已经调用了removeView的方法
        * 且在之前的初始化操作完成之后
        * 的每一次调用instantiateItem都在destroyItem之后，
        * 此处就不必重复调用removeView方法
        * */
        val vp = v.parent
        if (vp != null) {
            val parent = vp as ViewGroup
            parent.removeView(v)
        }
        container.addView(v)
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        var position = position
        /*同理，销毁view的时候也要对position进行处理*/
        position %= view.size
        if (position < 0) {
            position += view.size
        }
        container.removeView(view[position])
    }

}
