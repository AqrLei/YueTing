package com.leilei.guoshujinfu.mylearning.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/8/3.
 */

public class ChatMessage {
    public static final int TYPE_MESSAGE_FlAG = 5;
    private Drawable avatar;
    private String Content;
    private int type;
    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public int getType() {
        return type;
    }

    public Drawable getAvatar() {
        return avatar;
    }

    public void setAvatar(Drawable avatar) {
        this.avatar = avatar;
    }
}
