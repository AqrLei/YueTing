package com.aqrlei.graduation.truckrental.baselib.util.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/4
 */

public class ViewPagerAdapter<T extends View> extends PagerAdapter {
    /*保存传入的List<T>*/
    private List<T> view;

    public ViewPagerAdapter(List<T> view) {

       /* this.view =  new ArrayList<>();
        this.view.add(view.get(0));*/
        this.view = view;
    }

    /*
    * 必须实现的方法
    * 返回View的数量
    * */
    @Override
    public int getCount() {

        /*int型所能表达的最大值，用于实现图片的无限轮播*/
        return Integer.MAX_VALUE;
    }

    /*
    * 必须实现的方法
    * 返回view与object是否是同一类型
    * */
    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;
    }

    /*在指定的位置创建页面，并将之添加到要显示的容器中*/
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        /*由于getCount()中返回值是个int的最大值，不是实际值，故要对position做个预先处理*/
        position %= view.size();
        if(position < 0) {
            position = view.size()+position;
        }

        View v = view.get(position);


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
        ViewParent vp = v.getParent();
        if(vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(v);
        }
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        /*同理，销毁view的时候也要对position进行处理*/
        position %= view.size();
        if(position < 0) {
            position = view.size()+position;
        }
        container.removeView(view.get(position));
    }

}
