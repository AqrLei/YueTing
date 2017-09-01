package com.leilei.guoshujinfu.mylearning.util.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/9/1.
 */

public class SwipeMenuListView extends ListView {
    private static final int TOUCH_STATE_NONE = 0;
    private static final int TOUCH_STATE_X = 1;
    private static final int TOUCH_STATE_Y = 2;
    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_Right = -1;
    private int mDirection = 1;
    private int maxY = 5;
    private int maxX = 3;
    private float mDownX;
    private float mDownY;
    private int mTouchPosition;
    private int mTouchState;
    //private SwipeMenuLayout mTouchView;
    //private OnSwipeListener mOnSwipeListener;
    //private SwipeMenuCreator mMenuCreator;
    //private OnMenuItemClickListener mOnMenuItemClickListener;
    //private OnMenuStateChangeListener mOnMenuStateChangeListener;
    private Interpolator mCloseInterpolator;
    private Interpolator mOpenInterpolator;

    public SwipeMenuListView(Context context) {
        this(context, null);
    }

    public SwipeMenuListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuListView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SwipeMenuListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        maxX = dpToPx(maxX);
        maxY = dpToPx(maxY);
        mTouchState = TOUCH_STATE_NONE;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);//new SwipeMenuAdapter(){...}

    }

    public void setCloseInterpolator(Interpolator interpolator) {
        mCloseInterpolator = interpolator;
    }

    public void setOpenInterpolator(Interpolator interpolator) {
        mOpenInterpolator = interpolator;
    }

    public Interpolator getOpenInterPolator() {
        return mOpenInterpolator;
    }

    public Interpolator getCloseInterpolator() {
        return mCloseInterpolator;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                boolean handled = super.onInterceptTouchEvent(ev);
                mTouchState = TOUCH_STATE_NONE;
                mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
                View view = getChildAt(mTouchPosition - getFirstVisiblePosition());
                // if(view instanceof SwipeMenuLayout )\
                return handled;
            case MotionEvent.ACTION_MOVE:
                float dy = Math.abs((ev.getY() - mDownY));
                float dx = Math.abs((ev.getX() - mDownX));
                if (dy > maxY || dx > maxX) {
                    if (mTouchState == TOUCH_STATE_NONE) {
                        if (dy > maxY) {
                            mTouchState = TOUCH_STATE_Y;
                        } else if (dx > maxX) {
                            mTouchState = TOUCH_STATE_X;
                            /*if(mOnSwipeListener != null) {


                        }*/
                        }
                    }
                    return true;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() != MotionEvent.ACTION_DOWN /*&& mTouchView == null*/)
            return super.onTouchEvent(ev);
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return  super.onTouchEvent(ev);

    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }
}
