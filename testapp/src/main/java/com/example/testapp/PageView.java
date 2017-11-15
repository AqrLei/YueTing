package com.example.testapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/27 Time: 14:52
 */

public class PageView extends AppCompatTextView {
    private Bitmap mBitmap;
    private OnScrollListener mScrollListener;
    private int clickX;
    private int currentX;
    private int touchSlop;
    private boolean moved = false;

    public PageView(Context context) {
        this(context, null);
    }

    public PageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
       /* int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN) {
            clickX = (int) event.getX();
        }
        else if(action == MotionEvent.ACTION_MOVE) {
            if (Math.abs(event.getX() - clickX) > touchSlop) {
                moved = true;
            }
        }
        else if(action == MotionEvent.ACTION_UP) {
            currentX = (int) event.getX();
            if (moved && mScrollListener != null) {
                if (clickX > currentX) {
                    mScrollListener.onLeftScroll();
                } else {
                    mScrollListener.onRightScroll();
                }
            }
            moved = false;
        }*/
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getX() - clickX) > touchSlop) {
                    moved = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                currentX = (int) event.getX();
                if (moved && mScrollListener != null) {
                    if (clickX > currentX) {
                        mScrollListener.onLeftScroll();
                    } else {
                        mScrollListener.onRightScroll();
                    }
                }
                moved = false;
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        mScrollListener = listener;
    }

    public interface OnScrollListener {
        void onLeftScroll();

        void onRightScroll();
    }

}
