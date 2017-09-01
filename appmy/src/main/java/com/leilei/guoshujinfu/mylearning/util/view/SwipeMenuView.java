package com.leilei.guoshujinfu.mylearning.util.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/9/1.
 */

public class SwipeMenuView extends LinearLayout implements View.OnClickListener {
    public SwipeMenuView(Context context) {
        this(context, null);
    }

    public SwipeMenuView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwipeMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SwipeMenuView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onClick(View v) {

    }
}
