package com.leilei.guoshujinfu.mylearning.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leilei.guoshujinfu.mylearning.R;
import com.leilei.guoshujinfu.mylearning.util.BaseActivity;
import com.leilei.guoshujinfu.mylearning.util.camera.CameraUtil;
import com.leilei.guoshujinfu.mylearning.util.camera.DensityUtil;
import com.leilei.guoshujinfu.mylearning.util.view.camera.MyCameraView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/10
 */

public class CameraActivity extends BaseActivity implements MyCameraView.OnFlashModeChangerListener,
        MyCameraView.onTakePictureListener{
    @BindView(R.id.take_camera)
    MyCameraView mTakeCamera;
    @BindView(R.id.tv_flash_auto)
    TextView mFlashAuto;
    @BindView(R.id.tv_flash_open)
    TextView mFlashOpen;
    @BindView(R.id.tv_flash_close)
    TextView mFlashClose;
    @BindView(R.id.ll_mode_flash)
    LinearLayout mModuleFlash;
    @BindView(R.id.iv_flash_mode)
    ImageView mFlashMode;
    @BindView(R.id.iv_capture)
    ImageView mCaptureIV;
    @BindView(R.id.iv_review_img)
    ImageView mReviewIV;


    private Bitmap mBitmap;

    @Override
    protected void beforeSetCOntentView() {
        super.beforeSetCOntentView();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_camera;
    }

    @Override
    protected void initComponents(Bundle savedInstanceState) {
        super.initComponents(savedInstanceState);
        mTakeCamera.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mTakeCamera.autoFocus(((int) event.getX()), ((int) event.getY()));
                return true;
            }
        });
    }
    private void releaseBitmap() {
        if(mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    @Override
    protected void onDestroy() {
        releaseBitmap();
        super.onDestroy();
    }
    @OnClick({R.id.tv_flash_auto, R.id.tv_flash_close, R.id.tv_flash_open,
              R.id.iv_flash_mode, R.id.iv_camera_switch, R.id.iv_capture,
              R.id.iv_review_img, R.id.tv_capture_finish})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_flash_auto:
                mTakeCamera.changeFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                break;
            case R.id.tv_flash_close:
                mTakeCamera.changeFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                break;
            case R.id.tv_flash_open:
                mTakeCamera.changeFlashMode(Camera.Parameters.FLASH_MODE_ON);
                break;
            case R.id.iv_capture:
                mCaptureIV.setClickable(false);
                mTakeCamera.takePicture();
                break;
            case R.id.iv_review_img:
                break;
            case R.id.tv_capture_finish:
                break;

        }
    }

    @Override
    public void onFlashMoChange(String flashMode) {
        mModuleFlash.setVisibility(View.GONE);
        mFlashMode.setVisibility(View.VISIBLE);
        switch(flashMode) {
            case Camera.Parameters.FLASH_MODE_AUTO:
                mFlashAuto.setTextColor(Color.parseColor("#ff1b8fe6"));
                mFlashOpen.setTextColor(Color.parseColor("#ffffffff"));
                mFlashClose.setTextColor(Color.parseColor("#ffffffff"));
                mFlashMode.getDrawable().setLevel(0);
                break;
            case Camera.Parameters.FLASH_MODE_OFF:
                mFlashAuto.setTextColor(Color.parseColor("#ffffffff"));
                mFlashOpen.setTextColor(Color.parseColor("#ffffffff"));
                mFlashClose.setTextColor(Color.parseColor("#ff1b8fe6"));
                mFlashMode.getDrawable().setLevel(1);
                break;
            case Camera.Parameters.FLASH_MODE_ON:
                mFlashAuto.setTextColor(Color.parseColor("#ffffffff"));
                mFlashOpen.setTextColor(Color.parseColor("#ff1b8fe6"));
                mFlashClose.setTextColor(Color.parseColor("#ffffffff"));
                mFlashMode.getDrawable().setLevel(2);
                break;
        }

    }

    @Override
    public void onTakePicture(boolean isSuccess, String filePath) {
        if(isSuccess) {
            releaseBitmap();
            mBitmap = CameraUtil.compressBitmapInSize(filePath, DensityUtil.dip2px(this,60), DensityUtil.dip2px(this, 60));
            mReviewIV.setVisibility(View.VISIBLE);
            mReviewIV.setImageBitmap(mBitmap);


        }
        mCaptureIV.setClickable(true);

    }
}
