package com.leilei.guoshujinfu.mylearning.util.view.listview;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/15
 */

/*
* @param mViews: <key, value>键值对,相比hashMap内存优化
* @param mLayoutInflater 用于获取布局View
* @param convertView 保存缓存的View
* @param position 记录当前itemView的位置
* */
public class NormalViewHolder {
    private SparseArray<View> mViews;
    private LayoutInflater mLayoutInflater;
    private View convertView;
    private int position;

    /*私有构造方法*/
    private NormalViewHolder(Context context, int resId, int position, ViewGroup parent) {
        mViews = new SparseArray<>();
        mLayoutInflater = LayoutInflater.from(context);
        convertView = mLayoutInflater.inflate(resId, parent, false);
        /*将ViewHolder保存在convertView的Tag中*/
        convertView.setTag(this);
        this.position = position;
    }

    /*返回一个ViewHolder实例*/
    public static NormalViewHolder getNormalViewHolder(Context context, int resId, int position,
                                                       View convertView, ViewGroup parent) {
        NormalViewHolder holder = null;
        if (convertView == null) {
            holder = new NormalViewHolder(context, resId, position, parent);

        } else {
            holder = (NormalViewHolder) convertView.getTag();
            holder.position = position;
        }
        return holder;
    }

    public View getConvertView() {
        return convertView;
    }

    public int getPosition() {
        return position;
    }

    /*根据Id返回一个itemView中的View*/
    public <T extends View> T get(int viewId) {
        View v = mViews.get(viewId);
        if (v == null) {
            v = convertView.findViewById(viewId);
            mViews.put(viewId, v);
        }
        return (T) v;
    }
}
