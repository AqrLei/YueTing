package com.leilei.guoshujinfu.mylearning.util.view.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/15
 */


/*
* 通用型适配器基类
* @param mData: View需要用到的数据
* @param mCOntext: 背景上下文关系。。。
* @param mResId: 布局资源的Id
* */
public abstract class NormalListAdapter<T> extends BaseAdapter {
    protected List<T> mData;
    protected Context mContext;
    protected int mResId;

    public NormalListAdapter(List<T> data, Context context, int resId) {
        mData = data;
        mContext = context;
        mResId = resId;
    }

    /*返回数据的长度*/
    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    /*放回具体的数据*/
    @Override
    public Object getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    /*返回数据所处的位置*/
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
    * 返回View,进行绘制
    * @param holder
    * 自定义的ViewHolder
    * 对缓存的convertView进行重用，提高运行效率
    * 一些对View进行处理操作的方法
    * */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /**/
        NormalViewHolder holder = NormalViewHolder.getNormalViewHolder(mContext, mResId, position,
                convertView, parent);
        bindData(holder, mData.get(position));
        return holder.getConvertView();
    }

    /*抽象方法，子类继承实现用于将数据绑定到View上*/
    protected abstract void bindData(NormalViewHolder holder, T t);
}
