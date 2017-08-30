package com.leilei.guoshujinfu.mylearning.util.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.leilei.guoshujinfu.mylearning.R;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/30
 */

public class SemiRoundBar extends View {

    private Paint mPaint;
    private Context mContext;
    private int mBackgroundColor;
    private int mProgressColor;
    private float mRadius;
    private float mMaxProgress;
    private float mCurrentProgress;
    private boolean mIsOpenAnimation;
    private int mStartDegree;
    private int mSweepDegree;


    private int mProgressDegree;
    private int mDrawDegree;

    public SemiRoundBar(Context context) {
        this(context, null);
    }

    public SemiRoundBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SemiRoundBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SemiRoundBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attributeSet) {
        mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TintTypedArray typedArray = TintTypedArray.obtainStyledAttributes(
                mContext,
                attributeSet,
                R.styleable.SemiRoundBar);
     /*   TypedArray typedArray = mContext.obtainStyledAttributes(
                attributeSet,
                R.styleable.SemiRoundBar);*/
        mBackgroundColor = typedArray.
                getColor(R.styleable.SemiRoundBar_backgroundColor, Color.parseColor("#bbbbbb"));
        mProgressColor = typedArray.
                getColor(R.styleable.SemiRoundBar_progressColor, Color.parseColor("#41a9f8"));
        mRadius = typedArray.getDimension(R.styleable.SemiRoundBar_radius, 150);
        mMaxProgress = typedArray.getFloat(R.styleable.SemiRoundBar_max, 10);
        mCurrentProgress = typedArray.getFloat(R.styleable.SemiRoundBar_progress, 6);
        mIsOpenAnimation = typedArray.getBoolean(R.styleable.SemiRoundBar_openAnimation, true);
        float mRoundWidth = typedArray.getDimension(R.styleable.SemiRoundBar_roundWidth, 10);
        int capStyle = typedArray.getInteger(R.styleable.SemiRoundBar_capStyle, 1);
        mStartDegree = typedArray.getInteger(R.styleable.SemiRoundBar_startDegree, 150);
        mSweepDegree = typedArray.getInteger(R.styleable.SemiRoundBar_sweepDegree, 240);
        mDrawDegree = 0;
        mPaint.setStrokeWidth(mRoundWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        switch (capStyle) {
            case 0:
                mPaint.setStrokeCap(Paint.Cap.BUTT);
                break;
            case 1:
                mPaint.setStrokeCap(Paint.Cap.ROUND);
                break;
            case 2:
                mPaint.setStrokeCap(Paint.Cap.SQUARE);
                break;
        }
        setProgress(mCurrentProgress);
    }



    public void setOpenAnimation(boolean openAnimation) {
        mIsOpenAnimation = openAnimation;
    }

    public synchronized void setMaxProgress(float maxProgress) {
        mMaxProgress = maxProgress;
        setProgress(mCurrentProgress);
    }
    public synchronized void setProgress(float progress) {
        mProgressDegree = (int) (mSweepDegree * (progress / mMaxProgress));
        mProgressDegree = mProgressDegree > mSweepDegree? mSweepDegree:mProgressDegree;
        mDrawDegree = 0;
        postInvalidate();
    }
    public synchronized float getProgress() {
        return mCurrentProgress;
    }
    public synchronized float getMaxProgress() {
        return mMaxProgress;
    }
    public void setProgressColor(int color) {

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int[] colors = new int[4];
        float[] position = new float[4];

        position[0] = 0f;
        position[1] = 0.3f;
        position[2] = 0.75f;
        position[3] = 1.0f;
        colors[0] = 0XFF98050A;
        colors[1] = 0XFF985B00;
        colors[2] = 0XFF929807;
        colors[3] = 0XFF19982E;

        float centerX = width / 2;
        float centerY = height / 2;
        float left = centerX - mRadius;
        float top = centerY - mRadius;
        float right = centerX + mRadius;
        float bottom = centerY + mRadius;

        mPaint.setColor(mBackgroundColor);
        canvas.drawArc(left, top, right, bottom, mStartDegree, mSweepDegree, false, mPaint);

        mPaint.setColor(mProgressColor);
        if (mIsOpenAnimation) {
            canvas.drawArc(left, top, right, bottom, mStartDegree, mDrawDegree, false, mPaint);
            if (++mDrawDegree <= mProgressDegree) {
                invalidate();
            }
        } else {
            canvas.drawArc(left, top, right, bottom, mStartDegree, mProgressDegree, false, mPaint);
        }
    }
}
