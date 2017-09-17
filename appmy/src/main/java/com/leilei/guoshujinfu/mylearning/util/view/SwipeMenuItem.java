package com.leilei.guoshujinfu.mylearning.util.view;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/9/1.
 */

public class SwipeMenuItem {

    private int id;
    private Context mContext;
    private String title;
    private Drawable icon;
    private Drawable background;
    private int titleSize;
    private int titleColor;
    private int width;

    public SwipeMenuItem(Context context) {
        mContext = context;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setIcon(int resId) {
        this.icon = mContext.getResources().getDrawable(resId, null);
    }

    public Drawable getBackground() {
        return background;
    }

    public void setBackground(Drawable background) {
        this.background = background;
    }

    public void setBackground(int resId) {
        this.background = mContext.getResources().getDrawable(resId, null);
    }

    public int getTitleSize() {
        return titleSize;
    }

    public void setTitleSize(int titleSize) {
        this.titleSize = titleSize;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
