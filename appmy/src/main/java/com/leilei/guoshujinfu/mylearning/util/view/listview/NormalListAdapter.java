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

public abstract  class NormalListAdapter<T> extends BaseAdapter {
    protected List<T> mData;
    protected Context mContext;
    protected int mResId;

    public NormalListAdapter(List<T> data, Context context, int resId) {
        mData = data;
        mContext = context;
        mResId = resId;
    }

    @Override
    public int getCount() {
        return mData == null ? 0: mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NormalViewHolder holder = NormalViewHolder.getNormalViewHolder(mContext, mResId, position,
                convertView, parent);
        bindData(holder, mData.get(position));
        return holder.getConvertView();
    }
    protected abstract void bindData(NormalViewHolder holder,T t);
}
