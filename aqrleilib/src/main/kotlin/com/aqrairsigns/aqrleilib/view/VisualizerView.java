package com.aqrairsigns.aqrleilib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.aqrairsigns.aqrleilib.R;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/25 Time: 15:16
 */

public class VisualizerView extends View {
    private Paint mPaint;


    private byte[] bytes;
    private int[] mMultiColor;
    private RectF mRectF;
    private float mRadius;
    private int mScaleTime;
    private int mColor;
    private int mOffSetX;
    private boolean isMultiColor;

    public VisualizerView(Context context) {
        this(context, null);
    }

    public VisualizerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VisualizerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2.0F);
        mRectF = new RectF();
        mMultiColor = new int[]{Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.CYAN};
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VisualizerView);
        mOffSetX = typedArray.getInteger(R.styleable.VisualizerView_offSetX, 256);
        mRadius = typedArray.getDimension(R.styleable.VisualizerView_vRadius, 120F);
        mScaleTime = typedArray.getInteger(R.styleable.VisualizerView_scaleTime, 3);
        mColor = typedArray.getColor(R.styleable.VisualizerView_color, Color.GREEN);
        isMultiColor = typedArray.getBoolean(R.styleable.VisualizerView_useMultiColor, false);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public synchronized void setBytes(byte[] bytes) {
        this.bytes = bytes;
        invalidate();
    }

    public int[] getMultiColor() {
        return mMultiColor;
    }

    public void setMultiColor(int[] multiColor) {
        mMultiColor = multiColor;
    }

    public boolean isMultiColor() {
        return isMultiColor;
    }

    public void setMultiColor(boolean multiColor) {
        isMultiColor = multiColor;

    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;

    }

    public int getScaleTime() {
        return mScaleTime;
    }

    public void setScaleTime(int scaleTime) {
        mScaleTime = scaleTime;

    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;

    }

    public int getOffSetX() {
        return mOffSetX;
    }

    public void setOffSetX(int offSetX) {
        mOffSetX = offSetX;

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bytes == null) {
            return;
        }
        canvas.save();
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;
        mRectF.set(centerX - mRadius, centerY - mRadius, centerX + mRadius, centerY + mRadius);
        mPaint.setColor(Color.BLUE);
        mPaint.setAlpha(0);
        canvas.drawArc(mRectF, 0f, 360f, false, mPaint);
        float rotateD = 360F / bytes.length;
        float startX, startY, stopX, stopY;
        mPaint.setAlpha(255);
        mPaint.setColor(mColor);
        float scaleRatio = (float) (1.0 / mScaleTime);
        for (int j = mScaleTime; j > 0; j--) {
            if (isMultiColor) {
                mPaint.setColor(mMultiColor[j % 5]);
            }
            canvas.scale(j * scaleRatio, j * scaleRatio, centerX, centerY);
            for (int i = 0; i < bytes.length - 1; i++) {//X0, Y0, X1, Y1
                startX = (centerX + mRadius) + (bytes[i] + mOffSetX);
                startY = centerY;
                stopX = (centerX + mRadius) + (bytes[i + 1] + mOffSetX);
                stopY = centerY;
                canvas.drawLine(startX, startY, stopX, stopY, mPaint);
                canvas.rotate(rotateD, centerX, centerY);
            }
        }
        canvas.restore();
    }
}
