package com.aqrlei.graduation.yueting;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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
    private byte[] bytes;
    private float[] points;
    private RectF rect;

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
        rect = new RectF();
    }

    public void Update(byte[] wave) {
        bytes = wave;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bytes == null) {
            return;
        }

        mMatrix.reset();
        mCamera.save();
        canvas.save();
        mCamera.translate(0.0F, 0.0F, degree);
        mCamera.getMatrix(mMatrix);
        mCamera.restore();
        rect.set(0, 0, getWidth(), getWidth());
        mMatrix.preTranslate(-getWidth() / 2, 0F);
        mMatrix.postTranslate(getWidth() / 2, 0F);
        canvas.concat(mMatrix);
        if (points == null || points.length < bytes.length * 4) {
            points = new float[bytes.length * 4];
        }
        for (int i = 0; i < bytes.length - 1; i++) {
            points[i * 4] = rect.width() * i / (bytes.length - 1);
            points[i * 4 + 1] = (rect.height() / 2) + ((byte) (bytes[i] + 128)) * 128 / (rect.height() / 2);
            points[i * 4 + 2] = rect.width() * (i + 1) / (bytes.length - 1);
            points[i * 4 + 3] = (rect.height() / 2) + ((byte) (bytes[i + 1] + 128)) * 128 / (rect.height() / 2);
        }

        canvas.drawLines(points, mPaint);

        canvas.drawCircle(getWidth() / 2F, getHeight() - getWidth() / 4, getWidth() / 4F, mPaint);

        canvas.restore();
        if (degree < 10000) {
            degree = degree + 100;

            invalidate();
        }

    }
}
