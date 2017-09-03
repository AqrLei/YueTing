package com.leilei.guoshujinfu.mylearning.util.view;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/9/1.
 */

public class SwipeMenu {
    private Context mContext;
    private List<SwipeMenuItem> menuItems;
    private int mViewType;

    public SwipeMenu(Context context) {
        mContext = context;
        menuItems = new ArrayList<>();
    }
    public Context getContext() {
        return  mContext;
    }
    public void addMenuItem(SwipeMenuItem item) {
        menuItems.add(item);
    }
    public void removeMenuItem(SwipeMenuItem item) {
        menuItems.remove(item);
    }
    public List<SwipeMenuItem> getMenuItems() {
        return menuItems;
    }
    public SwipeMenuItem getMenuItem(int index) {
        return menuItems.get(index);
    }
    public int getViewType() {
        return mViewType;
    }
    public void setViewType(int viewType) {
        this.mViewType = viewType;
    }
}
