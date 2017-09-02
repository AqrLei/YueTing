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
    private SwipeMenuLayout mTouchView;
    private OnSwipeListener mOnSwipeListener;
    private SwipeMenuCreator mMenuCreator;
    private OnMenuItemClickListener mOnMenuItemClickListener;
    private OnMenuStateChangeListener mOnMenuStateChangeListener;
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
                if(view instanceof SwipeMenuLayout ) {
                    if(mTouchView != null && mTouchView.isOpen() &&
                            !isRangeOfView(mTouchView.getMenuView(), ev)) {
                        return true;
                    }
                    mTouchView = (SwipeMenuLayout) view;
                    mTouchView.setSwipeDirection(mDirection);
                }
                if(mTouchView != null && mTouchView.isOpen() && view != mTouchView)  {
                    handled = true;
                }
                if( mTouchView != null) {
                    mTouchView.onSwipe(ev);
                }
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
                            if(mOnSwipeListener != null) {
                                mOnSwipeListener.onSwipeStart(mTouchPosition);
                        }
                        }
                    }
                    return true;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() != MotionEvent.ACTION_DOWN && mTouchView == null)
            return super.onTouchEvent(ev);
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int oldPos = mTouchPosition;
                mDownX = ev.getX();
                mDownY = ev.getY();
                mTouchState = TOUCH_STATE_NONE;
                mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
                if(mTouchPosition == oldPos && mTouchView != null
                        && mTouchView.isOpen()) {
                    mTouchState = TOUCH_STATE_X;
                    mTouchView.onSwipe(ev);
                    return true;

                }
                View view = getChildAt(mTouchPosition - getFirstVisiblePosition());
                if(mTouchView != null && mTouchView.isOpen()) {
                    mTouchView.smoothCloseMenu();
                    mTouchView = null;
                    MotionEvent cancelEvent = MotionEvent.obtain(ev);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                    onTouchEvent(cancelEvent);
                    if(mOnMenuStateChangeListener != null) {
                        mOnMenuStateChangeListener.onMenuClose(oldPos);
                    }
                    return  true;
                }
                if(view instanceof  SwipeMenuLayout) {
                    mTouchView = (SwipeMenuLayout) view;
                    mTouchView.setSwipeDirection(mDirection);
                }
                if(mTouchView != null) {
                    mTouchView.onSwipe(ev);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mTouchState == TOUCH_STATE_X) {
                    if(mTouchView != null) {
                        boolean isBeforeOpen = mTouchView.isOpen();
                        mTouchView.onSwipe(ev);
                        boolean isAfterOpen = mTouchView.isOpen();
                        if(isBeforeOpen != isAfterOpen && mOnMenuStateChangeListener != null) {
                            if(isAfterOpen) {
                                mOnMenuStateChangeListener.onMenuOpen(mTouchPosition);
                            } else {
                                mOnMenuStateChangeListener.onMenuClose(mTouchPosition);
                            }
                        }
                        if(!isAfterOpen) {
                            mTouchPosition = -1;
                            mTouchView = null;
                        }
                    }
                    if( mOnSwipeListener != null) {
                        mOnSwipeListener.onSwipeEnd(mTouchPosition);
                    }
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY()) - getHeaderViewsCount();
                if(!mTouchView.getSwipeEnable() || mTouchPosition != mTouchView.getPosition()){
                    break;
                }
                float dy = Math.abs((ev.getY() - mDownY));
                float dx = Math.abs((ev.getX() - mDownX));
                if(mTouchState == TOUCH_STATE_X) {
                if(mTouchView != null) {
                    mTouchView.onSwipe(ev);
                }
                getSelector().setState(new int[]{0});
                ev.setAction(MotionEvent.ACTION_CANCEL);
                super.onTouchEvent(ev);
                return true;
            } else if( mTouchState == TOUCH_STATE_NONE) {
                if(dy> maxY) {
                    mTouchState = TOUCH_STATE_Y;
                } else if(dx > maxX) {
                    mTouchState = TOUCH_STATE_X;
                    if(mOnSwipeListener != null) {
                        mOnSwipeListener.onSwipeStart(mTouchPosition);
                    }
                }
            }

                break;
        }
        return  super.onTouchEvent(ev);

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
    public  void setSwipeDirection(int direction) {
        mDirection = direction;
    }
    public void setOnMenuStateChangeListener( OnMenuStateChangeListener listener) {
        mOnMenuStateChangeListener = listener;
    }
    public void setOnSwipeListener(OnSwipeListener listener) {
        mOnSwipeListener = listener;
    }
    public void setmOnMenuItemClickListener(OnMenuItemClickListener listener) {
        mOnMenuItemClickListener = listener;
    }
    public void setMenuCreator(SwipeMenuCreator menuCreator) {
        mMenuCreator = menuCreator;
    }
    public void smoothCloseMenu() {
        if(mTouchView != null && mTouchView.isOpen()) {
            mTouchView.smoothCloseMenu();
        }
    }
    public void smoothOpenMenu(int position) {
        if(position >= getFirstVisiblePosition()&&
                position <= getLastVisiblePosition()) {
                View view = getChildAt(position - getFirstVisiblePosition());
            if(view instanceof  SwipeMenuLayout) {
                mTouchPosition = position;
                if(mTouchView != null && mTouchView.isOpen()) {
                    mTouchView.smoothCloseMenu();
                }
                mTouchView = (SwipeMenuLayout) view;
                mTouchView.setSwipeDirection(mDirection);
                mTouchView.smoothOpenMenu();
            }
        }
    }


    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }
    public static boolean isRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if(ev.getX() < x || ev.getRawX() > (x+ view.getWidth())||
                ev.getY() <y || ev.getRawY()> (y+ view.getHeight())){
            return false;
        }
        return true;
    }
    public static interface OnMenuItemClickListener{
        boolean onMenuItemClick(int position, SwipeMenu menu, int index);
    }
    public static interface  OnSwipeListener{
        void onSwipeStart(int position);
        void onSwipeEnd(int position);
    }
    public static interface OnMenuStateChangeListener {
        void onMenuOpen(int position);
        void onMenuClose(int position);
    }
}
