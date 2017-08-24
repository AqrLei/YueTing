package com.leilei.guoshujinfu.mylearning.util.view.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

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
public class CommonPageViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View convertView;
    public CommonPageViewHolder(View itemView) {
        super(itemView);
        convertView = itemView;
        mViews = new SparseArray<>();

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
