package com.example.testapp;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/25 Time: 15:16
 */

public class TestView extends View {
    private Paint mPaint;
    private Camera mCamera;
    private float degree = 0F;
    private Matrix mMatrix;

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2.0F);
        mPaint.setStyle(Paint.Style.STROKE);
        mCamera = new Camera();
        mMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mMatrix.reset();
        mCamera.save();
        canvas.save();
        mCamera.translate(0.0F, 0.0F, degree);
        mCamera.getMatrix(mMatrix);
        mCamera.restore();

        mMatrix.preTranslate(-getWidth() / 2, 0F);
        mMatrix.postTranslate(getWidth() / 2, 0F);
        canvas.concat(mMatrix);

        canvas.drawCircle(getWidth() / 2F, getHeight() - getWidth() / 4, getWidth() / 4F, mPaint);

        canvas.restore();

        Log.d("camera", "X:\t " + mCamera.getLocationX());
        Log.d("camera", "Matrix:\t " + mMatrix);
        Log.d("camera", "Y:\t " + mCamera.getLocationY());
        Log.d("camera", "Z:\t " + mCamera.getLocationZ());
        if (degree < 10000) {
            degree = degree + 10;

            invalidate();
        }
      /*  while (degree < 720) {
            if (degree == 360) {
                degree = 0F;
            }
            degree++;
            invalidate();
        }*/
    }
}
