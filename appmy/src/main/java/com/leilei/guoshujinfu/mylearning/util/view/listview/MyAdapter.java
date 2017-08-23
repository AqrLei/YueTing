package com.leilei.guoshujinfu.mylearning.util.view.listview;

import android.content.Context;
import android.widget.TextView;

import com.leilei.guoshujinfu.mylearning.R;

import java.util.List;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/16
 */

/*
* 具体的Adapter
* @param data 传入的数据
* @param context 上下文
* @param resId 传入的布局Id
* */
public class MyAdapter extends NormalListAdapter {
    /*使用时调用构造方法， 创建适配器*/
    public MyAdapter(List data, Context context, int resId) {
        super(data, context, resId);
    }

    /*
    * 在getView()中执行该方法
    * 在布局上绑定相关数据
    * */
    @Override
    protected void bindData(NormalViewHolder holder, Object o) {
        TextView tv = holder.get(R.id.tv_list_item);
        tv.setText(o.toString());


    }
}
