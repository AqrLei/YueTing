package com.leilei.guoshujinfu.mylearning.util.view;


import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/9/1.
 */

public class SwipeMenuView extends LinearLayout implements View.OnClickListener {

    private SwipeMenuListView mListView;
    private SwipeMenuLayout mLayout;
    private SwipeMenu mMenu;
    private OnSwipeItemClickListener mListener;
    private  int position;

    public SwipeMenuListView getListView() {
        return mListView;
    }

    public void setListView(SwipeMenuListView mListView) {
        this.mListView = mListView;
    }

    public SwipeMenuLayout getLayout() {
        return mLayout;
    }

    public void setLayout(SwipeMenuLayout mLayout) {
        this.mLayout = mLayout;
    }

    public SwipeMenu getMenu() {
        return mMenu;
    }

    public void setMenu(SwipeMenu mMenu) {
        this.mMenu = mMenu;
    }

    public OnSwipeItemClickListener getOnSwipeItemClickListener() {
        return mListener;
    }

    public void setOnSwipeItemClickListener(OnSwipeItemClickListener mListener) {
        this.mListener = mListener;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public SwipeMenuView(SwipeMenu menu, SwipeMenuListView listView) {
        super(menu.getContext());
        mListView = listView;
        mMenu =  menu;
        List<SwipeMenuItem> items = menu.getMenuItems();
        int id = 0;
        for(SwipeMenuItem item: items) {

            addItem(item, id++);
        }

    }
    private void addItem(SwipeMenuItem item, int id) {
        LayoutParams params = new LayoutParams(item.getWidth(),
                LayoutParams.MATCH_PARENT);
        LinearLayout parent = new LinearLayout(getContext());
        parent.setId(id);
        parent.setGravity(Gravity.CENTER);
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setLayoutParams(params);
        parent.setBackground(item.getBackground());
        parent.setOnClickListener(this);
        addView(parent);
        if(item.getIcon() != null) {
            parent.addView(createIcon(item));
        }
        if(!TextUtils.isEmpty(item.getTitle())) {
            parent.addView(createTitle(item));
        }
    }
    private ImageView createIcon(SwipeMenuItem item) {
        ImageView iv = new ImageView(getContext());
        iv.setImageDrawable(item.getIcon());
        return iv;
    }
    private TextView createTitle(SwipeMenuItem item) {
        TextView tv = new TextView(getContext());
        tv.setText(item.getTitle());
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(item.getTitleSize());
        tv.setTextColor(item.getTitleColor());
        return tv;
    }

    @Override
    public void onClick(View v) {
        if(mListener != null && mLayout.isOpaque()) {
            mListener.onTtemClick(this, mMenu, v.getId());
        }

    }
    public static interface OnSwipeItemClickListener{
        void onTtemClick(SwipeMenuView view, SwipeMenu menu, int index);
    }
}
