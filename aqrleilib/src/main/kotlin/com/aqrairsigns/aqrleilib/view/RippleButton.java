package com.aqrairsigns.aqrleilib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.aqrairsigns.aqrleilib.drawable.RippleDrawable;

/**
 * Created by houruixiang on 2017/7/18.
 */

public class RippleButton extends android.support.v7.widget.AppCompatButton {


    private RippleDrawable rippleDrawable;
    private Paint paint;


    public RippleButton(Context context) {
        this(context, null);
    }

    public RippleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        rippleDrawable = new RippleDrawable();
        //设置刷新接口，View中已经实现 --->源码 button继承子drawable.callback
        rippleDrawable.setCallback(this);

        //rippleDrawable
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //设置刷新区域--->源码
        rippleDrawable.setBounds(0, 0, getWidth(), getHeight());
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return who == rippleDrawable || super.verifyDrawable(who);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rippleDrawable.draw(canvas);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        rippleDrawable.onTouch(event);
        return true;
    }


}
