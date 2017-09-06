package com.leilei.guoshujinfu.mylearning.util.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.leilei.guoshujinfu.mylearning.R;


/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/9/5
 */

public class LearnView extends View {
    private Paint mPaint;
    private Context mContext;
    private int mType = 0;
    private int mDrawTimes = 1;
    private int mWidth;
    private int mHeight;
    private float mScaleRatio;
    private float mMinScale;
    private float mMaxScale;
    private float mRadius;
    private float mStartX;
    private float mStartY;
    private float mLength;
    float sx = 1.0f, sy = 1.0f;

    public LearnView(Context context) {
        this(context, null);
    }

    public LearnView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LearnView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mPaint = new Paint();
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.LearnView);
        mType = typedArray.getInteger(R.styleable.LearnView_learnType, 0);
        mDrawTimes = typedArray.getInteger(R.styleable.LearnView_learnTimes, 1);
        mHeight = typedArray.getInteger(R.styleable.LearnView_learnHeight, 50);
        mWidth = typedArray.getInteger(R.styleable.LearnView_learnWidth, 50);
        mScaleRatio = typedArray.getFloat(R.styleable.LearnView_learnScale, 0.1f);
        mMinScale = typedArray.getFloat(R.styleable.LearnView_learnMinScale, 0.2f);
        mMaxScale = typedArray.getFloat(R.styleable.LearnView_learnMaxScale, 2.0f);
        mRadius = typedArray.getFloat(R.styleable.LearnView_learnRadius, 100);
        mStartX = typedArray.getFloat(R.styleable.LearnView_learnStartX, 100f);
        mStartY = typedArray.getFloat(R.styleable.LearnView_learnStartY, 100f);
        mLength = typedArray.getFloat(R.styleable.LearnView_learnLength, 200f);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2.0f);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#21ADF1"));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        float centerX = width / 2;
        float centerY = height / 2;


        canvas.save();
        switch (mType) {
            case 0:
                drawScale(canvas, centerX, centerY);
                break;
            case 1:
                drawRotate(canvas, centerX, centerY);
                break;
            case 2:
                drawTranslate(canvas);
                break;
        }

    }

    private void drawScale(Canvas canvas, float centerX, float centerY) {

        canvas.drawRect(centerX - mWidth / 2, centerY - mHeight / 2, centerX + mWidth / 2,
                centerY + mHeight/2, mPaint);
        for (; mDrawTimes > 0; mDrawTimes--) {
            sx = sx - mScaleRatio > mMinScale ? (sx - mScaleRatio) : sx;
            sy = sy - mScaleRatio > mMinScale ? (sy - mScaleRatio) : sy;
            canvas.scale(sx, sy, centerX, centerY);
            canvas.drawRect(centerX - mWidth / 2, centerY - mHeight / 2, centerX + mWidth / 2,
                    centerY + mHeight/2, mPaint);
        }
        canvas.restore();
    }

    private void drawRotate(Canvas canvas, float centerX, float centerY) {
        RectF rectF = new RectF(centerX - mRadius, centerY - mRadius,
                centerX + mRadius, centerY + mRadius);
        canvas.drawArc(rectF, 0f, 360f, false, mPaint);
        for (int i = 0; i < 12; i++) {
            canvas.drawLine(centerX + mRadius - 50f, centerY, centerX + mRadius, centerY, mPaint);
            for (int j = 0; j < 5; j++) {
                canvas.rotate(5f, centerX, centerY);
                canvas.drawLine(centerX + mRadius - 20f, centerY, centerX + mRadius, centerY, mPaint);
                if (j == 2)
                    canvas.drawLine(centerX + mRadius - 35f, centerY, centerX + mRadius, centerY, mPaint);
            }
            canvas.rotate(5f, centerX, centerY);
        }
        canvas.restore();
    }

    private void drawTranslate(Canvas canvas) {
        Path path = new Path();

        int i = 10;
        //path.rMoveTo(mStartX, mStartY);
        //path.moveTo(mStartX, mStartY);
        for (; i > 0; i--) {
           // path.rLineTo(0f, -50f);
            //path.rMoveTo(0f, 50f);
            //path.rLineTo(10f, 0f);
            path.lineTo(mStartX+0f, -50f);
            path.moveTo(0f, 50f);
            path.lineTo(10f, 0f);
            canvas.drawPath(path, mPaint);
            for (int j = 0; j < 9; j++) {
                canvas.translate(10f, 0f);
                path.lineTo(0f, -20f);
                path.lineTo(0f, 20f);
                path.lineTo(10f, 0f);
                if (j == 4) {
                    path.lineTo(0f, -35f);
                    path.lineTo(0f, 35f);
                    path.lineTo(10f, 0f);
                }
                canvas.drawPath(path, mPaint);
            }
            canvas.translate(10f, 0f);
        }
        path.lineTo(0f, -50f);
        path.moveTo(0f, 50f);
        path.lineTo(10f, 0f);
        canvas.drawPath(path, mPaint);
        canvas.restore();

    }
}
