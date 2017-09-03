package com.leilei.guoshujinfu.mylearning.util.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.leilei.guoshujinfu.mylearning.R;

import java.util.ArrayList;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/30
 */
/*
* @param context 上下文关系
* @param attrs 属性
* @param defStyleAttr 默认属性
* @param defStyleRes 默认资源ID
* @description: 画一个圆形或半圆的简单进度条
* */
public class SemiRoundBar extends View {

    private Paint mPaint;//画笔
    private Context mContext;//上下文
    private int mBackgroundColor;//背景色
    private int mProgressColor;//进度色
    private float mRadius;//圆半径
    private float mMaxProgress;//最大进度
    private float mCurrentProgress;//当前的进度
    private boolean mIsOpenAnimation;//是否开启动画
    private int mStartDegree;//画圆开始的角度
    private int mSweepDegree;//画圆扫过的角度
    private int mProgressDegree;// 进度该画的角度
    private int mDrawDegree;//画进度的起始角度
    /*用于存储相关数据用于恢复图形*/
    private static final String
            INSTANCE,
            INSTANCE_ANIMATION,
            INSTANCE_DEGREE;

    static {
        INSTANCE = "instance";
        INSTANCE_ANIMATION = "animation";
        INSTANCE_DEGREE = "degree";

    }


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
        /*与values文件夹下的属性文件相关联*/
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


        mPaint.setAntiAlias(true);

        setProgress(mCurrentProgress);
    }


    public void setOpenAnimation(boolean openAnimation) {
        mIsOpenAnimation = openAnimation;
    }
    /*synchronized 同步关键字，多个对象访问同一个方法时保持同步*/

    public synchronized void setMaxProgress(float maxProgress) {
        mMaxProgress = maxProgress;
        setProgress(mCurrentProgress);
    }

    public synchronized void setProgress(float progress) {
        mProgressDegree = (int) (mSweepDegree * (progress / mMaxProgress));
        mProgressDegree = mProgressDegree > mSweepDegree ? mSweepDegree : mProgressDegree;
        mDrawDegree = 0;
        postInvalidate();//可在子线程中更新
    }

    public synchronized float getProgress() {
        return mCurrentProgress;
    }

    public synchronized float getMaxProgress() {
        return mMaxProgress;
    }

    public void setProgressColor(int color) {

    }

    /*画图*/
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();


        float centerX = width / 2;
        float centerY = height / 2;
        float left = centerX - mRadius;
        float top = centerY - mRadius;
        float right = centerX + mRadius;
        float bottom = centerY + mRadius;
        //Shader shader = new SweepGradient(centerX, centerY, colors, null);
        //mPaint.setShader(shader);
        mPaint.setColor(mBackgroundColor);
        mPaint.setColor(0XFFFFFFFF);
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

    /*异常销毁时保存数据*/
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_ANIMATION, mIsOpenAnimation);
        ArrayList<Integer> degree = new ArrayList<>();
        degree.add(mStartDegree);
        degree.add(mSweepDegree);
        degree.add(mProgressDegree);
        bundle.putIntegerArrayList(INSTANCE_DEGREE, degree);
        return bundle;
    }

    /*通过数据恢复图形*/
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mIsOpenAnimation = bundle.getBoolean(INSTANCE_ANIMATION);
            ArrayList<Integer> degree = bundle.getIntegerArrayList(INSTANCE_DEGREE);
            mStartDegree = degree.get(0);
            mSweepDegree = degree.get(1);
            mProgressDegree = degree.get(2);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
