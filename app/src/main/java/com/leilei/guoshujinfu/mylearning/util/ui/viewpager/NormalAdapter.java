package com.leilei.guoshujinfu.mylearning.util.ui.viewpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.leilei.guoshujinfu.mylearning.util.AppLog;

import java.util.List;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/4
 */

public class NormalAdapter<T extends View> extends PagerAdapter {
    /*保存传入的List<T>*/
    private List<T> view;

    public NormalAdapter(List<T> view) {
        AppLog.logDebug(AppLog.LOG_TAG_TEST,"NormalAdapter");
        this.view = view;
    }

    /*
    * 必须实现的方法
    * 返回View的数量
    * */
    @Override
    public int getCount() {
        AppLog.logDebug(AppLog.LOG_TAG_TEST,"getCount");
        /*int型所能表达的最大值，用于实现图片的无限轮播*/
        return Integer.MAX_VALUE;
    }

    /*
    * 必须实现的方法
    * 返回view与object是否是同一类型
    * */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        AppLog.logDebug(AppLog.LOG_TAG_TEST,"isViewFromObject");
        return view == object;
    }

    /*在指定的位置创建页面，并将之添加到要显示的容器中*/
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        AppLog.logDebug(AppLog.LOG_TAG_TEST,"instantiateItem");
        /*由于getCount()中返回值是个int的最大值，不是实际值，故要对position做个预先处理*/
        position %= view.size();
        if(position < 0) {
            position = view.size()+position;
        }

        View v = view.get(position);
        ViewParent vp = v.getParent();
        /*如果v已经被添加过既第二次到达这个view，要先将之从父容器中remove*/
        if(vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(v);
        }
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        AppLog.logDebug(AppLog.LOG_TAG_TEST,"destroyItem");
        /*同理，销毁view的时候也要对position进行处理*/
        position %= view.size();
        if(position < 0) {
            position = view.size()+position;
        }
        container.removeView(view.get(position));
    }

}
