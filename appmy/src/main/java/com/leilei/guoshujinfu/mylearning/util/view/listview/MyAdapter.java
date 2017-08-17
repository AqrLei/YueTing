package com.leilei.guoshujinfu.mylearning.util.view.listview;

import android.content.Context;

import java.util.List;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/16
 */

/*
* 具体的Adapter
* @param 传入的数据
* @param 上下文
* @param 传入的布局Id
* */
public class MyAdapter extends NormalListAdapter {

    public MyAdapter(List data, Context context, int resId) {
        super(data, context, resId);
    }
/*
* 在getView()中执行该方法
* 在布局上绑定相关数据
* */
    @Override
    protected void bindData(NormalViewHolder holder, Object o) {


    }
}
